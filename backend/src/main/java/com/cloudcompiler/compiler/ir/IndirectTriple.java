package com.cloudcompiler.compiler.ir;

public class IndirectTriple {
    private int pointer;
    private int tripleIndex;
    private String statement;

    public IndirectTriple() {}

    public IndirectTriple(int pointer, int tripleIndex, String statement) {
        this.pointer = pointer;
        this.tripleIndex = tripleIndex;
        this.statement = statement;
    }

    public static IndirectTripleBuilder builder() {
        return new IndirectTripleBuilder();
    }

    public static class IndirectTripleBuilder {
        private int pointer;
        private int tripleIndex;
        private String statement;

        public IndirectTripleBuilder pointer(int pointer) { this.pointer = pointer; return this; }
        public IndirectTripleBuilder tripleIndex(int tripleIndex) { this.tripleIndex = tripleIndex; return this; }
        public IndirectTripleBuilder statement(String statement) { this.statement = statement; return this; }

        public IndirectTriple build() {
            return new IndirectTriple(pointer, tripleIndex, statement);
        }
    }

    public int getPointer() { return pointer; }
    public void setPointer(int pointer) { this.pointer = pointer; }
    public int getTripleIndex() { return tripleIndex; }
    public void setTripleIndex(int tripleIndex) { this.tripleIndex = tripleIndex; }
    public String getStatement() { return statement; }
    public void setStatement(String statement) { this.statement = statement; }
}
