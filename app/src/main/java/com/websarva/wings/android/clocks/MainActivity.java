package com.websarva.wings.android.clocks;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.websarva.wings.android.clocks.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_stopwatch, R.id.navigation_timer, R.id.navigation_alarms)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private boolean checkNightMode(){
        UiModeManager mUiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        boolean system_theme_Mode = false;
        if (mUiManager.getNightMode() == UiModeManager.MODE_NIGHT_YES){
            system_theme_Mode = true;
        }

        SharedPreferences preferences = getSharedPreferences("SettingsDB", MODE_PRIVATE);
        return preferences.getBoolean("dark_mode", system_theme_Mode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        SubClass subClass = new SubClass(this);

        if (itemID == R.id.settings){
            subClass.nextIntentSetting();
        }else if (itemID == R.id.aboutApp){
            subClass.nextIntentLicenses();
        }

        return super.onOptionsItemSelected(item);
    }
}