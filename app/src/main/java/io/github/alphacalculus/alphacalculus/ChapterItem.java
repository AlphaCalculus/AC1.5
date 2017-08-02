package io.github.alphacalculus.alphacalculus;

import android.os.Parcel;
import android.os.Parcelable;

public class ChapterItem implements Parcelable {

    private String name;
    private String content;

    private int imageId;

    public ChapterItem(String name, int imageId, String content) {
        this.name = name;
        this.imageId = imageId;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getImageId() {
        return imageId;
    }

    // Parcelling part
    public ChapterItem(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.name = data[0];
        this.content = data[1];
        this.imageId = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name, this.content});
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