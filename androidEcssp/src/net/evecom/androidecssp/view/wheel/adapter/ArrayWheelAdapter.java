/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel.adapter;

import android.content.Context;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:28:39 ¿‡ArrayWheelAdapter
 * 
 * @author Mars zhang
 * 
 * @param <T>
 */
public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

    // items
    /** MemberVariables */
    private T items[];

    /**
     * Constructor
     * 
     * @param context
     *            the current context
     * @param items
     *            the items
     */
    public ArrayWheelAdapter(Context context, T items[]) {
        super(context);

        // setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            T item = items[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }
}
