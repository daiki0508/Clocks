package com.websarva.wings.android.clocks.ui.alarms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.websarva.wings.android.clocks.R;
import com.websarva.wings.android.clocks.SubClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddAlarmsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    private TextView timeTextView;

    private int hour = 0, minute = 0;

    private SubClass subClass;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarms);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        timeTextView = findViewById(R.id.time_text);
        timeTextView.setOnClickListener(this::showTimePickerDialog);

        TextView saveTextView = findViewById(R.id.save_text);
        saveTextView.setOnClickListener(this::execute_save);

        TextView cancelTextView = findViewById(R.id.cancel_text);
        cancelTextView.setOnClickListener(this::execute_cancel);

        subClass = new SubClass(this);
    }

   /* void backIntent(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home){
            //backIntent();
            subClass.backIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;

        String str = String.format(Locale.US, "%02d:%02d", hour, this.minute);
        timeTextView.setText(str);
    }

    private void showTimePickerDialog(View v){
        DialogFragment newFragment = new TimerPick();
        newFragment.show(getSupportFragmentManager(), "timerPicker");
    }

    private void execute_cancel(View view){
        //backIntent();
        subClass.backIntent();
    }

    private void execute_save(View view){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);

        SharedPreferences preferences = getSharedPreferences("AlarmsDB", MODE_PRIVATE);
        int requestCode = preferences.getInt("requestCode", 0);
        SharedPreferences.Editor editor_requestCode = preferences.edit();
        editor_requestCode.putInt("requestCode", requestCode + 1);
        editor_requestCode.apply();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);
        Log.d("test", String.valueOf(requestCode));

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am != null){
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

            Gson gson = new Gson();
            List<Map<String, Object>> alarms_list = gson.fromJson(preferences.getString("alarms_list", ""), new TypeToken<List>(){}.getType());
            if (alarms_list == null){
                alarms_list = new ArrayList<>();
            }
            Map<String, Object> alarms = new HashMap<>();
            if (hour < 10 && minute < 10){
                alarms.put("time", "0" + hour + ":" + "0" + minute);
            }else if (hour < 10){
                alarms.put("time", "0" + hour + ":" + minute);
            }else if (minute < 10){
                alarms.put("time",hour + ":" + "0" + minute);
            }else {
                alarms.put("time",hour + ":" + minute);
            }
            alarms.put("request_code", requestCode);

            alarms_list.add(alarms);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("alarms_list", gson.toJson(alarms_list));
            editor.apply();

            //backIntent();
            subClass.backIntent();
        }
    }
}