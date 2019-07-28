package com.pixeldraw.dbrt.pixeldraw;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WarningDialog extends Dialog {
    LinearLayout linearLayout;
    Button cancel_button,enable_button;
    CharSequence title_str,str;
    View.OnClickListener onClickListener;
    public WarningDialog(@NonNull Context context) {
        super(context);
    }

    WarningDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WarningDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);

    }
    /*public void setContentView_(View view){
        this.view=view;
    }
    */
    @Override
    public void setTitle(@Nullable CharSequence title) {
        title_str=title;
        super.setTitle(title);

    }
    public void setContentText(CharSequence str){
        this.str=str;
    }
    public void setEnableButtonOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Override
    public void show() {
        View content=LayoutInflater.from(getContext()).inflate(R.layout.dialog_enable,null,true);
        enable_button=content.findViewById(R.id.enable);
        if(onClickListener!=null)
            enable_button.setOnClickListener(onClickListener);
        else
            enable_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WarningDialog.this.dismiss();
                }
            });
        linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView=new TextView(getContext());
        textView.setText(title_str);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getContext().getResources().getColor(R.color.background_grey));
        textView.setTextSize(getContext().getResources().getDimension(R.dimen.dialog_title_size));
        textView.setPadding(AppGlobalData.MA_INSTANCE.dip2px(15),AppGlobalData.MA_INSTANCE.dip2px(15),AppGlobalData.MA_INSTANCE.dip2px(15),AppGlobalData.MA_INSTANCE.dip2px(5));
        TextView textView1=new TextView(getContext());
        textView1.setText(str);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(AppGlobalData.MA_INSTANCE.dip2px(200),AppGlobalData.MA_INSTANCE.dip2px(50)));
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextColor(getContext().getResources().getColor(android.R.color.darker_gray));
        textView1.setTextSize(getContext().getResources().getDimension(R.dimen.dialog_text_size));
        linearLayout.addView(textView1,0);
        linearLayout.addView(content);
        linearLayout.addView(textView,0);

        setContentView(linearLayout);
        super.show();
    }


}
