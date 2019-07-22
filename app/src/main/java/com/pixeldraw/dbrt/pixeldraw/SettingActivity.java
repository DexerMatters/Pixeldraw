package com.pixeldraw.dbrt.pixeldraw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView l1=findViewById(R.id.setting_check);
        SettingListAdapter adapter = new SettingListAdapter(this,l1,R.layout.setting_check);
        l1.setAdapter(adapter);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0||i==1) {
                    TextView name = view.findViewById(R.id.name);
                    showProgressDialog(name.getText().toString());
                }
            }
        });
        findViewById(R.id.about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
    public void showProgressDialog(String name_){
        final String name=name_;
        SettingDialog dialog=new SettingDialog(this,R.style.settingDialog_style);
        dialog.setTitle(name);
        dialog.setContentView_(LayoutInflater.from(this).inflate(R.layout.setting_progress,null,true));
        SeekBar seekBar=dialog.view.findViewById(R.id.setting_seekBar);
        TextView value=dialog.view.findViewById(R.id.value);
        if(name=="控件缩放") {
            seekBar.setProgress((int) (((float) AppGlobalData.DENSITY_DPI / (float) AppGlobalData.DEFAULT_DPI - 0.5) * 4));
            value.setText((int) ((AppGlobalData.DENSITY_DPI / (float) AppGlobalData.DEFAULT_DPI - 0.5) * 100) + "%");
        }
        if(name=="文字大小"){
            seekBar.setProgress((int)(((float) AppGlobalData.FONT_SIZE/(float) AppGlobalData.DEFAULT_FONT_SIZE-0.5)*4));
            value.setText((int)((AppGlobalData.FONT_SIZE/(float) AppGlobalData.DEFAULT_FONT_SIZE-0.5)*100)+"%");
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar_, int i, boolean b) {
                value.setText(seekBar.getProgress()*25+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.setEnableButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name == "控件缩放") {
                    AppGlobalData.DENSITY_DPI = ((int) (((float) AppGlobalData.DEFAULT_DPI) * (((float) seekBar.getProgress()) / 4f + 0.5f)));
                }
                if(name=="文字大小"){
                    AppGlobalData.FONT_SIZE=(AppGlobalData.DEFAULT_FONT_SIZE) * (((float) seekBar.getProgress()) / 4f + 0.5f);
                }
                AppGlobalData.updateData();
                AppGlobalData.restartApplication(SettingActivity.this);

                dialog.dismiss();
            }
        });
        dialog.show();
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
