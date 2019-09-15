package com.pixeldraw.dbrt.pixeldraw;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.*;
public class Listeners {
    static PopupWindow dragWin;
    static boolean hasSelected=false;
    public static View.OnClickListener getGraphToolOnClickListener(View v,int graph_id){
        return new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                MA_INSTANCE.graph_tools[graph_id]=!MA_INSTANCE.graph_tools[graph_id];
                if(!MA_INSTANCE.graph_tools[graph_id]){
                    resetListenersForGraphTools();
                    v.setBackgroundResource(R.drawable.shape_button_selected);
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
        @TargetApi(Build.VERSION_CODES.O)
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
        @TargetApi(Build.VERSION_CODES.O)
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
        @TargetApi(Build.VERSION_CODES.O)
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
        @TargetApi(Build.VERSION_CODES.O)
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
        @TargetApi(Build.VERSION_CODES.O)
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
    public static View.OnClickListener selectListener=new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View view) {
            if(!MA_INSTANCE.isEnable_select) {
                MA_INSTANCE.enable_move=false;
                view.setBackgroundResource(R.drawable.shape_button_selected);
                MA_INSTANCE.pic.setOnPixelTouchListener(new PixelPicView.OnPixelTouchListener() {
                    int[] pos;
                    int[] stop_pos;
                    int[] diff,diff_end;
                    float[] pos_mea;
                    @Override
                    public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
                        if(!hasSelected) {
                            MA_INSTANCE.pic.cleanDrawnLines();
                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                //if(dragWin!=null) dragWin.dismiss();
                                pos = new int[]{x, y};
                                pos_mea = new float[]{motionEvent.getRawX(), motionEvent.getRawY()};
                            } else {
                                MA_INSTANCE.pic.drawRectLine(pos[0], pos[1], x + 1, y + 1);
                            }
                            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                stop_pos = new int[]{x, y};
                                hasSelected=true;
                                //dragWin=showDragBottomWindow(R.layout.popupwin_select_edit,(int)pos_mea[0]-MA_INSTANCE.dip2px(45),(int)pos_mea[1]-MA_INSTANCE.dip2px(45));
                            }
                        }else{
                            if(x>=pos[0]&&x<=stop_pos[0]&&y>=pos[1]&&y<=stop_pos[1]) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    diff = new int[]{pos[0] - x, pos[1] - y};
                                    diff_end = new int[]{stop_pos[0] - x, stop_pos[1] - y};
                                }
                                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                                    pos[0] = diff[0] + x;
                                    pos[1] = diff[1] + y;
                                    stop_pos[0] = diff_end[0] + x;
                                    stop_pos[1] = diff_end[1] + y;
                                    MA_INSTANCE.pic.cleanDrawnLines();
                                    MA_INSTANCE.pic.drawRectLine(pos[0], pos[1], stop_pos[0] + 1, stop_pos[1] + 1);
                                }
                            }
                        }
                        super.onTouch(view, motionEvent, x, y);
                    }
                });
            }else{
                hasSelected=false;
                MA_INSTANCE.enable_move=true;
                MA_INSTANCE.pic.setOnPixelTouchListener(null);
                MA_INSTANCE.pic.cleanDrawnLines();
                //dragWin.dismiss();
                view.setBackgroundResource(R.drawable.shape_button);
            }
            MA_INSTANCE.isEnable_select=!MA_INSTANCE.isEnable_select;
        }
    };
    private static PopupWindow showDragBottomWindow(int layout_id,int x,int y){
        LayoutInflater inflater = LayoutInflater.from(MA_INSTANCE);
        PopupWindow popupWindow=new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        View view=inflater.inflate(layout_id,null,true);
        popupWindow.setContentView(view);
        //popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.popupWindow);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(MA_INSTANCE.getWindow().getDecorView(), Gravity.TOP|Gravity.LEFT,x,y);
        return popupWindow;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public static void resetListenersForTools(){
        MA_INSTANCE.tools=new boolean[]{false,false,false,false,false};
        MA_INSTANCE.pic.setOnPixelTouchListener(null);
        MA_INSTANCE.pic.setOnPixelClickListener(null);
        MA_INSTANCE.button_pen.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_drawpen.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_eraser.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_bucket.setBackgroundResource(R.drawable.shape_sel);
        MA_INSTANCE.button_colorpicker.setBackgroundResource(R.drawable.shape_sel);
    }
    @TargetApi(Build.VERSION_CODES.O)
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
