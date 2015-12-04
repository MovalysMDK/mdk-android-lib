/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>DataSetObservable class.</p>
 *
 */
public class DataSetObservable implements Serializable{

	/** serialisation ID */
	private static final long serialVersionUID = -1295558847154521852L;
	/**
     * The list of observers.  An observer can be in the list at most
     * once and will never be null.
     */
    protected final ArrayList<DataSetObserver> mObservers = new ArrayList<DataSetObserver>();

    /**
     * Adds an observer to the list. The observer cannot be null and it must not already
     * be registered.
     *
     * @param p_oObserver the observer to register
     * @throws java.lang.IllegalArgumentException the observer is null
     * @throws java.lang.IllegalStateException the observer is already registered
     */
    public void registerObserver(DataSetObserver p_oObserver) {
        if (p_oObserver == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized(mObservers) {
            if (!mObservers.contains(p_oObserver)) {
                mObservers.add(p_oObserver);
            }
        }
    }

    /**
     * Removes a previously registered observer. The observer must not be null and it
     * must already have been registered.
     *
     * @param p_oObserver the observer to unregister
     * @throws java.lang.IllegalArgumentException the observer is null
     * @throws java.lang.IllegalStateException the observer is not yet registered
     */
    public void unregisterObserver(DataSetObserver p_oObserver) {
        if (p_oObserver == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized(mObservers) {
            int index = mObservers.indexOf(p_oObserver);
            if (index != -1) {
                mObservers.remove(index);
            }
        }
    }

    /**
     * Remove all registered observers.
     */
    public void unregisterAll() {
        synchronized(mObservers) {
            mObservers.clear();
        }
    }
	
	/**
	 * <p>notifyChanged.</p>
	 */
	public void notifyChanged() {
		synchronized (mObservers) {
			// since onChanged() is implemented by the app, it could do
			// anything, including
			// removing itself from {@link mObservers} - and that could cause
			// problems if
			// an iterator is used on the ArrayList {@link mObservers}.
			// to avoid such problems, just march thru the list in the reverse
			// order.
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onChanged();
			}
		}
	}

	/**
	 * <p>notifyInvalidated.</p>
	 */
	public void notifyInvalidated() {
		synchronized (mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onInvalidated();
			}
		}
	}

}
