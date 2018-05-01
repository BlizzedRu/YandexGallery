package ru.blizzed.yandexgallery.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

import ru.blizzed.pixabaylib.model.PixabayImage;

@Entity
public class URLImage implements Serializable {

    @NonNull
    @PrimaryKey
    private String largeURL;
    private String mediumURL;
    private String previewURL;

    private int mediumWidth;
    private int mediumHeight;

    private String type;

    private long timestamp;

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

    public String getLargeURL() {
        return largeURL;
    }

    public void setLargeURL(String largeURL) {
        this.largeURL = largeURL;
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
}
