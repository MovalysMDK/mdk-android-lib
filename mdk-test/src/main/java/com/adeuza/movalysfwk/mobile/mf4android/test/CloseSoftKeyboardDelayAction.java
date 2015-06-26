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
package com.adeuza.movalysfwk.mobile.mf4android.test;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CloseKeyboardAction;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * Custom closeSoftKeyboard method.
 * Workaround to manage time delay to close soft keyboard bug.
 */
public class CloseSoftKeyboardDelayAction implements ViewAction {

    /**
     * The delay time to allow the soft keyboard to dismiss.
     */
    private static final long KEYBOARD_DISMISSAL_DELAY_MILLIS = 1000L;

    /**
     * The real {@link CloseKeyboardAction} instance.
     */
    private final ViewAction mCloseSoftKeyboard = new CloseKeyboardAction();

    @Override
    public Matcher<View> getConstraints() {
        return mCloseSoftKeyboard.getConstraints();
    }

    @Override
    public String getDescription() {
        return mCloseSoftKeyboard.getDescription();
    }

    @Override
    public void perform(final UiController uiController, final View view) {
        mCloseSoftKeyboard.perform(uiController, view);
        uiController.loopMainThreadForAtLeast(KEYBOARD_DISMISSAL_DELAY_MILLIS);
    }

    /**
     * Close keyboard and wait until closed.
     * @return a new CloseSoftKeyboardDelayAction action
     */
    public static ViewAction closeSoftKeyboardDelay() {
        return new CloseSoftKeyboardDelayAction();
    }
}
