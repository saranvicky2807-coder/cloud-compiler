package com.cloudcompiler.compiler.ir;

public class TacInstruction {
    private int index;
    private String op;
    private String arg1;
    private String arg2;
    private String result;
    private String label;
    private String formatted;

    public TacInstruction() {}

    public TacInstruction(int index, String op, String arg1, String arg2, String result, String label, String formatted) {
        this.index = index;
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
        this.label = label;
        this.formatted = formatted;
    }

    public static TacInstructionBuilder builder() {
        return new TacInstructionBuilder();
    }

    public static class TacInstructionBuilder {
        private int index;
        private String op;
        private String arg1;
        private String arg2;
        private String result;
        private String label;
        private String formatted;

        public TacInstructionBuilder index(int index) { this.index = index; return this; }
        public TacInstructionBuilder op(String op) { this.op = op; return this; }
        public TacInstructionBuilder arg1(String arg1) { this.arg1 = arg1; return this; }
        public TacInstructionBuilder arg2(String arg2) { this.arg2 = arg2; return this; }
        public TacInstructionBuilder result(String result) { this.result = result; return this; }
        public TacInstructionBuilder label(String label) { this.label = label; return this; }
        public TacInstructionBuilder formatted(String formatted) { this.formatted = formatted; return this; }

        public TacInstruction build() {
            return new TacInstruction(index, op, arg1, arg2, result, label, formatted);
        }
    }

    public String toTacString() {
        if (formatted != null && !formatted.isEmpty()) {
            return formatted;
        }

        if (label != null && !label.isEmpty()) {
            return label + ":";
        }

        if ("=".equals(op)) {
            return result + " = " + arg1;
        }

        if ("PARAM".equals(op)) {
            return "PARAM " + arg1;
        }

        if ("CALL".equals(op)) {
            if (result != null && !result.isEmpty()) {
                return result + " = CALL " + arg1 + ", " + arg2;
            }
            return "CALL " + arg1 + ", " + arg2;
        }

        if ("RETURN".equals(op)) {
            if (arg1 != null && !arg1.isEmpty()) {
                return "RETURN " + arg1;
            }
            return "RETURN";
        }

        if ("PRINT".equals(op)) {
            return "PRINT " + arg1;
        }

        if ("READ".equals(op)) {
            return "READ " + result;
        }

        if ("GOTO".equals(op)) {
            return "GOTO " + result;
        }

        if ("IF_FALSE".equals(op)) {
            return "IF_FALSE " + arg1 + " GOTO " + result;
        }

        if ("IF_TRUE".equals(op)) {
            return "IF " + arg1 + " GOTO " + result;
        }

        if ("IF".equals(op)) {
            return "IF " + arg1 + " " + op + " " + arg2 + " GOTO " + result;
        }

        if (arg2 != null && !arg2.isEmpty()) {
            return result + " = " + arg1 + " " + op + " " + arg2;
        }

        if (arg1 != null && !arg1.isEmpty()) {
            return result + " = " + op + " " + arg1;
        }

        return op + " " + (result != null ? result : "");
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }
    public String getArg1() { return arg1; }
    public void setArg1(String arg1) { this.arg1 = arg1; }
    public String getArg2() { return arg2; }
    public void setArg2(String arg2) { this.arg2 = arg2; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getFormatted() { return formatted; }
    public void setFormatted(String formatted) { this.formatted = formatted; }
}
