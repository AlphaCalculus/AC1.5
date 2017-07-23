package com.example.materialtest;

public class Chapter {
    private String chapterName;

    private int chapterImageId;

    public Chapter(String chapterName, int chapterImageId) {
        this.chapterImageId = chapterImageId;
        this.chapterName = chapterName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public int getChapterImageId() {
        return chapterImageId;
    }
}
