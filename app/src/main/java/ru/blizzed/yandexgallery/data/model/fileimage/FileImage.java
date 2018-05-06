package ru.blizzed.yandexgallery.data.model.fileimage;

import android.os.Parcel;

import java.io.File;
import java.util.Objects;

import ru.blizzed.yandexgallery.data.model.Image;

public class FileImage implements Image {

    public final static Creator<FileImage> CREATOR = new Creator<FileImage>() {
        @Override
        public FileImage createFromParcel(Parcel source) {
            return new FileImage((File) source.readSerializable());
        }

        @Override
        public FileImage[] newArray(int size) {
            return new FileImage[0];
        }
    };
    private File file;

    public FileImage(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileImage fileImage = (FileImage) o;
        return Objects.equals(file.getAbsolutePath(), fileImage.file.getAbsolutePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(file.getAbsolutePath());
    }
}
