package ru.blizzed.yandexgallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.R;

public class SplashActivity extends Activity {

    private YoYo.YoYoString animation;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animation = YoYo.with(Techniques.FadeOut)
                .duration(1500)
                .delay(1000)
                .playOn(findViewById(R.id.root));

        disposable = App.getRepositoriesComponent()
                .fileImagesRepository()
                .scan()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(this::run)
                .doOnError(error -> run())
                .subscribe();

    }

    private void run() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animation.stop();
        disposable.dispose();
    }
}
