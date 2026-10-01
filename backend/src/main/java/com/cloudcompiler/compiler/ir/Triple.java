package com.cloudcompiler.compiler.ir;

public class Triple {
    private int index;
    private String op;
    private String arg1;
    private String arg2;

    public Triple() {}

    public Triple(int index, String op, String arg1, String arg2) {
        this.index = index;
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public static TripleBuilder builder() {
        return new TripleBuilder();
    }

    public static class TripleBuilder {
        private int index;
        private String op;
        private String arg1;
        private String arg2;

        public TripleBuilder index(int index) { this.index = index; return this; }
        public TripleBuilder op(String op) { this.op = op; return this; }
        public TripleBuilder arg1(String arg1) { this.arg1 = arg1; return this; }
        public TripleBuilder arg2(String arg2) { this.arg2 = arg2; return this; }

        public Triple build() {
            return new Triple(index, op, arg1, arg2);
        }
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public String getOp() { return op; }
    public void setOp(String op) { this.op = op; }
    public String getArg1() { return arg1; }
    public void setArg1(String arg1) { this.arg1 = arg1; }
    public String getArg2() { return arg2; }
    public void setArg2(String arg2) { this.arg2 = arg2; }
}
