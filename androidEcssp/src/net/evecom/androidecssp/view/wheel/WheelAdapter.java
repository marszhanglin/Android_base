/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel;

/**
 * 
 * 2014-7-22œ¬ŒÁ6:14:24 ¿‡WheelAdapter
 * 
 * @author Mars zhang
 * 
 */
public interface WheelAdapter {
    /**
     * Gets items count
     * 
     * @return the count of wheel items
     */
    public int getItemsCount();

    /**
     * Gets a wheel item by index.
     * 
     * @param index
     *            the item index
     * @return the wheel item text or null
     */
    public String getItem(int index);

    /**
     * Gets maximum item length. It is used to determine the wheel width. If -1
     * is returned there will be used the default wheel width.
     * 
     * @return the maximum item length or -1
     */
    public int getMaximumLength();
}
