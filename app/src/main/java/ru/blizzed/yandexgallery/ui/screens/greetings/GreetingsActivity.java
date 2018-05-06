package ru.blizzed.yandexgallery.ui.screens.greetings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import ru.blizzed.yandexgallery.R;

public class GreetingsActivity extends AppIntro2 {

    public static final int REQUEST_CODE = 393;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage helloPage = new SliderPage();
        helloPage.setDescription(getString(R.string.greetings_hello_message));
        helloPage.setImageDrawable(R.drawable.greetings_hello);
        helloPage.setBgColor(getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(helloPage));

        SliderPage timePage = new SliderPage();
        timePage.setTitle(getString(R.string.greetings_time_title));
        timePage.setDescription(getString(R.string.greetings_time_message));
        timePage.setImageDrawable(R.drawable.greetings_time);
        timePage.setBgColor(getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(timePage));

        SliderPage permissionsPage = new SliderPage();
        permissionsPage.setTitle(getString(R.string.greetings_permissions_title));
        permissionsPage.setDescription(getString(R.string.greetings_permissions_message));
        permissionsPage.setImageDrawable(R.drawable.greetings_permissions);
        permissionsPage.setBgColor(getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(permissionsPage));

        SliderPage thanksPage = new SliderPage();
        thanksPage.setTitle(getString(R.string.greetings_thanks_title));
        thanksPage.setDescription(getString(R.string.greetings_thanks_message));
        thanksPage.setImageDrawable(R.drawable.greetings_thanks);
        thanksPage.setBgColor(getColor(R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(thanksPage));

        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startApp();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startApp();
    }

    private void startApp() {
        setResult(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? RESULT_OK : RESULT_CANCELED);
        finish();
    }

}
