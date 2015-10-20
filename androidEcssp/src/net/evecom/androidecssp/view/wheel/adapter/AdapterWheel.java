/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel.adapter;

import net.evecom.androidecssp.view.wheel.WheelAdapter;
import android.content.Context;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:28:47 ¿‡AdapterWheel
 * 
 * @author Mars zhang
 * 
 */
public class AdapterWheel extends AbstractWheelTextAdapter {

    /** MemberVariables */
    private WheelAdapter adapter;

    /**
     * Constructor
     * 
     * @param context
     *            the current context
     * @param adapter
     *            the source adapter
     */
    public AdapterWheel(Context context, WheelAdapter adapter) {
        super(context);

        this.adapter = adapter;
    }

    /**
     * Gets original adapter
     * 
     * @return the original adapter
     */
    public WheelAdapter getAdapter() {
        return adapter;
    }

    @Override
    public int getItemsCount() {
        return adapter.getItemsCount();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return adapter.getItem(index);
    }

}
