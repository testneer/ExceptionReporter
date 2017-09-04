package com.ex.org.exceptionstest.com.ex.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.IDNA;

import java.util.HashMap;
import java.util.Map;

/**
 * Collects all the possible information about the device/config/app
 *
 * Created by orenegauthier on 04/09/2017.
 */

public class InfoCollector {

    private final Context mContext;

    public InfoCollector (Context context){
        mContext = context;
    }

    public Map<String, String> getExtraInfo() {
        Map<String, String> infoContainer = new HashMap<>();
        getPackgeInfo(infoContainer);
        return infoContainer;
    }

    private void getPackgeInfo(Map<String, String> container) {

        PackageManager pm = mContext.getPackageManager();
        if(pm != null){
            try {
                PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
                container.put("APP_VERSION_NAME", String.valueOf(pi.versionName));
                container.put("APP_VERSION_CODE", String.valueOf(pi.versionCode));
            }
            catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
