package ru.blizzed.yandexgallery.ui.screens.files.model;

import java.io.File;

public class FileImage {

    private File file;

    public FileImage(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
