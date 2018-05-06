package ru.blizzed.yandexgallery.ui.screens.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.blizzed.yandexgallery.App;
import ru.blizzed.yandexgallery.R;
import ru.blizzed.yandexgallery.ui.MainActivity;
import ru.blizzed.yandexgallery.ui.screens.greetings.GreetingsActivity;

public class SplashActivity extends Activity {

    private YoYo.YoYoString animation;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sp = getSharedPreferences("system", MODE_PRIVATE);
        if (!sp.contains("hasVisited")) {
            sp.edit().putBoolean("hasVisited", true).apply();
            onFirstVisit();
        } else onVisit();

    }

    private void onFirstVisit() {
        startActivityForResult(new Intent(getApplicationContext(), GreetingsActivity.class), GreetingsActivity.REQUEST_CODE);
    }

    private void onVisit() {
        animation = YoYo.with(Techniques.FadeOut)
                .duration(1500)
                .delay(1000)
                .playOn(findViewById(R.id.root));

        disposable = App.getRepositoriesComponent()
                .fileImagesRepository()
                .scan()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(this::startApp)
                .doOnError(error -> startApp())
                .subscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GreetingsActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onVisit();
            } else startApp();
        }
    }

    private void startApp() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animation != null)
            animation.stop();
        if (disposable != null)
            disposable.dispose();
    }
}
