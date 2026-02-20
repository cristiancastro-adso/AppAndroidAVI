package com.pipe.avi.model;

public class RiasecScores {

    private int R;
    private int I;
    private int A;
    private int S;
    private int E;
    private int C;

    public RiasecScores(int r, int i, int a, int s, int e, int c) {
        this.R = r;
        this.I = i;
        this.A = a;
        this.S = s;
        this.E = e;
        this.C = c;
    }

    public void updateScore(String category, int value) {

        if (category == null) return;

        switch (category.toUpperCase()) {
            case "R":
                R += value;
                break;
            case "I":
                I += value;
                break;
            case "A":
                A += value;
                break;
            case "S":
                S += value;
                break;
            case "E":
                E += value;
                break;
            case "C":
                C += value;
                break;
        }
    }

    public int getR() { return R; }
    public int getI() { return I; }
    public int getA() { return A; }
    public int getS() { return S; }
    public int getE() { return E; }
    public int getC() { return C; }
}

