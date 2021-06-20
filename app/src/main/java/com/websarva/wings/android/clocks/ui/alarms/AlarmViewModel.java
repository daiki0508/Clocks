package com.websarva.wings.android.clocks.ui.alarms;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.websarva.wings.android.clocks.R;

public class AlarmViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final String alarmText;

    AlarmViewModel(Context context){
        mText = new MutableLiveData<>();

        SharedPreferences preferences = context.getSharedPreferences("AlarmsDB", Context.MODE_PRIVATE);
        alarmText = preferences.getString("alarms_list", "");
        Log.d("test", alarmText);
        if (alarmText.isEmpty() || alarmText.equals("[]")){
            mText.setValue(context.getString(R.string.no_time_data));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("requestCode", 0);
            editor.apply();
            Log.d("test", "empty");
        }else {
            mText.setValue("");
            Log.d("test", "no_empty");
        }
    }

    LiveData<String> getText(){
        return mText;
    }

    String getAlarmText(){
        return alarmText;
    }
}
