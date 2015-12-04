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
package com.google.zxing.client.android.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import android.os.Build;
import android.util.Log;

/**
 * <p>Sometimes the application wants to access advanced functionality exposed by Android APIs that are only available
 * in later versions of the platform. While {@code Build.VERSION} can be used to determine the device's API level
 * and alter behavior accordingly, and it is possible to write code that uses both old and new APIs selectively,
 * such code would fail to load on older devices that do not have the new API methods.</p>
 *
 * <p>It is necessary to only load classes that use newer APIs than the device may support after the app
 * has checked the API level. This requires reflection, loading one of several implementations based on the
 * API level.</p>
 *
 * <p>This class manages that process. Subclasses of this class manage access to implementations of a given interface
 * in an API-level-aware way. Subclasses implementation classes <em>by name</em>, and the minimum API level that
 * the implementation is compatible with. They also provide a default implementation.</p>
 *
 * <p>At runtime an appropriate implementation is then chosen, instantiated and returned from {@link #build()}.</p>
 *
 * @param <T> the interface which managed implementations implement
 */
public abstract class PlatformSupportManager<T> {
  
  private static final String TAG = PlatformSupportManager.class.getSimpleName();

  private final Class<T> managedInterface;
  private final T defaultImplementation;
  private final SortedMap<Integer,String> implementations;
  
  /**
   * <p>Constructor for PlatformSupportManager.</p>
   *
   * @param p_oManagedInterface a {@link java.lang.Class} object.
   * @param p_oDefaultImplementation a T object.
   */
  protected PlatformSupportManager(Class<T> p_oManagedInterface, T p_oDefaultImplementation) {
    if (!p_oManagedInterface.isInterface()) {
      throw new IllegalArgumentException();
    }
    if (!p_oManagedInterface.isInstance(p_oDefaultImplementation)) {
      throw new IllegalArgumentException();
    }
    this.managedInterface = p_oManagedInterface;
    this.defaultImplementation = p_oDefaultImplementation;
    this.implementations = new TreeMap<>(Collections.reverseOrder());
  }
  
  /**
   * <p>addImplementationClass.</p>
   *
   * @param minVersion a int.
   * @param className a {@link java.lang.String} object.
   */
  protected final void addImplementationClass(int minVersion, String className) {
    implementations.put(minVersion, className);
  }

  /**
   * <p>build.</p>
   *
   * @return a T object.
   */
  public final T build() {
    for (Integer minVersion : implementations.keySet()) {
      if (Build.VERSION.SDK_INT >= minVersion) {
        String className = implementations.get(minVersion);
        try {
          Class<? extends T> clazz = Class.forName(className).asSubclass(managedInterface);
          Log.i(TAG, "Using implementation " + clazz + " of " + managedInterface + " for SDK " + minVersion);
          return clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException cnfe) {
          Log.w(TAG, cnfe);
        } catch (IllegalAccessException iae) {
          Log.w(TAG, iae);
        } catch (InstantiationException ie) {
          Log.w(TAG, ie);
        } catch (NoSuchMethodException nsme) {
          Log.w(TAG, nsme);
        } catch (InvocationTargetException ite) {
          Log.w(TAG, ite);
        }
      }
    }
    Log.i(TAG, "Using default implementation " + defaultImplementation.getClass() + " of " + managedInterface);
    return defaultImplementation;
  }

}
