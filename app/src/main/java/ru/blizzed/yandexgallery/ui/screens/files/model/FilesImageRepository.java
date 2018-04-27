package ru.blizzed.yandexgallery.ui.screens.files.model;

import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

public class FilesImageRepository {

    private static List<String> imgExtensions = Arrays.asList("jpg", "png", "jpeg", "bmp", "gif", "ico");
    private static List<String> foldersBlackList = Arrays.asList("/storage/emulated/0/Android");

    public Flowable<ImagesFolder> getImageFolders() {
        return Flowable.create(emitter -> {
            File root = getRootFile();
            Log.d("ru.blizzed", "Root " + root.getAbsolutePath());

            handleDirectory(root, emitter);
            //emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    private void handleDirectory(File directory, FlowableEmitter<ImagesFolder> emitter) {
        if (foldersBlackList.contains(directory.getAbsolutePath()) || directory.getName().startsWith("."))
            return;
        ImagesFolder imagesFolder = null;
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) handleDirectory(file, emitter);
            else if (isFileAnImage(file)) {
                if (imagesFolder == null) imagesFolder = new ImagesFolder(directory);
                imagesFolder.addImage(new Image(file));
            }
        }
        if (imagesFolder != null) emitter.onNext(imagesFolder);
    }

    private File getRootFile() {
        return Environment.getExternalStorageDirectory(); // TODO: 19.04.2018 PERMISSION
    }

    private boolean isFileAnImage(File file) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        return imgExtensions.contains(ext);
    }

}
