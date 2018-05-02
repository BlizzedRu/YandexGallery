package ru.blizzed.yandexgallery.ui.screens.files.model;

import android.os.Parcel;

import java.io.File;

import ru.blizzed.yandexgallery.model.Image;

public class FileImage implements Image {

    private File file;

    public FileImage(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    final static Creator<FileImage> CREATOR = new Creator<FileImage>() {
        @Override
        public FileImage createFromParcel(Parcel source) {
            return new FileImage((File) source.readSerializable());
        }

        @Override
        public FileImage[] newArray(int size) {
            return new FileImage[0];
        }
    };

    @Override
    public String getMediumURL() {
        return file.getAbsolutePath();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(file);
    }

}
