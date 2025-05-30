package com.example.marblepuzzle;

public class StageInfo {
    private String stageName;
    private int star;
    private boolean[] usedPiece = new boolean[12];

    public StageInfo(String stageName, boolean cleared, int star) {
        this.stageName = stageName;
        this.star = star;
    }

    public String getStageName() {
        return stageName;
    }

    public int getStar() {
        return star;
    }
}
