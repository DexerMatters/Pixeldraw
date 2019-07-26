package com.pixeldraw.dbrt.pixeldraw;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
public class AboutActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }
    @Override
    public Resources getResources() {
        Resources res=super.getResources();
        if(AppGlobalData.DEFAULT_DPI!=AppGlobalData.DENSITY_DPI) {
            Configuration conf = res.getConfiguration();
            conf.densityDpi = AppGlobalData.DENSITY_DPI;
            res.updateConfiguration(conf, super.getResources().getDisplayMetrics());
        }
        if(AppGlobalData.DEFAULT_FONT_SIZE!=AppGlobalData.FONT_SIZE){
            Configuration conf=res.getConfiguration();
            conf.fontScale=AppGlobalData.FONT_SIZE;
            res.updateConfiguration(conf,super.getResources().getDisplayMetrics());
        }
        return res;
    }
}
