package com.pixeldraw.dbrt.pixeldraw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.telecom.CallScreeningService;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class ColorListAdapter extends BaseAdapter {
    private ArrayList<Integer> color;
    private Context mContext;

    public ColorListAdapter(Context mContext,ArrayList<Integer> color){
        this.color=color;
        this.mContext=mContext;
    }

    public void addColor(int color){
        int w=mContext.getResources().getDisplayMetrics().heightPixels/dip2px(30)-4;
        if(this.color.size()+1>w) this.color.remove(this.color.size()-1);
        this.color.add(0,color);
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Color getColor(int index){
        return Color.valueOf(color.get(index));
    }
    @Override
    public int getCount() {
        return color.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView=new ImageView(mContext);
        ScaleDrawable scaleDrawable=new ScaleDrawable(new LayerDrawable(new Drawable[]{mContext.getResources().getDrawable(R.drawable.trans_2),new ColorDrawable(color.get(i))}),Gravity.CENTER,dip2px(25),dip2px(25));
        scaleDrawable.setLevel(9960);
        LayerDrawable layerDrawable=new LayerDrawable(new Drawable[]{mContext.getResources().getDrawable(R.drawable.shape_button),scaleDrawable});
        imageView.setBackground(layerDrawable);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(dip2px(30),dip2px(30)));
        imageView.setFocusable(false);
        imageView.setFocusableInTouchMode(false);
        return imageView;
    }
    private int dip2px(float dpValue) {
        Context context=mContext;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
