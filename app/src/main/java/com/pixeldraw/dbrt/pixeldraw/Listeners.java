package com.pixeldraw.dbrt.pixeldraw;

import android.view.MotionEvent;
import android.view.View;
import static com.pixeldraw.dbrt.pixeldraw.AppGlobalData.*;
public class Listeners {
    public static PixelPicView.OnPixelTouchListener SquareListener=new PixelPicView.OnPixelTouchListener() {
        private int point=0;
        private int x_0=0;
        private int y_0=0;
        @Override
        public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
            if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                if(point==0){
                    x_0=x;
                    y_0=y;
                    point=1;
                    return;
                }
                if(point==1){
                    point=0;
                    return;
                }
            }
            super.onTouch(view, motionEvent, x, y);
        }
    };
}
