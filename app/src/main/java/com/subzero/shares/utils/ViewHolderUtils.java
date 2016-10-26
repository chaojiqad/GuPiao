package com.subzero.shares.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * view缓存器
 * Created by zzy on 2016/4/22.
 */
public class ViewHolderUtils {


    public static View getView(View v, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) v.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            v.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = v.findViewById(id);
            viewHolder.put(id, childView);
        }
        return childView;
    }

}
