package dev.moutamid.mobilebrowser.startup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import dev.moutamid.mobilebrowser.R;
import dev.moutamid.mobilebrowser.activities.MainActivity;
import dev.moutamid.mobilebrowser.databinding.ActivityOnBoardBinding;
import dev.moutamid.mobilebrowser.utils.Constants;
import dev.moutamid.mobilebrowser.utils.Utils;

public class OnBoardActivity extends AppCompatActivity {
    private ActivityOnBoardBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityOnBoardBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.store(Constants.IS_LOGGED_IN, true);
                finish();
                startActivity(new Intent(OnBoardActivity.this, MainActivity.class));
            }
        });
    }
}