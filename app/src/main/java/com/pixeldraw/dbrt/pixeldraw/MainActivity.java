package com.pixeldraw.dbrt.pixeldraw;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.arch.core.util.Function;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.*;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.O)
public class MainActivity extends Activity {
    public boolean[] tools={false,false,false,false,false};
    public boolean[] graph_tools={false,false,false,false,false};
    private FrameLayout screen;
    private TextView colorviewer;
    private Drawable scroll_background;
    private ListView button_colorlist;
    private PixelPicView.OnPixelTouchListener onPixelClickListener,onPixelTouchListener,onBucketUseListener,onColorPickerUseListener,onEraserUseClickListener;
    private Bitmap bitmap;
    private float pixel_size_0;
    public ImageButton button_pen,button_drawpen,button_eraser,button_bucket,button_colorpicker,b_line,b_square,b_square_hol,b_circle,b_circle_hol;
    public DisplayMetrics displayMetrics=new DisplayMetrics();
    public ColorListAdapter colorListAdapter=new ColorListAdapter(this,new ArrayList<Integer>());
    public Color al_color=Color.valueOf(00000000);
    public int color_picked;
    public static MainActivity instance;
    public static String pathStr;
    public static int pen_color=0xFF000000;
    public static boolean enable_move=true;
    public PopupWindow mainWin,editWin,fileWin,colorWin,colorSelectorWin,returnWin,graphWin;
    public PixelPicView pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppGlobalData.MAIN_CONTEXT=MainActivity.this;
        AppGlobalData.MA_INSTANCE=MainActivity.this;

        AppGlobalData.initailizeData();
        AppGlobalData.initColorfulBar();

        pic=findViewById(R.id.pic);
        screen=findViewById(R.id.frameLayout);
        onPixelClickListener=new PixelPicView.OnPixelTouchListener() {
            @Override
            public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    pic.loadHistoryBitmap();
                    pic.set(x, y, pen_color);
                }
                super.onTouch(view, motionEvent, x, y);
            }
        };
        onPixelTouchListener=new PixelPicView.OnPixelTouchListener() {
            @Override
            public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    pic.loadHistoryBitmap();
                else
                    pic.set(x,y,pen_color);
                super.onTouch(view, motionEvent, x, y);
            }
        };
        onEraserUseClickListener=new PixelPicView.OnPixelTouchListener() {
            @Override
            public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    pic.loadHistoryBitmap();
                else
                    pic.set(x,y,Color.TRANSPARENT);
                super.onTouch(view, motionEvent, x, y);
            }
        };
        onBucketUseListener=new PixelPicView.OnPixelTouchListener() {
            private int orginal_color;
            public int loop(int x,int y){
                pic.set(x,y,pen_color);
                if(pic.get(x+1,y)==orginal_color)
                    loop(x+1,y);
                if(pic.get(x-1,y)==orginal_color)
                    loop(x-1,y);
                if(pic.get(x,y+1)==orginal_color)
                    loop(x,y+1);
                if(pic.get(x,y-1)==orginal_color)
                    loop(x,y-1);
                return 0;
            }
            @Override
            public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    pic.loadHistoryBitmap();
                    orginal_color=pic.get(x,y);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            loop(x, y);
                        }
                    }).start();
                }
                super.onTouch(view, motionEvent, x, y);
            }
        };
        onColorPickerUseListener=new PixelPicView.OnPixelTouchListener() {
            @Override
            public void onTouch(View view, MotionEvent motionEvent, int x, int y) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    colorListAdapter.addColor(pic.get(x,y));
                    button_colorlist.setAdapter(colorListAdapter);
                    pen_color=pic.get(x,y);
                }
                super.onTouch(view, motionEvent, x, y);
            }
        };
        screen.post(new Runnable() {
            @Override
            public void run() {
                pic.setHeightPixels(16);
                pic.setWidthPixels(16);
                pic.setInitCloth(Color.WHITE,MainActivity.this.getResources().getColor(R.color.init_pic));
                pic.updateCanvas();

                colorListAdapter.addColor(Color.BLACK);

                pixel_size_0=pic.getScaleX()*dip2px(300)/pic.getWidthPixels();
                {//version operation
                    String bytime="2019-09-01 12:59:59";
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = dateFormat.parse(bytime);
                        if(new Date().after(date)){
                            WarningDialog dialog=new WarningDialog(MainActivity.this,R.style.settingDialog_style);
                            dialog.setContentText("测试版本已过期，请等接下来的其他测试");
                            dialog.setTitle("警告");
                            dialog.setEnableButtonOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AppGlobalData.cancelApplication();
                                }
                            });
                            dialog.show();
                        }else {
                            WarningDialog dialog=new WarningDialog(MainActivity.this,R.style.settingDialog_style);
                            dialog.setContentText("此版本为内测版\n使用截止到"+bytime);
                            dialog.setTitle("警告");
                            dialog.show();
                        }
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }

                mainWin = showMainPopWindow(R.layout.popupwin);
                final View mainView = mainWin.getContentView();
                ImageButton button_A = mainView.findViewById(R.id.button2);
                ImageButton button_B = mainView.findViewById(R.id.button3);
                ImageButton button_settings=mainView.findViewById(R.id.button4);
                button_settings.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this,SettingActivity.class);
                        startActivity(intent);
                    }
                });
                button_A.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainWin.dismiss();
                        editWin = showMainPopWindow(R.layout.popupwin_edit);
                        colorWin = showSecPopWindow(R.layout.popupwin_color);
                        returnWin=showReturnWindow(R.layout.popupwin_return);
                        ImageButton return_button=returnWin.getContentView().findViewById(R.id.button_return);
                        return_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(AppGlobalData.Plates.size()!=0) {
                                    pic.setInitBitmap(AppGlobalData.Plates.get(AppGlobalData.Plates.size()-1));
                                    pic.updateCanvas();
                                    AppGlobalData.Plates.remove(AppGlobalData.Plates.size()-1);
                                }
                            }
                        });

                        View editView = editWin.getContentView();
                        View colorView = colorWin.getContentView();

                        ImageButton button_sel = colorView.findViewById(R.id.button_sel);
                        button_colorlist = colorView.findViewById(R.id.color_list);
                        ImageButton button_back = editView.findViewById(R.id.button_back);
                        ImageButton button_graph=editView.findViewById(R.id.button_graph);
                        button_pen = editView.findViewById(R.id.button3);
                        button_drawpen = editView.findViewById(R.id.button1);
                        button_bucket = editView.findViewById(R.id.button);
                        button_colorpicker = editView.findViewById(R.id.button2);
                        button_eraser=editView.findViewById(R.id.button5);
                        button_pen.setOnClickListener(getToolOnClickListener(button_pen, 0));
                        button_drawpen.setOnClickListener(getToolOnClickListener(button_drawpen, 1));
                        button_bucket.setOnClickListener(getToolOnClickListener(button_bucket,2));
                        button_colorpicker.setOnClickListener(getToolOnClickListener(button_colorpicker,3));
                        button_eraser.setOnClickListener(getToolOnClickListener(button_eraser,4));
                        button_colorlist.setAdapter(colorListAdapter);
                        button_colorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    al_color = colorListAdapter.getColor(i);
                                    pen_color = al_color.toArgb();
                                    colorListAdapter.addColor(al_color.toArgb());
                                    button_colorlist.setAdapter(colorListAdapter);
                                }
                            }
                        });

                        button_sel.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void onClick(View view) {
                                colorSelectorWin=showColorSelectorWin(MainActivity.this,new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        pen_color= ((ColorDrawable) ((LayerDrawable) colorviewer.getBackground()).getDrawable(1)).getColor();
                                        colorListAdapter.addColor(pen_color);
                                        button_colorlist.setAdapter(colorListAdapter);
                                        colorSelectorWin.dismiss();
                                    }
                                });
                            }
                        });
                        button_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainWin.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.START, 20, 20);
                                Listeners.resetListenersForTools();
                                enable_move=true;
                                editWin.dismiss();
                                colorWin.dismiss();
                                returnWin.dismiss();
                            }
                        });
                        button_graph.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                graphWin=showMainPopWindow(R.layout.popupwin_graph);
                                View graphView=graphWin.getContentView();
                                ImageButton b_back=graphView.findViewById(R.id.button_back);
                                b_line=graphView.findViewById(R.id.button_line);
                                b_square=graphView.findViewById(R.id.button_square);
                                b_square_hol=graphView.findViewById(R.id.button_square_hol);
                                b_circle=graphView.findViewById(R.id.button_circle);
                                b_circle_hol=graphView.findViewById(R.id.button_circle_hol);
                                b_line.setOnClickListener(Listeners.getGraphToolOnClickListener(b_line,0));
                                b_square.setOnClickListener(Listeners.getGraphToolOnClickListener(b_square,1));
                                b_square_hol.setOnClickListener(Listeners.getGraphToolOnClickListener(b_square_hol,2));
                                b_circle.setOnClickListener(Listeners.getGraphToolOnClickListener(b_circle,3));
                                b_circle_hol.setOnClickListener(Listeners.getGraphToolOnClickListener(b_circle_hol,4));
                                Listeners.resetListenersForGraphTools();
                                b_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Listeners.resetListenersForGraphTools();
                                        graphWin.dismiss();
                                        editWin.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.START, 20, 20);
                                        Listeners.resetListenersForTools();
                                    }
                                });
                                editWin.dismiss();
                            }
                        });
                    }
                });
                button_B.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fileWin=showMainPopWindow(R.layout.popupwin_file);
                        View con=fileWin.getContentView();
                        ImageButton button_back=con.findViewById(R.id.button_back),
                                button_open_file=con.findViewById(R.id.button_open_file),
                                button_save=con.findViewById(R.id.button_save),
                                button_save_as=con.findViewById(R.id.button_save_as),
                                button_new_file=con.findViewById(R.id.button_new_file);
                        button_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mainWin.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.START, 20, 20);
                                fileWin.dismiss();
                            }
                        });
                        button_new_file.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showNewFileDialog();
                            }
                        });

                        button_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(pathStr!=null) {
                                    saveImage();
                                    Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                        pathStr = Environment.getExternalStorageDirectory().getPath();
                                        startActivity(new Intent(MainActivity.this, FileSaveActivity.class));
                                    }
                            }
                        });
                        button_save_as.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(pathStr!=null) {
                                    startActivity(new Intent(MainActivity.this, FileSaveActivity.class));
                                }else pathStr=Environment.getExternalStorageDirectory().getPath();
                            }
                        });
                        button_open_file.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MainActivity.this,FileActivity.class));
                            }
                        });
                        mainWin.dismiss();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        try {
            pic.setInitBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/PixelDraw/recent.png"));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Resources getResources() {
        Resources res=super.getResources();
        if(AppGlobalData.DEFAULT_DPI!=AppGlobalData.DENSITY_DPI) {
            Configuration conf = res.getConfiguration();
            conf.densityDpi = AppGlobalData.DENSITY_DPI;
            res.updateConfiguration(conf, displayMetrics);
        }
        if(AppGlobalData.DEFAULT_FONT_SIZE!=AppGlobalData.FONT_SIZE){
            Configuration conf=res.getConfiguration();
            conf.fontScale=AppGlobalData.FONT_SIZE;
            res.updateConfiguration(conf,displayMetrics);
        }
        return res;
    }
    float[] startX= new float[2];
    float[] startY= new float[2];
    float[] orginal_pos=new float[2];
    float[] orginal_size=new float[2];
    float orginal_distance;
    boolean enable_web=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getPointerCount()==2&&enable_move) {
            if(event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN){
                startX[0] = event.getX(0);
                startY[0] = event.getY(0);
                startX[1] = event.getX(1);
                startY[1] = event.getY(1);
                orginal_size[0]=pic.getScaleX();
                orginal_size[1]=pic.getScaleY();
                orginal_pos[0]=pic.getX();
                orginal_pos[1]=pic.getY();
                orginal_distance=getDistance(startX[0],startY[0],startX[1],startY[1]);
            }

            if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                float distence=getDistance(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
                float pixel_size=pic.getScaleX()*dip2px(300)/pic.getWidthPixels();
                if (orginal_size[0] + (distence-orginal_distance) / 300 > 0) {
                    pic.setScaleX(orginal_size[0] + (distence-orginal_distance) / 300);
                    pic.setScaleY(orginal_size[1] + (distence-orginal_distance) / 300);
                }
                pic.setX(orginal_pos[0] + (event.getX(0) - startX[0] + event.getX(1) - startX[1]) / 2);
                pic.setY(orginal_pos[1] + (event.getY(0) - startY[0] + event.getY(1) - startY[1]) / 2);
                if(pixel_size>=22){
                    pic.enableWeb(true);
                }else{
                    pic.enableWeb(false);
                }

            }
            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                startX = new float[2];
                startY = new float[2];
                orginal_pos = new float[2];
                orginal_size = new float[2];
            }
        }
        return super.onTouchEvent(event);
    }
    public void onRead(){
        Log.d(TAG, "onRead: "+MainActivity.pathStr);
        Toast.makeText(MainActivity.this, "打开中", Toast.LENGTH_SHORT).show();
        if(new File(MainActivity.pathStr).exists()) {
            bitmap = BitmapFactory.decodeFile(MainActivity.pathStr);
            if(bitmap!=null)
                pic.setInitBitmap(bitmap);
            else Toast.makeText(MainActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(MainActivity.this, "打开失败", Toast.LENGTH_SHORT).show();

    }
    private PopupWindow showMainPopWindow(int layout_id){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        PopupWindow popupWindow=new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        View view=inflater.inflate(layout_id,null,true);
        popupWindow.setContentView(view);
        //popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.popupWindow);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.TOP|Gravity.START,20,20);
        return popupWindow;
    }
    private PopupWindow showSecPopWindow(int layout_id){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        PopupWindow popupWindow=new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        View view=inflater.inflate(layout_id,null,true);
        popupWindow.setContentView(view);
        //popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.popupWindow_sec);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.TOP|Gravity.END,20,20);
        return popupWindow;
    }
    private PopupWindow showSimplePopWindow(Context ctx,int layout_id){
        LayoutInflater inflater = LayoutInflater.from(ctx);
        PopupWindow popupWindow=new PopupWindow(dip2px(245),dip2px(250));
        View view=inflater.inflate(layout_id,null,true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popupWindow_sec);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
        return popupWindow;
    }
    private PopupWindow showReturnWindow(int layout_id){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        PopupWindow popupWindow=new PopupWindow(dip2px(200),dip2px(200));
        View view=inflater.inflate(layout_id,null,true);
        popupWindow.setContentView(view);
        //popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.popupWindow);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(getWindow().getDecorView(),Gravity.START|Gravity.BOTTOM,20,0);
        return popupWindow;
    }
    public void updateColorViewer(int color){
        colorviewer.setBackground(new LayerDrawable(new Drawable[]{getResources().getDrawable(R.drawable.trans_1),new ColorDrawable(color)}));
        color_picked=color;
        colorviewer.setText(String.format("%02x%02x%02x%02x",Color.alpha(color),Color.red(color),Color.green(color),Color.blue(color)));
        float ave=(Color.red(color)+Color.green(color)+Color.blue(color))/3f/255f;
        if(ave<0.5) colorviewer.setTextColor(Color.WHITE);
        else colorviewer.setTextColor(Color.BLACK);
    }
    @SuppressLint("ClickableViewAccessibility")
    public PopupWindow showColorSelectorWin(Context ctx,View.OnClickListener listener){
        PopupWindow colorSelectorWin=showSimplePopWindow(ctx,R.layout.popupwin_colorselector);
        View ctt=colorSelectorWin.getContentView();
        ImageView color_adjust=ctt.findViewById(R.id.color_plane);
        ImageView color_opacity=ctt.findViewById(R.id.alpha_plate);
        ImageView color_select=ctt.findViewById(R.id.lightness_plane);
        ImageView color_brightness=ctt.findViewById(R.id.brightness_plate);
        Button button_pick=ctt.findViewById(R.id.button_yes);
        Button button_cancel=ctt.findViewById(R.id.button_no);
        colorviewer=ctt.findViewById(R.id.colorviewer);
        int color_before=colorListAdapter.getColor(0).toArgb();
        AppGlobalData.makeAlphaPlane(color_before);
        AppGlobalData.makeBrightnessPlane(color_before);
        color_brightness.setBackground(new BitmapDrawable(AppGlobalData.brightness_plane));
        color_adjust.setBackground(new BitmapDrawable(AppGlobalData.makeColorPlane(color_before)));
        color_opacity.setBackground(new BitmapDrawable(AppGlobalData.alpha_plane));
        color_select.setBackground(new BitmapDrawable(AppGlobalData.colorful_bar));
        updateColorViewer(color_before);
        color_select.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x=(int)motionEvent.getX();
                int y=(int)motionEvent.getY();
                try{
                    int color_touch = getBitmapFromView(color_select).getPixel(x, y);
                    AppGlobalData.alpha_plane=Bitmap.createBitmap(870,300,Bitmap.Config.ARGB_8888);
                    AppGlobalData.makeAlphaPlane(color_touch);
                    AppGlobalData.makeBrightnessPlane(color_touch);
                    color_brightness.setBackground(new BitmapDrawable(AppGlobalData.brightness_plane));
                    color_adjust.setBackground(new BitmapDrawable(AppGlobalData.makeColorPlane(color_touch)));
                    color_opacity.setBackground(new BitmapDrawable(AppGlobalData.alpha_plane));
                    updateColorViewer(color_touch);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                return true;
            }
        });
        color_adjust.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x=(int)motionEvent.getX();
                int y=(int)motionEvent.getY();
                try {
                    int color_touch = getBitmapFromView(color_adjust).getPixel(x, y);
                    AppGlobalData.alpha_plane=Bitmap.createBitmap(870,300,Bitmap.Config.ARGB_8888);
                    AppGlobalData.makeAlphaPlane(color_touch);
                    AppGlobalData.makeBrightnessPlane(color_touch);
                    color_opacity.setBackground(new BitmapDrawable(AppGlobalData.alpha_plane));
                    color_brightness.setBackground(new BitmapDrawable(AppGlobalData.brightness_plane));
                    updateColorViewer(color_touch);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                return true;
            }
        });
        color_opacity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y=motionEvent.getY();
                try {
                    int color_touch=Color.argb(((int) (255 - y / color_opacity.getMeasuredHeight() * 255)),Color.red(color_picked),Color.green(color_picked),Color.blue(color_picked));
                    updateColorViewer(color_touch);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                return true;
            }
        });
        color_brightness.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x=(int)motionEvent.getX();
                int y=(int)motionEvent.getY();
                try {
                    int color_touch = getBitmapFromView(color_brightness).getPixel(x, y);
                    AppGlobalData.makeAlphaPlane(color_touch);
                    color_opacity.setBackground(new BitmapDrawable(AppGlobalData.alpha_plane));
                    updateColorViewer(color_touch);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                return true;
            }
        });
        button_pick.setOnClickListener(listener);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorSelectorWin.dismiss();
            }
        });
        return colorSelectorWin;
    }
    public int dip2px(float dpValue) {
        Context context=MainActivity.this;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public Bitmap getBitmapFromView(View v)
    {
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
        {
            bgDrawable.draw(c);
        }
        else
        {
            c.drawColor(Color.WHITE);
        }
        // Draw view to canvas
        v.draw(c);
        return b;
    }
    public void saveImage(){
        try{
            File saveFile=new File(pathStr);
            if(!saveFile.exists()) saveFile.createNewFile();
            FileOutputStream outputStream=new FileOutputStream(saveFile);
            pic.getBitmap().compress(Bitmap.CompressFormat.PNG,80,outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void showNewFileDialog(){
        pathStr=null;
        SettingDialog dialog=new SettingDialog(this,R.style.settingDialog_style);
        dialog.setTitle("新建文件");
        dialog.setContentView_(LayoutInflater.from(this).inflate(R.layout.dialog_new_file,null,true));
        EditText lengthEdit=dialog.view.findViewById(R.id.length_edit);
        EditText heightEdit=dialog.view.findViewById(R.id.height_edit);
        lengthEdit.setText("16");heightEdit.setText("16");
        dialog.setEnableButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lengthEdit.getText().toString().length()!=0&&heightEdit.getText().toString().length()!=0) {
                    Bitmap bitmap = Bitmap.createBitmap(Integer.parseInt(lengthEdit.getText().toString()), Integer.parseInt(heightEdit.getText().toString()), Bitmap.Config.ARGB_8888);
                    pic.setInitBitmap(bitmap);
                    dialog.dismiss();
                }else Toast.makeText(MainActivity.this, "请输入尺寸", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    public View.OnClickListener getToolOnClickListener(ImageButton view, final int tool_id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools[tool_id]=!tools[tool_id];
                if(!tools[tool_id]) {
                    Listeners.resetListenersForTools();
                    view.setBackgroundResource(R.drawable.shape_button_selected);
                    enable_move=false;
                    switch (tool_id){
                        case 0:
                            pic.setOnPixelTouchListener(onPixelClickListener);
                            break;
                        case 1:
                            pic.setOnPixelTouchListener(onPixelTouchListener);
                            break;
                        case 2:
                            pic.setOnPixelTouchListener(onBucketUseListener);
                            break;
                        case 3:
                            pic.setOnPixelTouchListener(onColorPickerUseListener);
                            break;
                        case 4:
                            pic.setOnPixelTouchListener(onEraserUseClickListener);
                            break;
                    }
                }else{
                    view.setBackgroundResource(R.drawable.shape_sel);
                    pic.setOnPixelTouchListener(null);
                    pic.setOnPixelClickListener(null);
                    enable_move=true;
                }
            }
        };
    }

    public float getDistance(float p1_x,float p1_y,float p2_x,float p2_y){
        float x=Math.abs(p1_x-p2_x),y=Math.abs(p1_y-p2_y);
        return ((float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }


}
