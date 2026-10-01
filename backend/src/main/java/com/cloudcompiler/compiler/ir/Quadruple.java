package com.cloudcompiler.compiler.ir;

public class Quadruple {
    private int index;
    private String op;
    private String arg1;
    private String arg2;
    private String result;

    public Quadruple() {}

    public Quadruple(int index, String op, String arg1, String arg2, String result) {
        this.index = index;
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public static QuadrupleBuilder builder() {
        return new QuadrupleBuilder();
    }

    public static class QuadrupleBuilder {
        private int index;
        private String op;
        private String arg1;
        private String arg2;
        private String result;

        public QuadrupleBuilder index(int index) { this.index = index; return this; }
        public QuadrupleBuilder op(String op) { this.op = op; return this; }
        public QuadrupleBuilder arg1(String arg1) { this.arg1 = arg1; return this; }
        public QuadrupleBuilder arg2(String arg2) { this.arg2 = arg2; return this; }
        public QuadrupleBuilder result(String result) { this.result = result; return this; }

        public Quadruple build() {
            return new Quadruple(index, op, arg1, arg2, result);
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
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
