package ru.blizzed.yandexgallery.ui.screens.files.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesFolder {

    private String title;

    private List<Image> imagesList;

    private File file;

    public ImagesFolder(File file) {
        this.file = file;
        imagesList = new ArrayList<>();
    }

    public String getTitle() {
        return file.getName();
    }

    public List<Image> getImagesList() {
        return imagesList;
    }

    public void addImage(Image image) {
        imagesList.add(image);
    }

    public File getFile() {
        return file;
    }

}
