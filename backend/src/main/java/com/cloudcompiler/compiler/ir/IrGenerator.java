package com.cloudcompiler.compiler.ir;

import com.cloudcompiler.compiler.ast.*;

import java.util.*;

public class IrGenerator implements AstVisitor<String> {
    private final List<TacInstruction> tacInstructions = new ArrayList<>();
    private final List<Quadruple> quadruples = new ArrayList<>();
    private final List<Triple> triples = new ArrayList<>();
    private final List<IndirectTriple> indirectTriples = new ArrayList<>();

    private int tempCounter = 1;
    private int labelCounter = 1;
    private final Map<String, String> tempToTripleIndexMap = new HashMap<>();

    public List<TacInstruction> getTacInstructions() {
        return tacInstructions;
    }

    public List<Quadruple> getQuadruples() {
        return quadruples;
    }

    public List<Triple> getTriples() {
        return triples;
    }

    public List<IndirectTriple> getIndirectTriples() {
        return indirectTriples;
    }

    public void generate(ProgramNode program) {
        if (program != null) {
            program.accept(this);
            buildTriplesAndIndirect();
        }
    }

    private String newTemp() {
        return "t" + (tempCounter++);
    }

    private String newLabel() {
        return "L" + (labelCounter++);
    }

    private void emit(String op, String arg1, String arg2, String result) {
        int idx = tacInstructions.size();
        TacInstruction tac = TacInstruction.builder()
                .index(idx)
                .op(op)
                .arg1(arg1)
                .arg2(arg2)
                .result(result)
                .build();
        tac.setFormatted(tac.toTacString());
        tacInstructions.add(tac);

        Quadruple quad = Quadruple.builder()
                .index(idx)
                .op(op)
                .arg1(arg1 != null ? arg1 : "")
                .arg2(arg2 != null ? arg2 : "")
                .result(result != null ? result : "")
                .build();
        quadruples.add(quad);
    }

    private void emitLabel(String label) {
        int idx = tacInstructions.size();
        TacInstruction tac = TacInstruction.builder()
                .index(idx)
                .op("LABEL")
                .label(label)
                .formatted(label + ":")
                .build();
        tacInstructions.add(tac);

        Quadruple quad = Quadruple.builder()
                .index(idx)
                .op("LABEL")
                .arg1("")
                .arg2("")
                .result(label)
                .build();
        quadruples.add(quad);
    }

    private void buildTriplesAndIndirect() {
        tempToTripleIndexMap.clear();

        for (int i = 0; i < quadruples.size(); i++) {
            Quadruple q = quadruples.get(i);
            String arg1Ref = mapOperandToTripleRef(q.getArg1());
            String arg2Ref = mapOperandToTripleRef(q.getArg2());

            Triple t = Triple.builder()
                    .index(i)
                    .op(q.getOp())
                    .arg1(arg1Ref)
                    .arg2(arg2Ref)
                    .build();
            triples.add(t);

            if (q.getResult() != null && q.getResult().startsWith("t")) {
                tempToTripleIndexMap.put(q.getResult(), "(" + i + ")");
            }

            IndirectTriple it = IndirectTriple.builder()
                    .pointer(100 + i)
                    .tripleIndex(i)
                    .statement(q.getOp() + " " + arg1Ref + (arg2Ref.isEmpty() ? "" : ", " + arg2Ref) + (q.getResult().isEmpty() ? "" : " -> " + q.getResult()))
                    .build();
            indirectTriples.add(it);
        }
    }

    private String mapOperandToTripleRef(String operand) {
        if (operand == null || operand.isEmpty()) return "";
        if (tempToTripleIndexMap.containsKey(operand)) {
            return tempToTripleIndexMap.get(operand);
        }
        return operand;
    }

    @Override
    public String visit(ProgramNode node) {
        for (AstNode decl : node.getDeclarations()) {
            decl.accept(this);
        }
        return null;
    }

    @Override
    public String visit(FunctionDeclNode node) {
        emitLabel("func_" + node.getName());

        for (ParameterNode p : node.getParameters()) {
            emit("PARAM_DECL", p.getType(), "", p.getName());
        }

        if (node.getBody() != null) {
            node.getBody().accept(this);
        }

        emit("FUNC_END", node.getName(), "", "");
        return null;
    }

    @Override
    public String visit(ParameterNode node) {
        return node.getName();
    }

    @Override
    public String visit(VarDeclNode node) {
        if (node.getInitializer() != null) {
            String initVal = node.getInitializer().accept(this);
            emit("=", initVal, "", node.getName());
        }
        return node.getName();
    }

    @Override
    public String visit(BlockNode node) {
        for (AstNode stmt : node.getStatements()) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public String visit(IfStmtNode node) {
        String condVal = node.getCondition().accept(this);
        String labelElse = newLabel();
        String labelEnd = newLabel();

        if (node.getElseBranch() != null) {
            emit("IF_FALSE", condVal, "", labelElse);
            node.getThenBranch().accept(this);
            emit("GOTO", "", "", labelEnd);
            emitLabel(labelElse);
            node.getElseBranch().accept(this);
            emitLabel(labelEnd);
        } else {
            emit("IF_FALSE", condVal, "", labelEnd);
            node.getThenBranch().accept(this);
            emitLabel(labelEnd);
        }

        return null;
    }

    @Override
    public String visit(WhileStmtNode node) {
        String labelStart = newLabel();
        String labelEnd = newLabel();

        emitLabel(labelStart);
        String condVal = node.getCondition().accept(this);
        emit("IF_FALSE", condVal, "", labelEnd);

        if (node.getBody() != null) {
            node.getBody().accept(this);
        }

        emit("GOTO", "", "", labelStart);
        emitLabel(labelEnd);
        return null;
    }

    @Override
    public String visit(ForStmtNode node) {
        String labelStart = newLabel();
        String labelEnd = newLabel();

        if (node.getInit() != null) {
            node.getInit().accept(this);
        }

        emitLabel(labelStart);
        if (node.getCondition() != null) {
            String condVal = node.getCondition().accept(this);
            emit("IF_FALSE", condVal, "", labelEnd);
        }

        if (node.getBody() != null) {
            node.getBody().accept(this);
        }

        if (node.getUpdate() != null) {
            node.getUpdate().accept(this);
        }

        emit("GOTO", "", "", labelStart);
        emitLabel(labelEnd);
        return null;
    }

    @Override
    public String visit(ReturnStmtNode node) {
        String retVal = "";
        if (node.getValue() != null) {
            retVal = node.getValue().accept(this);
        }
        emit("RETURN", retVal, "", "");
        return null;
    }

    @Override
    public String visit(PrintStmtNode node) {
        String val = "";
        if (node.getExpression() != null) {
            val = node.getExpression().accept(this);
        }
        emit("PRINT", val, "", "");
        return null;
    }

    @Override
    public String visit(ReadStmtNode node) {
        emit("READ", "", "", node.getTargetVar());
        return null;
    }

    @Override
    public String visit(ExpressionStmtNode node) {
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return null;
    }

    @Override
    public String visit(AssignStmtNode node) {
        String val = node.getValue().accept(this);
        String op = node.getOperator();

        if ("=".equals(op)) {
            emit("=", val, "", node.getName());
        } else {
            String baseOp = switch (op) {
                case "+=" -> "+";
                case "-=" -> "-";
                case "*=" -> "*";
                case "/=" -> "/";
                default -> "+";
            };
            String temp = newTemp();
            emit(baseOp, node.getName(), val, temp);
            emit("=", temp, "", node.getName());
        }

        return node.getName();
    }

    @Override
    public String visit(BinaryExprNode node) {
        String left = node.getLeft().accept(this);
        String right = node.getRight().accept(this);
        String temp = newTemp();
        emit(node.getOperator(), left, right, temp);
        return temp;
    }

    @Override
    public String visit(UnaryExprNode node) {
        String operand = node.getOperand().accept(this);
        String op = node.getOperator();

        if ("++".equals(op) || "--".equals(op)) {
            String mathOp = "++".equals(op) ? "+" : "-";
            if (node.isPrefix()) {
                emit(mathOp, operand, "1", operand);
                return operand;
            } else {
                String temp = newTemp();
                emit("=", operand, "", temp);
                emit(mathOp, operand, "1", operand);
                return temp;
            }
        }

        String temp = newTemp();
        emit(op, operand, "", temp);
        return temp;
    }

    @Override
    public String visit(LiteralExprNode node) {
        return String.valueOf(node.getValue());
    }

    @Override
    public String visit(IdentifierExprNode node) {
        return node.getName();
    }

    @Override
    public String visit(FunctionCallExprNode node) {
        List<String> argTemps = new ArrayList<>();
        for (AstNode arg : node.getArguments()) {
            argTemps.add(arg.accept(this));
        }

        for (String arg : argTemps) {
            emit("PARAM", arg, "", "");
        }

        String temp = newTemp();
        emit("CALL", node.getName(), String.valueOf(argTemps.size()), temp);
        return temp;
    }
}
