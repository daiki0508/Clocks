package com.websarva.wings.android.clocks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch darkSwitch = findViewById(R.id.dark_switch);
        Configuration configuration = getResources().getConfiguration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode){
            case Configuration.UI_MODE_NIGHT_NO:
                darkSwitch.setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                darkSwitch.setChecked(true);
                break;
        }
        darkSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home){
            SubClass subClass = new SubClass(this);
            subClass.backIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        writeData(checked);
    }

    private void writeData(boolean flag){
        SharedPreferences preferences = getSharedPreferences("SettingsDB", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("dark_mode", flag);
        editor.apply();
    }
}