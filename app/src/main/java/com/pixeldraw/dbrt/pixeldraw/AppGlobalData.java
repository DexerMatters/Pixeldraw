package com.pixeldraw.dbrt.pixeldraw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

abstract class AppGlobalData {
    static Context MAIN_CONTEXT;
    static MainActivity MA_INSTANCE;
    static int DENSITY_DPI;
    static int DEFAULT_DPI;
    static float FONT_SIZE;
    static float DEFAULT_FONT_SIZE;
    static ArrayList<Bitmap> Plates = new ArrayList<>();
    static Bitmap colorful_bar;
    private static Bitmap color_plane = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
    static Bitmap alpha_plane = Bitmap.createBitmap(870, 300, Bitmap.Config.ARGB_8888);
    static Bitmap brightness_plane = Bitmap.createBitmap(870, 300, Bitmap.Config.ARGB_8888);
    private static File conf_file, bitmap_file;
    private static Drawable black;
    private static Drawable white;

    static void initializeData() {
        conf_file = new File(MAIN_CONTEXT.getFilesDir().getAbsolutePath() + "/config.cfg");
        bitmap_file = new File(MAIN_CONTEXT.getFilesDir().getAbsolutePath() + "/recent.png");
        ///
        MA_INSTANCE.getWindowManager().getDefaultDisplay().getMetrics(MA_INSTANCE.displayMetrics);
        ///
        DEFAULT_DPI = MA_INSTANCE.displayMetrics.densityDpi;
        DEFAULT_FONT_SIZE = 1;
        try {
            if (!bitmap_file.exists()) {
                bitmap_file.createNewFile();
                FileOutputStream os = new FileOutputStream(bitmap_file);
                Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888).compress(Bitmap.CompressFormat.PNG, 80, os);
                os.flush();
                os.close();
            }
            if (!conf_file.exists()) {
                DENSITY_DPI = DEFAULT_DPI;
                FONT_SIZE = DEFAULT_FONT_SIZE;
                conf_file.createNewFile();
            } else {
                if (conf_file.length() == 0) {
                    FileWriter writer = new FileWriter(conf_file);
                    writer.write(DENSITY_DPI + ",");
                    writer.write(FONT_SIZE + ",");
                    writer.write("0");
                    writer.close();
                    loadData();
                } else loadData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void loadData() throws IOException {
        conf_file = new File(MAIN_CONTEXT.getFilesDir().getPath() + "/config.cfg");
        FileReader reader = new FileReader(conf_file);
        int len;
        StringBuilder res = new StringBuilder();
        while ((len = reader.read()) != -1)
            res.append((char) len);
        reader.close();
        Log.d(TAG, res.toString());
        String[] strings = res.toString().split(",");
        DENSITY_DPI = Integer.parseInt(strings[0]);
        FONT_SIZE = Float.parseFloat(strings[1]);
    }

    static void updateData() {
        try {
            FileWriter writer = new FileWriter(conf_file);
            writer.write(DENSITY_DPI + ",");
            writer.write(FONT_SIZE + ",");
            writer.write("0");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static Bitmap initColorfulBar() {
        //color
        colorful_bar = Bitmap.createBitmap(300, 870, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < 870; i++)
            for (int j = 0; j < 300; j++) {
                float sum = (float) i / 870f;
                colorful_bar.setPixel(j, i, Color.HSVToColor(255, new float[]{sum * 360, 1, 1}));
            }
        ;
        black = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.TRANSPARENT, Color.BLACK});
        white = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.TRANSPARENT, Color.WHITE});
        return colorful_bar;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static Bitmap makeColorPlane(int color) {
        Drawable d = new ColorDrawable(color);
        LayerDrawable l = new LayerDrawable(new Drawable[]{d, black, white});

        Canvas canvas = new Canvas(color_plane);
        l.setBounds(0, 0, 800, 800);
        l.draw(canvas);
        return color_plane;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static void makeAlphaPlane(int color) {
        int turned_color = Color.argb(0, Color.red(color), Color.green(color), Color.blue(color));
        GradientDrawable ll = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{color, turned_color});
        LayerDrawable l = new LayerDrawable(new Drawable[]{MA_INSTANCE.getResources().getDrawable(R.drawable.trans, null), ll});
        Canvas canvas = new Canvas(alpha_plane);
        l.setBounds(0, 0, 900, 300);
        l.draw(canvas);
    }

    static void makeBrightnessPlane(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 1f;
        GradientDrawable l = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.BLACK, Color.HSVToColor(Color.alpha(color), hsv), Color.WHITE});
        Canvas canvas = new Canvas(brightness_plane);
        l.setBounds(0, 0, 900, 300);
        l.draw(canvas);
    }

    static void cancelApplication() {
        Process.killProcess(Process.myPid());
    }

    static void restartApplication(Activity instance) {
        final Intent intent = instance.getPackageManager().getLaunchIntentForPackage(MA_INSTANCE.getPackageName());
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        instance.startActivity(intent);
    }
}
