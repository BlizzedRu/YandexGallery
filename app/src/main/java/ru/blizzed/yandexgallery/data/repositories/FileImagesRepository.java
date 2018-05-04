package ru.blizzed.yandexgallery.data.repositories;

import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImage;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolder;
import ru.blizzed.yandexgallery.data.model.fileimage.FileImagesFolderEvent;

import static android.os.FileObserver.CREATE;
import static android.os.FileObserver.DELETE;

public class FileImagesRepository {

    private static final List<String> IMG_EXTENSIONS = Arrays.asList("jpg", "png", "jpeg", "bmp", "gif", "ico");
    private static final List<String> FOLDERS_BLACK_LIST = Arrays.asList("/storage/emulated/0/Android");

    private Map<FileImagesFolder, FileObserver> folders;

    private Subject<FileImagesFolderEvent> foldersEventsSubject;

    public FileImagesRepository() {
        folders = new HashMap<>();
    }

    public Completable scan() {
        folders = new HashMap<>();
        return Completable.create(emitter ->
                getImageFolders()
                        .doOnNext(this::addFolderObserver)
                        .doOnComplete(emitter::onComplete)
                        .doOnError(emitter::onError)
                        .subscribe()
        );
    }

    public Flowable<FileImagesFolder> asyncScan() {
        folders = new HashMap<>();
        return getImageFolders()
                .doOnNext(this::addFolderObserver)
                .subscribeOn(Schedulers.io());
    }

    public List<FileImagesFolder> getFolders() {
        return new ArrayList<>(folders.keySet());
    }

    public void subscribe(Observer<FileImagesFolderEvent> observer) {
        if (foldersEventsSubject == null) foldersEventsSubject = PublishSubject.create();
        foldersEventsSubject.subscribe(observer);
    }

    private void addFolderObserver(FileImagesFolder folder) {
        FileObserver fileObserver = new FileObserver(folder.getFile().getAbsolutePath(), CREATE | DELETE) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                Disposable disposable = Observable.fromIterable(folder.getImagesList())
                        .filter(i -> i.getFile().getCanonicalFile().getName().equals(path))
                        .firstOrError()
                        .subscribe(cause -> {
                            FileImagesFolderEvent.Type type = null;
                            if (event == CREATE) {
                                type = FileImagesFolderEvent.Type.FILE_ADDED;
                                folder.addImage(cause);
                            } else if (event == DELETE) {
                                type = FileImagesFolderEvent.Type.FILE_DELETED;
                                folder.removeImage(cause);
                            }

                            if (type != null)
                                foldersEventsSubject.onNext(new FileImagesFolderEvent(folder, FileImagesFolderEvent.Type.FILE_ADDED, cause));
                        }, error -> {
                            Log.e("ru.blizzed.yandex", error.toString());
                        });
            }
        };
        fileObserver.startWatching();
        folders.put(folder, fileObserver);
    }

    private Flowable<FileImagesFolder> getImageFolders() {
        return Flowable.create(emitter -> {
            File root = getRootFile();
            if (root.listFiles() != null) {
                handleDirectory(root, emitter);
                emitter.onComplete();
            } else
                emitter.onError(new NoPermissionException("To use android file system you must have android.permission.READ_EXTERNAL_STORAGE"));

        }, BackpressureStrategy.DROP);
    }

    private void handleDirectory(File directory, Emitter<FileImagesFolder> emitter) {
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
        return Environment.getExternalStorageDirectory();

    }

    private boolean isFileAnImage(File file) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        return IMG_EXTENSIONS.contains(ext);
    }

}
