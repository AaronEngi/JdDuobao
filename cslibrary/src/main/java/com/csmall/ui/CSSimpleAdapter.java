package com.csmall.ui;

import android.widget.BaseAdapter;

/**
 * 实现了两个通常用不到的方法
 * Created by wangchao on 2015/9/29.
 */
public abstract class CSSimpleAdapter extends BaseAdapter {
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
