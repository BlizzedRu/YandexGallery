package ru.blizzed.yandexgallery.ui.screens.files.model;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import ru.blizzed.yandexgallery.ui.screens.files.FilesContract;

public class FileImagesRepository implements FilesContract.Model {

    private static final List<String> IMG_EXTENSIONS = Arrays.asList("jpg", "png", "jpeg", "bmp", "gif", "ico");
    private static final List<String> FOLDERS_BLACK_LIST = Arrays.asList("/storage/emulated/0/Android");

    @Override
    public Flowable<FileImagesFolder> getImageFolders() {
        return Flowable.create(emitter -> {
            File root = getRootFile();
            handleDirectory(root, emitter);
        }, BackpressureStrategy.BUFFER);
    }

    private void handleDirectory(File directory, FlowableEmitter<FileImagesFolder> emitter) {
        if (FOLDERS_BLACK_LIST.contains(directory.getAbsolutePath()) || directory.getName().startsWith("."))
            return;
        new Thread(() -> {
            FileImagesFolder fileImagesFolder = null;
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) handleDirectory(file, emitter);
                else if (isFileAnImage(file)) {
                    if (fileImagesFolder == null)
                        fileImagesFolder = new FileImagesFolder(directory);
                    fileImagesFolder.addImage(new FileImage(file));
                }
            }
            if (fileImagesFolder != null) emitter.onNext(fileImagesFolder);

            Thread.currentThread().interrupt();
        }).run();
    }

    private File getRootFile() {
        return Environment.getExternalStorageDirectory(); // TODO: 19.04.2018 PERMISSION
    }

    private boolean isFileAnImage(File file) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        return IMG_EXTENSIONS.contains(ext);
    }

}
