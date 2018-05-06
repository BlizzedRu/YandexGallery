package ru.blizzed.yandexgallery.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class GlideUtils {

    public static File getFileFromImageURL(Context context, String imageURL) throws ExecutionException, InterruptedException {
        return Glide
                .with(context)
                .downloadOnly()
                .load(imageURL)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    }

}
