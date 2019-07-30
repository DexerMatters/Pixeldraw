package com.pixeldraw.dbrt.pixeldraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.graphics.Paint;
<<<<<<< HEAD
import android.graphics.Rect;
<<<<<<< HEAD
=======
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
>>>>>>> parent of a8db2bf... Drawing lines added.Borders added.Fix bugs of opening pic
=======
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
>>>>>>> parent of 54d16a5... Main:Android 5.0 adapted
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

import java.util.ArrayList;

import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.MA_INSTANCE;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.Plates;

import static android.content.ContentValues.TAG;
@RequiresApi(api = Build.VERSION_CODES.O)
public class PixelPicView extends View {
    private Canvas canvas=new Canvas();
    private int widthPixels=16,heightPixels=16,webWidth=18;
    private int[][] Plate=new int[][]{{0}};
    private boolean isWeb=false;
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
        for (int i = 0; i < Plate.length; i++)
            for (int i1 = 0; i1 < Plate[i].length; i1++)
                Plate[i][i1]= Color.TRANSPARENT;
        l= (FrameLayout.LayoutParams) getLayoutParams();
        l.width=MA_INSTANCE.dip2px(300);
        l.height=MA_INSTANCE.dip2px(300)*(heightPixels/widthPixels);
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
        for (int i = 0; i < Plate.length; i++)
            for (int i1 = 0; i1 < Plate[i].length; i1++) {
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(false);
                mpaint.setColor(Plate[i][i1]);

<<<<<<< HEAD
                canvas.drawRect(this.getMeasuredWidth() / Plate.length * i, this.getMeasuredHeight() / Plate[0].length * i1, this.getMeasuredWidth() / Plate.length * (i + 1), this.getMeasuredHeight() / Plate[0].length * (i1 + 1), mpaint);
            }
<<<<<<< HEAD
        if(isWeb) {
            for (int i = 1; i < Plate.length; i++)
                canvas.drawRect(this.getMeasuredWidth() / Plate.length * i, 0f, this.getMeasuredWidth() / Plate.length * i + (getMeasuredWidth() / getWidthPixels() * 0.05f), this.getMeasuredHeight(), mpaint2);
            for (int i = 1; i < Plate[0].length; i++)
                canvas.drawRect(0f,this.getMeasuredHeight() / Plate[0].length * i - (getMeasuredHeight() / getHeightPixels() * 0.05f),getMeasuredWidth(),this.getMeasuredHeight() / Plate[0].length * i, mpaint2);

        }
=======
                Paint mpaint2 = new Paint();
                mpaint2.setAntiAlias(false);
                mpaint2.setColor(Color.WHITE);
                canvas.drawRect(this.getWidth() / Plate.length * i, this.getHeight() / Plate[0].length * i1, this.getWidth() / Plate.length * (i + 1), this.getHeight() / Plate[0].length * (i1 + 1), mpaint);
                if(isWeb) {
                    canvas.drawRect(this.getWidth() / Plate.length * i, this.getHeight() / Plate[0].length * i1, this.getWidth() / Plate.length * i - this.getWidth() / Plate.length / webWidth, this.getHeight() / Plate[0].length * (i1 + 1), mpaint2);
                    canvas.drawRect(this.getWidth() / Plate.length * i, this.getHeight() / Plate[0].length * i1, this.getWidth() / Plate.length * (i + 1), this.getHeight() / Plate[0].length * i1 - this.getWidth() / Plate.length / webWidth, mpaint2);
                }
            }
>>>>>>> parent of 54d16a5... Main:Android 5.0 adapted
        Rect rect=canvas.getClipBounds();
        rect.bottom--;
        rect.right--;
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawRect(rect,paint);
=======
>>>>>>> parent of a8db2bf... Drawing lines added.Borders added.Fix bugs of opening pic
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
<<<<<<< HEAD
<<<<<<< HEAD
        if(x>=0&&x<widthPixels&&y>=0&&y<heightPixels)
            if(Plate[x][y]!=value)
                Plate[x][y]=value;
        invalidate();
    }
    public void set(float x, float y, int value){
        if(x>=0&&x<widthPixels&&y>=0&&y<heightPixels)
            if(Plate[(int)x][(int)y]!=value)
                Plate[(int)x][(int)y]=value;
=======
        /*if(Color.alpha(value)==1){
            Plate[x][y]=value;
        }else {
            Color color_0 = Color.valueOf(value);
            Color color_1 = Color.valueOf(Plate[x][y]);
            Plate[x][y]=Color.argb(color_0.alpha()+color_1.alpha(),color_0.red()+color_1.red(),color_0.green()+color_1.green(),color_0.blue()+color_1.blue());
        }*/
        Plate[x][y]=value;
>>>>>>> parent of a8db2bf... Drawing lines added.Borders added.Fix bugs of opening pic
=======
        if(Plate[x][y]!=value)
            Plate[x][y]=value;
        invalidate();
    }
    public void set(float x, float y, int value){
        if(Plate[(int)x][(int)y]!=value)
            Plate[(int)x][(int)y]=value;
>>>>>>> parent of 54d16a5... Main:Android 5.0 adapted
        invalidate();
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
        Log.d(TAG, "loadHistoryBitmap() returned: ");
    }
    public Bitmap getBitmap(){
        Bitmap bitmap=Bitmap.createBitmap(widthPixels,heightPixels,Bitmap.Config.ARGB_8888);
        Log.d("bit",bitmap.getWidth()+","+widthPixels+","+Plate.length);
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
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    PixelPicView instance = PixelPicView.this;
                    for (int i = 0; i < Plate.length; i++)
                        for (int i1 = 0; i1 < Plate[i].length; i1++) {
                            if (motionEvent.getX() > i * (getMeasuredWidth()/getWidthPixels())
                                    && motionEvent.getX() < (i + 1) * (getMeasuredWidth()/getWidthPixels())
                                    && motionEvent.getY() > i1 * (getMeasuredHeight()/getHeightPixels())
                                    && motionEvent.getY() < (i1 + 1) * (getMeasuredHeight()/getWidthPixels())) {
                                pixelTouchListener.onTouch(instance, motionEvent, i, i1);
                            }
                        }
                    return true;
                }
            });
        }else setOnTouchListener(null);
    }
    private static float dip2px(float dp){
        return MA_INSTANCE.dip2px(dp);
    }
}
