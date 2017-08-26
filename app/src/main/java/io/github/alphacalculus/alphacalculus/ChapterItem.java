package io.github.alphacalculus.alphacalculus;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

public class ChapterItem implements Parcelable {

    private String name;
    private String content;
    private int partIdx;
    private int chapterIdx;
    private int imageId;

    public ChapterItem(int partIdx, int chapterIdx, String name, int imageId, String content) {
        this.partIdx=partIdx;
        this.chapterIdx=chapterIdx;
        this.name = name;
        this.imageId = imageId;
        this.content = content;
    }

    public int getPartIdx() { return partIdx; }
    public int getChapterIdx() { return chapterIdx; }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getImageId() {
        return imageId;
    }

    public ChapterItem getNextChapter() {
        if (ChapterItemFactory.getInstance().getChapterCount(this.partIdx) > this.chapterIdx + 1) {
            return ChapterItemFactory.getInstance().getChapterCached(this.partIdx, this.chapterIdx+1);
        } else {
            Context context = TheApp.getInstance().getApplicationContext();
            Toast.makeText(context, "已经是最后一章！", Toast.LENGTH_SHORT).show();
            return this;
        }
    }

    public ChapterItem getPreviousChapter() {
        if (this.chapterIdx > 0) {
            return ChapterItemFactory.getInstance().getChapterCached(this.partIdx, this.chapterIdx-1);
        } else {
            Context context = TheApp.getInstance().getApplicationContext();
            Toast.makeText(context, "已经是第一章！", Toast.LENGTH_SHORT).show();
            return this;
        }
    }
    // Parcelling part
    public ChapterItem(Parcel in) {
        String[] data = new String[2];
        int[] ids = new int[2];

        in.readStringArray(data);
        in.readIntArray(ids);
        // the order needs to be the same as in writeToParcel() method
        this.name = data[0];
        this.content = data[1];
        this.imageId = in.readInt();
        this.partIdx = ids[0];
        this.chapterIdx = ids[1];
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name, this.content});
        dest.writeIntArray(new int[]{this.partIdx, this.chapterIdx});
        dest.writeInt(imageId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChapterItem createFromParcel(Parcel in) {
            return new ChapterItem(in);
        }

        public ChapterItem[] newArray(int size) {
            return new ChapterItem[size];
        }
    };

}
