package ru.blizzed.yandexgallery.ui.screens.files.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileImagesFolder {

    private String title;

    private List<FileImage> imagesList;

    private File file;

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

    public void addImage(FileImage image) {
        imagesList.add(image);
    }

    public File getFile() {
        return file;
    }

}
