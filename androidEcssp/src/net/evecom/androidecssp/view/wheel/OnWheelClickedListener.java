/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:28:02 ¿‡OnWheelClickedListener
 * 
 * @author Mars zhang
 * 
 */
public interface OnWheelClickedListener {
    /**
     * Callback method to be invoked when current item clicked
     * 
     * @param wheel
     *            the wheel view
     * @param itemIndex
     *            the index of clicked item
     */
    void onItemClicked(WheelView wheel, int itemIndex);
}
