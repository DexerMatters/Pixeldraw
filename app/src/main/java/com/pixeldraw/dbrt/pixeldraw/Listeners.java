package com.pixeldraw.dbrt.pixeldraw;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.*;
public class Listeners {
    public static View.OnClickListener getGraphToolOnClickListener(View v,int graph_id){
        return new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                MA_INSTANCE.graph_tools[graph_id]=!MA_INSTANCE.graph_tools[graph_id];
                if(!MA_INSTANCE.graph_tools[graph_id]){
                    resetListenersForGraphTools();
                    v.setBackgroundResource(R.drawable.shape_button_selected);
                    MainActivity.enable_move=false;
                    switch (graph_id){
                        case 0:
                            break;
                        case 1:
                            MA_INSTANCE.pic.setOnPixelTouchListener(SquareListener);
                            break;
                        case 2:
                            MA_INSTANCE.pic.setOnPixelTouchListener(SquareHolListener);
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        default:
                            break;
                    }
                }else{
                    MainActivity.enable_move =true;
                    v.setBackgroundResource(R.drawable.shape_sel);
                }
            }
        };
    }
    public static PixelPicView.OnPixelTouchListener SquareListener=new PixelPicView.OnPixelTouchListener() {
        private int point=0;
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
                point=1;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                for(int i=x_0;i<x+1;i++)
                    for(int i1=y_0;i1<y+1;i1++){
                        MA_INSTANCE.pic.set(i,i1,MainActivity.pen_color);
                    }
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                point=0;
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    public static PixelPicView.OnPixelTouchListener SquareHolListener=new PixelPicView.OnPixelTouchListener() {
        private int point=0;
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @TargetApi(Build.VERSION_CODES.O)
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
                point=1;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                for(int i=x_0;i<x;i++)
                    MA_INSTANCE.pic.set(i,y_0,MainActivity.pen_color);
                for(int i=x_0;i<x;i++)
                    MA_INSTANCE.pic.set(i,y,MainActivity.pen_color);
                for(int i=y_0;i<y;i++)
                    MA_INSTANCE.pic.set(x_0,i,MainActivity.pen_color);
                for(int i=y_0;i<y+1;i++)
                    MA_INSTANCE.pic.set(x,i,MainActivity.pen_color);

            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                point=0;
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    @TargetApi(Build.VERSION_CODES.O)
    public static void resetListenersForTools(){
        MA_INSTANCE.tools=new boolean[]{false,false,false,false};
        MA_INSTANCE.pic.setOnPixelTouchListener(null);
        MA_INSTANCE.pic.setOnPixelClickListener(null);
        MA_INSTANCE.button_pen.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_drawpen.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_bucket.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_colorpicker.setBackgroundResource(R.drawable.shape_sel);
    }
    @TargetApi(Build.VERSION_CODES.O)
    public static void resetListenersForGraphTools(){
        MA_INSTANCE.graph_tools=new boolean[]{false,false,false,false};
        MA_INSTANCE.pic.setOnPixelTouchListener(null);
        MA_INSTANCE.pic.setOnPixelClickListener(null);
        MA_INSTANCE.b_line.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_square.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_square_hol.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_circle.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_circle_hol.setBackgroundResource(R.drawable.shape_sel);
    }
}
