package com.example.marblepuzzle;

public class DifficultyInfo {
    private String difficulty;
    private int total;
    private int cleared;
    private boolean locked;

    public DifficultyInfo(String difficulty, int total, int cleared, boolean locked) {
        this.difficulty = difficulty;
        this.total = total;
        this.cleared = cleared;
        this.locked = locked;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getTotal() {
        return total;
    }

    public int getCleared() {
        return cleared;
    }

    public boolean getLocked() {
        return locked;
    }
}
