package ru.blizzed.yandexgallery.data.model.fileimage;

public class FileImagesFolderEvent {

    private FileImagesFolder folder;
    private Type type;
    private FileImage cause;

    public FileImagesFolderEvent(FileImagesFolder folder, Type type, FileImage cause) {
        this.folder = folder;
        this.type = type;
        this.cause = cause;
    }

    public FileImagesFolder getFolder() {
        return folder;
    }

    public Type getType() {
        return type;
    }

    public FileImage getCause() {
        return cause;
    }

    public enum Type {
        FILE_DELETED, FILE_ADDED
    }

}
