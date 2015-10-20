/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:27:46 ¿‡OnWheelScrollListener
 * 
 * @author Mars zhang
 * 
 */
public interface OnWheelScrollListener {
    /**
     * Callback method to be invoked when scrolling started.
     * 
     * @param wheel
     *            the wheel view whose state has changed.
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     * 
     * @param wheel
     *            the wheel view whose state has changed.
     */
    void onScrollingFinished(WheelView wheel);
}
