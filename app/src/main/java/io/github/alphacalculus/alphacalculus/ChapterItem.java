package io.github.alphacalculus.alphacalculus;

public class ChapterItem {

    private String name;

    private int imageId;

    public ChapterItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

}