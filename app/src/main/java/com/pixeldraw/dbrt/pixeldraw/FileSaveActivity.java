package com.pixeldraw.dbrt.pixeldraw;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FileSaveActivity extends Activity {
    public PixelPicView pic;
    private String pathStr=Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_save);
        EditText editText=(EditText) findViewById(R.id.editText);
        editText.setText(new File(MainActivity.pathStr).isDirectory()?"Unnamed.png":new File(MainActivity.pathStr).getName());
        Button button=(Button)findViewById(R.id.button_backtofore);
        final ListView listView=(ListView) findViewById(R.id.list);
        final TextView path=(TextView) findViewById(R.id.path);
        path.setText(Environment.getExternalStorageDirectory().getAbsolutePath());
        listView.setAdapter(getAdapterOfFiles(Environment.getExternalStorageDirectory().getAbsolutePath()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name=(TextView) view.findViewById(R.id.name);
                    pathStr+="/"+name.getText();
                    path.setText(pathStr);
                    listView.setAdapter(getAdapterOfFiles(pathStr));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getAdapterOfFiles(pathStr.substring(0, pathStr.lastIndexOf("/")))!=null) {
                    listView.setAdapter(getAdapterOfFiles(pathStr.substring(0, pathStr.lastIndexOf("/"))));
                    path.setText(pathStr.substring(0, pathStr.lastIndexOf("/")));
                    pathStr = pathStr.substring(0, pathStr.lastIndexOf("/"));
                }
            }
        });
        Button button_checkSave=(Button) findViewById(R.id.button_checksave);
        button_checkSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.pathStr=pathStr+"/"+editText.getText();
                AppGlobalData.MA_INSTANCE.saveImage();
                Toast.makeText(FileSaveActivity.this, "成功另存为"+MainActivity.pathStr, Toast.LENGTH_SHORT).show();
                FileSaveActivity.this.finish();
            }
        });
    }
    private SimpleAdapter getAdapterOfFiles(String filePath){
        File file=new File(filePath);
        File[] files=file.listFiles();
        List<HashMap<String,Object>> maps=new ArrayList<>();
        for(File i : files){
            HashMap<String,Object> map=new HashMap<>();
            if(!i.isDirectory()) continue;
            else if(i.isDirectory()&&(i.listFiles()!=null||i.listFiles().length!=0)){
                map.put("name", i.getName());
                map.put("icon", R.drawable.archive_director);
            }
            else continue;
            maps.add(map);
        }
        return new SimpleAdapter(FileSaveActivity.this,maps,R.layout.list_theme,new String[]{"name","icon"},new int[]{R.id.name,R.id.icon});
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
