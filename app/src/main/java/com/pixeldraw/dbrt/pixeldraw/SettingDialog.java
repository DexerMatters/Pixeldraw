package com.pixeldraw.dbrt.pixeldraw;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingDialog extends Dialog {
    LinearLayout linearLayout;
    View view;
    Button cancel_button,enable_button;
    CharSequence title_str;
    View.OnClickListener onClickListener;
    public SettingDialog(@NonNull Context context) {
        super(context);
    }

    public SettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SettingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void setContentView_(View view){
        this.view=view;
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        title_str=title;
        super.setTitle(title);

    }
    public void setEnableButtonOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @Override
    public void show() {
        View content=LayoutInflater.from(getContext()).inflate(R.layout.dialog_select,null,true);
        cancel_button=(Button) content.findViewById(R.id.cancel);
        enable_button=(Button) content.findViewById(R.id.enable);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        enable_button.setOnClickListener(onClickListener);
        linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView=new TextView(getContext());
        textView.setText(title_str);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getContext().getResources().getColor(R.color.background_grey));
        textView.setTextSize(getContext().getResources().getDimension(R.dimen.dialog_title_size));
        textView.setPadding(0,AppGlobalData.MA_INSTANCE.dip2px(15),0,AppGlobalData.MA_INSTANCE.dip2px(5));
        linearLayout.addView(view,0);
        linearLayout.addView(content);
        linearLayout.addView(textView,0);

        setContentView(linearLayout);
        super.show();
    }


}
