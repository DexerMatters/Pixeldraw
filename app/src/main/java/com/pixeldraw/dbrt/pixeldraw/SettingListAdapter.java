package com.pixeldraw.dbrt.pixeldraw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SettingListAdapter extends BaseAdapter {
    Context context;
    int resId;
    String names[]={"控件缩放","文字大小","其他设置都是浮云"};
    public SettingListAdapter(Context context, ListView listView, int resId){
        this.context=context;
        this.resId=resId;
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dip2px(30*getCount())));
    }
    @Override
    public int getCount() {
        return names.length;
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
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view_=layoutInflater.inflate(resId,null,true);
        ((TextView)view_.findViewById(R.id.name)).setText(names[i]);
        return view_;
    }
    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
