package ru.blizzed.yandexgallery.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import ru.blizzed.pixabaylib.model.PixabayImage;

@Entity
public class URLImage implements Parcelable {

    @NonNull
    @PrimaryKey
    private String largeURL;
    private String mediumURL;
    private String previewURL;

    private int mediumWidth;
    private int mediumHeight;

    private String type;

    private long timestamp;

    @Ignore
    public URLImage(PixabayImage pixabayImage) {
        largeURL = pixabayImage.getLargeImageURL();
        mediumURL = pixabayImage.getWebformatURL();
        previewURL = pixabayImage.getPreviewURL();
        mediumWidth = (int) pixabayImage.getWebformatWidth();
        mediumHeight = (int) pixabayImage.getWebformatHeight();
        type = pixabayImage.getType();
    }

    public URLImage() {
    }

    public static final Parcelable.Creator<URLImage> CREATOR = new Parcelable.Creator<URLImage>() {

        @Override
        public URLImage createFromParcel(Parcel source) {
            URLImage image = new URLImage();
            String[] stringData = new String[4];
            source.readStringArray(stringData);
            image.setLargeURL(stringData[0]);
            image.setMediumURL(stringData[1]);
            image.setPreviewURL(stringData[2]);
            image.setType(stringData[3]);

            int[] intData = new int[2];
            source.readIntArray(intData);
            image.setMediumWidth(intData[0]);
            image.setMediumHeight(intData[1]);
            return image;
        }

        @Override
        public URLImage[] newArray(int size) {
            return new URLImage[size];
        }
    };

    @NonNull
    public String getLargeURL() {
        return largeURL;
    }

    public String getMediumURL() {
        return mediumURL;
    }

    public void setMediumURL(String mediumURL) {
        this.mediumURL = mediumURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public int getMediumWidth() {
        return mediumWidth;
    }

    public void setMediumWidth(int mediumWidth) {
        this.mediumWidth = mediumWidth;
    }

    public int getMediumHeight() {
        return mediumHeight;
    }

    public void setMediumHeight(int mediumHeight) {
        this.mediumHeight = mediumHeight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLargeURL(@NonNull String largeURL) {
        this.largeURL = largeURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{largeURL, mediumURL, previewURL, type});
        dest.writeIntArray(new int[]{mediumWidth, mediumHeight});
    }

}
