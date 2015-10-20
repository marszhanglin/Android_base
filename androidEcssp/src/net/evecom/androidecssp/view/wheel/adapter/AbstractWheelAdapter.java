/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.view.wheel.adapter;

import java.util.LinkedList;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * 2014-7-22œ¬ŒÁ4:29:14 ¿‡AbstractWheelAdapter
 * 
 * @author Mars zhang
 * 
 */
public abstract class AbstractWheelAdapter implements WheelViewAdapter {
    /** MemberVariables */
    private List<DataSetObserver> datasetObservers;

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if (datasetObservers == null) {
            datasetObservers = new LinkedList<DataSetObserver>();
        }
        datasetObservers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (datasetObservers != null) {
            datasetObservers.remove(observer);
        }
    }

    /**
     * Notifies observers about data changing
     */
    protected void notifyDataChangedEvent() {
        if (datasetObservers != null) {
            for (DataSetObserver observer : datasetObservers) {
                observer.onChanged();
            }
        }
    }

    /**
     * Notifies observers about invalidating data
     */
    protected void notifyDataInvalidatedEvent() {
        if (datasetObservers != null) {
            for (DataSetObserver observer : datasetObservers) {
                observer.onInvalidated();
            }
        }
    }
}
