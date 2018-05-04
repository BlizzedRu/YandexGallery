package ru.blizzed.yandexgallery.data.model.fileimage;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileImagesFolder implements Parcelable {

    public static final Parcelable.Creator<FileImagesFolder> CREATOR = new Parcelable.Creator<FileImagesFolder>() {

        @Override
        public FileImagesFolder createFromParcel(Parcel source) {
            FileImagesFolder folder = new FileImagesFolder();
            folder.title = source.readString();
            folder.file = (File) source.readSerializable();
            folder.imagesList = new ArrayList<>();
            source.readList(folder.imagesList, FileImage.class.getClassLoader());
            return folder;
        }

        @Override
        public FileImagesFolder[] newArray(int size) {
            return new FileImagesFolder[size];
        }
    };

    private String title;

    private List<FileImage> imagesList;

    private File file;

    private FileImagesFolder() {
    }

    public FileImagesFolder(File file) {
        this.file = file;
        imagesList = new ArrayList<>();
    }

    public String getTitle() {
        return file.getName();
    }

    public List<FileImage> getImagesList() {
        return imagesList;
    }

    public File getFile() {
        return file;
    }

    public void addImage(FileImage image) {
        imagesList.add(0, image);
    }

    public void removeImage(FileImage image) {
        imagesList.remove(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeSerializable(file);
        dest.writeList(imagesList);
    }


}
