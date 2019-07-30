package com.pixeldraw.dbrt.pixeldraw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

import static android.content.ContentValues.TAG;


public class FileActivity extends Activity {
    public PixelPicView pic;
    private String pathStr=Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        Button button=(Button) findViewById(R.id.button_backtofore);
        final ListView listView=(ListView) findViewById(R.id.list);
        final TextView path=(TextView) findViewById(R.id.path);
        path.setText(Environment.getExternalStorageDirectory().getAbsolutePath());
        listView.setAdapter(getAdapterOfFiles(Environment.getExternalStorageDirectory().getAbsolutePath()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name=(TextView)view.findViewById(R.id.name);
                Log.d(TAG,"Path:"+pathStr);
                if(getAdapterOfFiles(pathStr+"/"+name.getText())!=null) {
                    pathStr+="/"+name.getText();
                    Log.d(TAG,"DirPath:"+pathStr);
                    path.setText(pathStr);
                    listView.setAdapter(getAdapterOfFiles(pathStr));
                }
                else {
                    if (!new File(pathStr+"/"+name.getText()).isDirectory()) {
                        Log.d(TAG,"UnDirPath:"+pathStr+"/"+name.getText());
                        Intent intent=new Intent(FileActivity.this,MainActivity.class);
                        MainActivity.pathStr=path.getText().toString()+"/"+name.getText();
                        AppGlobalData.MA_INSTANCE.onRead();
                        FileActivity.this.finish();
                    }
                    else
                        Toast.makeText(FileActivity.this, "此文件夹为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getAdapterOfFiles(pathStr.substring(0, pathStr.lastIndexOf("/")))!=null) {
                    listView.setAdapter(getAdapterOfFiles(pathStr.substring(0, pathStr.lastIndexOf("/"))));
                    path.setText(pathStr.substring(0, pathStr.lastIndexOf("/")));
                    pathStr = pathStr.substring(0, pathStr.lastIndexOf("/"));
                    Log.d(TAG, "DirPath:" + pathStr);
                }
            }
        });
    }
    private SimpleAdapter getAdapterOfFiles(String filePath){
        File file=new File(filePath);
        File[] files=file.listFiles();
        List<HashMap<String,Object>> maps=new ArrayList<>();
        if(files==null||files.length==0) return null;
        for(File i : files){
            HashMap<String,Object> map=new HashMap<>();
            if(i.getName().substring(i.getName().lastIndexOf(".")+1).equals("png")&&!i.isDirectory()) {
                map.put("name", i.getName());
                map.put("icon", R.drawable.file);
            }
            else if(i.isDirectory()&&(i.listFiles()!=null||i.listFiles().length!=0)){
                map.put("name", i.getName());
                map.put("icon", R.drawable.archive_director);
            }
            else continue;
            maps.add(map);
        }
        return new SimpleAdapter(FileActivity.this,maps,R.layout.list_theme,new String[]{"name","icon"},new int[]{R.id.name,R.id.icon});
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
