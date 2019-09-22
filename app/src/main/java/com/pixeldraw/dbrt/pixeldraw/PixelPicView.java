package com.pixeldraw.dbrt.pixeldraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.MA_INSTANCE;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.Plates;

import static android.content.ContentValues.TAG;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.Saved_Plates;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.copied_pic;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PixelPicView extends View {
    private Canvas canvas=new Canvas();
    private int widthPixels=16,heightPixels=16,webWidth=18;
    private int[][] Plate=new int[][]{{0}};
    private boolean[][] selected=new boolean[16][16];
    private boolean isWeb=false,isSelected=false;
    private int[] draw_pos=new int[2]
            ,stop_pos=new int[2];
    private Drawable background;
    private Bitmap bitmap;
    private FrameLayout.LayoutParams l;
    public int mod=0;
    public abstract static class OnPixelClickListener{
        public OnPixelClickListener(){ }
        public void onClick(View view,int x,int y) { }
    }
    public abstract static class OnPixelTouchListener{
        public OnPixelTouchListener(){ }
        public void onTouch(View view,MotionEvent motionEvent,int x,int y) { }
    }
    protected void updateSize(){
        Plate=new int[widthPixels][heightPixels];
        selected=new boolean[widthPixels][heightPixels];
        for (int i = 0; i < Plate.length; i++)
            for (int i1 = 0; i1 < Plate[i].length; i1++)
                Plate[i][i1]= Color.TRANSPARENT;
        l= (FrameLayout.LayoutParams) getLayoutParams();
        l.width=MA_INSTANCE.dip2px(300);
        l.height= ((int) (MA_INSTANCE.dip2px(300) * ((float) heightPixels / (float) widthPixels)));
        l.gravity=Gravity.CENTER;
        setX(MA_INSTANCE.displayMetrics.widthPixels/2-getMeasuredWidth()/2);
        setY(MA_INSTANCE.displayMetrics.heightPixels/2-getMeasuredHeight()/2);
        setScaleX(1);
        setScaleY(1);
        setLayoutParams(l);
        updateCanvas();
    }

    protected void updateCanvas() {
        invalidate();
    }
    public PixelPicView(Context ctx){
        super(ctx);
    }
    public PixelPicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public PixelPicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mpaint2 = new Paint();
        mpaint2.setAntiAlias(false);
        mpaint2.setColor(Color.argb(0.5f,1f,1f,1f));
        for (int i = 0; i < Plate.length; i++)
            for (int i1 = 0; i1 < Plate[i].length; i1++) {
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(false);
                mpaint.setColor(Plate[i][i1]);
                canvas.drawRect(this.getMeasuredWidth() / Plate.length * i, this.getMeasuredHeight() / Plate[0].length * i1, this.getMeasuredWidth() / Plate.length * (i + 1), this.getMeasuredHeight() / Plate[0].length * (i1 + 1), mpaint);
            if(isSelected){
                Paint paint= new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.argb(0.5f,1f,1f,1f));
                paint.setStrokeWidth(8f);
                paint.setPathEffect(new DashPathEffect(new float[]{18,8},0));
                if(selected[i][i1]==true)
                    canvas.drawRect(this.getMeasuredWidth() / Plate.length * i, this.getMeasuredHeight() / Plate[0].length * i1, this.getMeasuredWidth() / Plate.length * (i + 1), this.getMeasuredHeight() / Plate[0].length * (i1 + 1), paint);
                }
        }
        if(isWeb) {
            for (int i = 1; i < Plate.length; i++)
                canvas.drawRect(this.getMeasuredWidth() / Plate.length * i, 0f, this.getMeasuredWidth() / Plate.length * i + (getMeasuredWidth() / getWidthPixels() * 0.05f), this.getMeasuredHeight(), mpaint2);
            for (int i = 1; i < Plate[0].length; i++)
                canvas.drawRect(0f,this.getMeasuredHeight() / Plate[0].length * i - (getMeasuredHeight() / getHeightPixels() * 0.05f),getMeasuredWidth(),this.getMeasuredHeight() / Plate[0].length * i, mpaint2);

        }
        Paint paint=new Paint();
        paint.setColor(Color.argb(0.5f,1f,1f,1f));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        canvas.drawRect(0,0,this.getMeasuredWidth(),this.getMeasuredHeight(),paint);
        super.onDraw(canvas);
    }
    public void enableWeb(boolean b){
        if(isWeb!=b) {
            isWeb = b;
            Log.d("c",""+b);
            updateCanvas();
        }
    }
    public void setWebWidth(int webWidth){
        this.webWidth=webWidth;
        updateCanvas();
    }
    public void setWidthPixels(int count){
        widthPixels=count;
        updateSize();
    }
    public void setHeightPixels(int count){
        heightPixels=count;
        updateSize();
    }
    public int getWidthPixels(){
        return widthPixels;
    }
    public int getHeightPixels(){
        return heightPixels;
    }

    public void set(int x, int y, int value){
        if(x>=0&&x<widthPixels&&y>=0&&y<heightPixels)
            if(Plate[x][y]!=value)
                Plate[x][y]=value;
        invalidate();
    }
    public void set(float x, float y, int value){
        if(x>=0&&x<widthPixels&&y>=0&&y<heightPixels)
            if(Plate[(int)x][(int)y]!=value)
                Plate[(int)x][(int)y]=value;
        invalidate();
    }
    public void drawBitmapAt(int x,int y,Bitmap bitmap){
        for(int i=x;i<x+bitmap.getWidth();i++)
            for(int ii=y;ii<y+bitmap.getHeight();ii++){
                if(i>=0&&i<getWidthPixels())
                    if(ii>=0&&ii<getHeightPixels())
                        if(bitmap.getPixel(i-x, ii-y)==Color.TRANSPARENT) {
                            set(i, ii, bitmap.getPixel(i - x, ii - y));
                        }

        }
    }
    public void selectPixel(int x,int y){
        if(x>=0&&x<widthPixels&&y>=0&&y<heightPixels)
            selected[x][y]=true;
    }
    public void renderSelectedPixels(){
        isSelected=true;
        updateCanvas();
    }
    public void cleanSelectedPixels(){
        isSelected=false;
        selected=new boolean[widthPixels][heightPixels];
        updateCanvas();
    }
    public void selectRectPixel(int x,int y,int x_end,int y_end){
        for(int i=x;i<x_end;i++)
            for(int ii=y;ii<y_end;ii++)
                selectPixel(i,ii);
    }
    public boolean hasSelected(int x,int y){
        return selected[x][y];
    }
    public int get(int x,int y){
        if(x<0||y<0||x>widthPixels||y>heightPixels)
            return 0;
        else
            return Plate[x][y];
    }
    public Bitmap[] getHistoryBitmap(){
        Bitmap[] bitmaps=new Bitmap[Plates.size()];
        return Plates.toArray(bitmaps);
    }
    public void loadHistoryBitmap(){
        Plates.add(getBitmap());
        if(MA_INSTANCE.undo_button.getVisibility()==VISIBLE){
            MA_INSTANCE.undo_button.setVisibility(INVISIBLE);
        }
        AppGlobalData.Saved_Plates=new ArrayList<>();
        MA_INSTANCE.return_button.setVisibility(View.VISIBLE);
        if(Saved_Plates.size()!=0){
            Saved_Plates=new ArrayList<>();
        }
    }
    public Bitmap sliceAsBitmap(int x,int y,int end_x,int end_y){
        Bitmap bitmap=Bitmap.createBitmap(Math.abs(x-end_x),Math.abs(y-end_y),Bitmap.Config.ARGB_8888);
        for (int ii = 0; ii < Math.abs(x-end_x); ii++)
            for (int ii1 = 0; ii1 < Math.abs(y-end_y); ii1++) {
                bitmap.setPixel(ii, ii1, get(x+ii, y+ii1));
            }
        return bitmap;
    }
    public Bitmap getBitmap(){
        Bitmap bitmap=Bitmap.createBitmap(widthPixels,heightPixels,Bitmap.Config.ARGB_8888);
        for (int ii = 0; ii < bitmap.getWidth(); ii++)
            for (int ii1 = 0; ii1 < bitmap.getHeight(); ii1++) {
                bitmap.setPixel(ii, ii1, get(ii, ii1));
            }
        return bitmap;
    }
    public void setInitCloth(int color_A,int color_B){
        for (int i = 0; i < Plate.length; i++)
            for (int i1 = 0; i1 < Plate[i].length; i1++)
                if(i%2==0)
                    if(i1%2==0)
                        set(i,i1,color_A);
                    else
                        set(i,i1,color_B);
                else
                    if(i1%2==0)
                        set(i,i1,color_B);
                    else
                        set(i,i1,color_A);
    }
    public void setInitBitmap(Bitmap bitmap){
        isWeb=false;
        this.bitmap=bitmap;
        setWidthPixels(bitmap.getWidth());
        setHeightPixels(bitmap.getHeight());
        Log.d("create",bitmap.getWidth()+","+Plate[1].length);
        updateSize();
        for (int i = 0; i < bitmap.getWidth(); i++)
            for (int i1 = 0; i1 < bitmap.getHeight(); i1++) {
            set(i, i1, bitmap.getPixel(i, i1));
        }
    }
    public void updateBitmap(Bitmap bitmap){
        for (int i = 0; i < bitmap.getWidth(); i++)
            for (int i1 = 0; i1 < bitmap.getHeight(); i1++) {
                set(i, i1, bitmap.getPixel(i, i1));
            }
    }
    public void setOnPixelClickListener(final OnPixelClickListener pixelClickListener){
        if(pixelClickListener!=null) {
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    PixelPicView instance = PixelPicView.this;
                    motionEvent.recycle();
                    if (motionEvent.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                        for (int i = 0; i < Plate.length; i++)
                            for (int i1 = 0; i1 < Plate[i].length; i1++) {
                                if (motionEvent.getX() > i * (getMeasuredWidth()/getWidthPixels())
                                        && motionEvent.getX() < (i + 1) * (getMeasuredWidth()/getWidthPixels())
                                        && motionEvent.getY() > i1 * (getMeasuredHeight()/getHeightPixels())
                                        && motionEvent.getY() < (i1 + 1) * (getMeasuredHeight()/getWidthPixels())) {
                                    pixelClickListener.onClick(instance, i, i1);
                                }
                            }
                    }
                    return true;
                }
            });
        }else setOnTouchListener(null);
    }
    public void setOnPixelTouchListener(@Nullable final OnPixelTouchListener pixelTouchListener) {
        if (pixelTouchListener != null) {
            setOnTouchListener(new OnTouchListener() {
                long down;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    PixelPicView instance = PixelPicView.this;
                    for (int i = 0; i < Plate.length; i++)
                        for (int i1 = 0; i1 < Plate[i].length; i1++) {
                            if (motionEvent.getX() > i * (getMeasuredWidth()/getWidthPixels())
                                    && motionEvent.getX() < (i + 1) * (getMeasuredWidth()/getWidthPixels())
                                    && motionEvent.getY() > i1 * (getMeasuredHeight()/getHeightPixels())
                                    && motionEvent.getY() < (i1 + 1) * (getMeasuredHeight()/getHeightPixels())) {
                                pixelTouchListener.onTouch(instance, motionEvent, i, i1);
                                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                                    down=new Date().getTime();
                                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                                    if(new Date().getTime()-down>=400&&copied_pic!=null){
                                        Toast.makeText(getContext(), "已粘贴", Toast.LENGTH_SHORT).show();
                                        drawBitmapAt(i,i1,copied_pic);
                                    }
                            }
                        }
                    return true;
                }
            });
        }else setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setOnTouchListener(new OnTouchListener() {
                    long down;
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        for (int i = 0; i < Plate.length; i++)
                            for (int i1 = 0; i1 < Plate[i].length; i1++) {
                                if (motionEvent.getX() > i * (getMeasuredWidth()/getWidthPixels())
                                        && motionEvent.getX() < (i + 1) * (getMeasuredWidth()/getWidthPixels())
                                        && motionEvent.getY() > i1 * (getMeasuredHeight()/getHeightPixels())
                                        && motionEvent.getY() < (i1 + 1) * (getMeasuredHeight()/getHeightPixels())) {
                                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                                        down=new Date().getTime();
                                    if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                                        if(new Date().getTime()-down>=400&&copied_pic!=null){
                                            Toast.makeText(getContext(), "已粘贴", Toast.LENGTH_SHORT).show();
                                            drawBitmapAt(i,i1,copied_pic);
                                        }
                                }
                            }
                        return true;
                    }
                });
                return true;
            }
        });
    }
    private static float dip2px(float dp){
        return MA_INSTANCE.dip2px(dp);
    }

}
