package com.websarva.wings.android.clocks;

import android.app.Activity;
import android.content.Intent;

public class SubClass extends Activity {
    private final Activity mActivity;

   public SubClass(Activity activity){
        mActivity = activity;
    }

    public void backIntent(){
       mActivity.startActivity(new Intent(mActivity, MainActivity.class));
       mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
       mActivity.finish();
    }

    public void nextIntentSetting(){
       mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
    }

    public void nextIntentLicenses(){
       mActivity.startActivity(new Intent(mActivity, LicenseActivity.class));
    }
}
