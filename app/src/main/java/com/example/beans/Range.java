package com.example.beans;

/**
 * 这个类记录 一个区间
 * 在 完型填空 替换答案的时候用到
 */
public class Range {

    private int start;
    private int end;

    public Range(int start,int end){
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
