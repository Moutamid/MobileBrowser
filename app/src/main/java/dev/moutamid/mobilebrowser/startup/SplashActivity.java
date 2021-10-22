package dev.moutamid.mobilebrowser.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import dev.moutamid.mobilebrowser.activities.MainActivity;
import dev.moutamid.mobilebrowser.utils.Constants;
import dev.moutamid.mobilebrowser.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (Utils.getBoolean(Constants.IS_LOGGED_IN)) {// USER IS LOGGED IN

            startActivity(new Intent(this, MainActivity.class));

        } else {// USER IS NOT LOGGED IN
            startActivity(new Intent(this, OnBoardActivity.class));

        }


    }
}
