/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel;

/**
 * 
 * 2014-7-22ÏÂÎç4:28:17 ÀàItemsRange
 * 
 * @author Mars zhang
 * 
 */
public class ItemsRange {
    /** MemberVariables */
    private int first;

    /** MemberVariables */
    private int count;

    /**
     * Default constructor. Creates an empty range
     */
    public ItemsRange() {
        this(0, 0);
    }

    /**
     * Constructor
     * 
     * @param first
     *            the number of first item
     * @param count
     *            the count of items
     */
    public ItemsRange(int first, int count) {
        this.first = first;
        this.count = count;
    }

    /**
     * Gets number of first item
     * 
     * @return the number of the first item
     */
    public int getFirst() {
        return first;
    }

    /**
     * Gets number of last item
     * 
     * @return the number of last item
     */
    public int getLast() {
        return getFirst() + getCount() - 1;
    }

    /**
     * Get items count
     * 
     * @return the count of items
     */
    public int getCount() {
        return count;
    }

    /**
     * Tests whether item is contained by range
     * 
     * @param index
     *            the item number
     * @return true if item is contained
     */
    public boolean contains(int index) {
        return index >= getFirst() && index <= getLast();
    }
}