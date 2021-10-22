package dev.moutamid.mobilebrowser.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;

import dev.moutamid.mobilebrowser.R;
import dev.moutamid.mobilebrowser.databinding.ActivitySettingsBinding;
import dev.moutamid.mobilebrowser.utils.Constants;
import dev.moutamid.mobilebrowser.utils.Utils;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.zoomCB.setChecked(Utils.getBoolean(Constants.ZOOM_SETTINGS, true));
        b.cacheCB.setChecked(Utils.getBoolean(Constants.CACHE_SETTINGS, true));
        b.cookieCB.setChecked(Utils.getBoolean(Constants.COOKIE_SETTINGS, true));
        b.thirdPartyCacheCB.setChecked(Utils.getBoolean(Constants.THIRD_PARTY_COOKIE_SETTINGS, true));

        b.zoomCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.store(Constants.ZOOM_SETTINGS, b);
            }
        });

        b.cacheCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.store(Constants.CACHE_SETTINGS, b);
            }
        });

        b.cookieCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.store(Constants.COOKIE_SETTINGS, b);
            }
        });

        b.thirdPartyCacheCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.store(Constants.THIRD_PARTY_COOKIE_SETTINGS, b);
            }
        });

    }
}