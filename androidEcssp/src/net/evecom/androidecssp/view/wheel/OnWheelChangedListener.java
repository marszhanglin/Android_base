/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:28:09 ¿‡OnWheelChangedListener
 * 
 * @author Mars zhang
 * 
 */
public interface OnWheelChangedListener {
    /**
     * Callback method to be invoked when current item changed
     * 
     * @param wheel
     *            the wheel view whose state has changed
     * @param oldValue
     *            the old value of current item
     * @param newValue
     *            the new value of current item
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}
