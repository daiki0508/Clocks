package com.websarva.wings.android.clocks;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Debug;

import java.io.File;
import java.util.List;

public class SecurityCheckClass {
    private final ActivityManager manager;

    SecurityCheckClass(Activity activity){
        manager = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);
    }

    boolean root_Check(){
        boolean flag = false;

        String[] su = new String[] {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};

        for (String s : su) {
            if (new File((s) + "su").exists()) {
                flag = true;
            }
        }
        return flag;
    }

    // SuperSUの検出
    boolean checkRunningProcesses() {

        boolean returnValue = false;

        // Get currently running application processes
        List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(300);

        if(list != null){
            String tempName;
            for(int i=0;i<list.size();++i){
                tempName = list.get(i).process;

                if(tempName.contains("supersu") || tempName.contains("superuser")){
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    // AndroidManifestでデバッグが許可されているかどうかをチェック
    boolean isDebuggable(Context context){

        return ((context.getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);

    }

    // デバッグに接続されているかどうかのチェック
    boolean detectDebugger() {
        return Debug.isDebuggerConnected();
    }
}
