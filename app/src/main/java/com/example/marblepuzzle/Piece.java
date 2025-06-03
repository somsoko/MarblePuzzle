package com.example.marblepuzzle;

public class Piece {
    private String name;
    private float[] center;
    private float[] XY = new float[2];
    private int[][] offset;
    private int rotate;
    private boolean mirrorVer, mirrorHor;


    public Piece(String name, float[] center, int[][] offset, int rotate, boolean mirrorVer, boolean mirrorHor) {
        this.name = name;
        this.center = center;
        this.offset = offset;
        this.rotate = rotate;
        this.mirrorVer = mirrorVer;
        this.mirrorHor = mirrorHor;
    }

    public String getName() {
        return name;
    }

    public float[] getCenter() {
        return center;
    }

    public float[] getXY() {
        return XY;
    }

    public int getRotate() {
        return rotate;
    }

    public boolean isMirrorVer() {
        return mirrorVer;
    }

    public boolean isMirrorHor() {
        return mirrorHor;
    }

    public int[][] getOffset() {
        return offset;
    }

    public void setXY(float x, float y) {
        XY[0] = x;
        XY[1] = y;
    }

    public void setOffset(int[][] offset) {
        this.offset = offset;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setMirrorVer(boolean mirrorVer) {
        this.mirrorVer = mirrorVer;
    }

    public void setMirrorHor(boolean mirrorHor) {
        this.mirrorHor = mirrorHor;
    }



    public void rotate() {
        for (int i = 0; i < offset.length; i++) {
            int x = offset[i][0];
            int y = offset[i][1];
            offset[i][0] = y;
            offset[i][1] = -x;
        }

        float cx = center[0];
        float cy = center[1];
        center[0] = 150-cy;
        center[1] = cx;
    }

    public void mirrorVer() {
        for(int i=0; i< offset.length; i++) {
            int x = offset[i][0];
            int y = offset[i][1];
            offset[i][0] = x;
            offset[i][1] = -y;
        }

        float cx = center[0];
        float cy = center[1];
        center[0] = cy;
        center[1] = cx;
    }

    public void mirrorHor() {
        for(int i=0; i< offset.length; i++) {
            int x = offset[i][0];
            int y = offset[i][1];
            offset[i][0] = -x;
            offset[i][1] = y;
        }

        float cx = center[0];
        float cy = center[1];
        center[0] = 150-cy;
        center[1] = 150-cx;

    }


}
