package com.pixeldraw.dbrt.pixeldraw;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.*;
public class Listeners {
    public static View.OnClickListener getGraphToolOnClickListener(View v,int graph_id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MA_INSTANCE.graph_tools[graph_id]=!MA_INSTANCE.graph_tools[graph_id];
                if(!MA_INSTANCE.graph_tools[graph_id]){
                    resetListenersForGraphTools();
                    v.setBackgroundResource(R.drawable.shape_button_selected);
                    MainActivity.enable_move=false;
                    switch (graph_id){
                        case 0:
                            MA_INSTANCE.pic.setOnPixelTouchListener(LineListener);
                            break;
                        case 1:
                            MA_INSTANCE.pic.setOnPixelTouchListener(SquareListener);
                            break;
                        case 2:
                            MA_INSTANCE.pic.setOnPixelTouchListener(SquareHolListener);
                            break;
                        case 3:
                            MA_INSTANCE.pic.setOnPixelTouchListener(CircleListener);
                            break;
                        case 4:
                            MA_INSTANCE.pic.setOnPixelTouchListener(CircleHolListener);
                            break;
                        default:
                            break;
                    }
                }else{
                    MainActivity.enable_move =true;
                    v.setBackgroundResource(R.drawable.shape_sel);
                    MA_INSTANCE.pic.setOnPixelTouchListener(null);
                    MA_INSTANCE.pic.setOnPixelClickListener(null);
                }
            }
        };
    }
    public static PixelPicView.OnPixelTouchListener LineListener=new PixelPicView.OnPixelTouchListener() {
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                MA_INSTANCE.pic.loadHistoryBitmap();
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                int s=x_0-x>0?1:-1;
                int s_=y_0-y>0?1:-1;
                float length = (float) Math.sqrt(Math.pow(x_0 - x==0?1:x_0 - x, 2) + Math.pow(y_0 - y==0?1:y_0 - y, 2));
                for (float th = 0; th <= length; th = th + 1f) {
                    MA_INSTANCE.pic.set(x_0 + (x - x_0) * th / length, y_0 + (y - y_0) * th / length,MainActivity.pen_color);
                }
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    public static PixelPicView.OnPixelTouchListener SquareListener=new PixelPicView.OnPixelTouchListener() {
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                MA_INSTANCE.pic.loadHistoryBitmap();
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                int s=x_0-x>0?1:-1;
                int s_=y_0-y>0?1:-1;
                for(int i=0;i<Math.abs(x_0-x)+1;i++)
                    for(int i1=0;i1<Math.abs(y_0-y)+1;i1++){
                        MA_INSTANCE.pic.set(x_0-s*i,y_0-s_*i1,MainActivity.pen_color);
                    }
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    public static PixelPicView.OnPixelTouchListener SquareHolListener=new PixelPicView.OnPixelTouchListener() {
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                MA_INSTANCE.pic.loadHistoryBitmap();
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                int s=x_0-x>0?1:-1;
                int s_=y_0-y>0?1:-1;
                for(int i=0;i<Math.abs(x_0-x);i++) {
                    MA_INSTANCE.pic.set(x_0 - i * s, y_0, MainActivity.pen_color);
                    MA_INSTANCE.pic.set(x_0 - i * s, y, MainActivity.pen_color);
                }
                for(int i=0;i<Math.abs(y_0-y);i++)
                    MA_INSTANCE.pic.set(x_0,y_0-i*s_,MainActivity.pen_color);
                for(int i=0;i<Math.abs(y_0-y)+1;i++)
                    MA_INSTANCE.pic.set(x,y_0-i*s_,MainActivity.pen_color);

            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    public static PixelPicView.OnPixelTouchListener CircleHolListener=new PixelPicView.OnPixelTouchListener() {
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                MA_INSTANCE.pic.loadHistoryBitmap();
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                for(float s=0;s<60;s+=0.1f)
                {
                    float sin= Math.round(Math.sin(s*6*Math.PI/180)*(MA_INSTANCE.getDistance(x_0,y_0,x,y))*100000)/100000;
                    float cos= Math.round(Math.cos(s*6*Math.PI/180)*(MA_INSTANCE.getDistance(x_0,y_0,x,y))*100000)/100000;
                    MA_INSTANCE.pic.set(x_0+sin,y_0+cos,MainActivity.pen_color);
                }
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    public static PixelPicView.OnPixelTouchListener CircleListener=new PixelPicView.OnPixelTouchListener() {
        private int x_0=0;
        private int y_0=0;
        private Bitmap last_bmp;
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                MA_INSTANCE.pic.loadHistoryBitmap();
                last_bmp=MA_INSTANCE.pic.getBitmap();
                x_0=x;
                y_0=y;
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                MA_INSTANCE.pic.updateBitmap(last_bmp);
                for(float s=0;s<60;s+=0.1f)
                {
                    for(float i=1;i<=MA_INSTANCE.getDistance(x_0,y_0,x,y);i+=0.1f) {
                        float sin = Math.round(Math.sin(s * 6 * Math.PI / 180) * (i) * 100000) / 100000;
                        float cos = Math.round(Math.cos(s * 6 * Math.PI / 180) * (i) * 100000) / 100000;
                        MA_INSTANCE.pic.set(x_0 + sin, y_0 + cos, MainActivity.pen_color);
                    }
                }
            }
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                x_0=0;
                y_0=0;
                last_bmp=null;
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
    public static void resetListenersForTools(){
        MA_INSTANCE.tools=new boolean[]{false,false,false,false};
        MA_INSTANCE.pic.setOnPixelTouchListener(null);
        MA_INSTANCE.pic.setOnPixelClickListener(null);
        MA_INSTANCE.button_pen.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_drawpen.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_bucket.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_colorpicker.setBackgroundResource(R.drawable.shape_sel);
    }
    public static void resetListenersForGraphTools(){
        MA_INSTANCE.graph_tools=new boolean[]{false,false,false,false,false};
        MA_INSTANCE.pic.setOnPixelTouchListener(null);
        MA_INSTANCE.pic.setOnPixelClickListener(null);
        MA_INSTANCE.b_line.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_square.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_square_hol.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_circle.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.b_circle_hol.setBackgroundResource(R.drawable.shape_sel);
    }
}
