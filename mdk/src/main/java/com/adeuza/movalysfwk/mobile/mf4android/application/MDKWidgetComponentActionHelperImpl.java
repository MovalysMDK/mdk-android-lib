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
package com.adeuza.movalysfwk.mobile.mf4android.application;

import android.content.Context;

import android.content.Intent;

import com.soprasteria.movalysmdk.widget.core.command.AsyncWidgetCommand;
import com.soprasteria.movalysmdk.widget.core.listener.AsyncWidgetCommandListener;
import com.soprasteria.movalysmdk.widget.core.provider.MDKWidgetComponentActionHandler;
import com.soprasteria.movalysmdk.widget.core.provider.MDKWidgetComponentActionHelper;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple implementation for the MDKWidgetComponentActionHelper interface.
 * This class can manage only one instance of a given command class per component.
 * Regarding asynchronous commands, the implementation should be the following:
 * <ul>
 *     <li>call the <em>startAsyncCommandOnWidget</em> method to start the command</li>
 *     <li>call the <em>restoreAsyncCommandsOnWidget</em> in the restoration phase of the listener (in the onRestoreInstanceState for a view)</li>
 *     <li>call the <em>removeCommandListenerOnWidget</em> when the listener is destroyed</li>
 *     <li>call the <em>removeCommandOnWidget</em> when the command is done</li>
 * </ul>
 */
public class MDKWidgetComponentActionHelperImpl implements MDKWidgetComponentActionHelper {

    /** A map that stores all the registered handlers. **/
    private Map<Integer, MDKWidgetComponentActionHandler> widgetHandlerForIntentMap;

    /** asynchronous commands list to widgets maps. */
    private Map<Integer, List<AsyncWidgetCommand>> asyncCommandsMap;


    /** AtomicInteger used to compute the views unique ids. */
    private AtomicInteger atomicInteger;

    /**
     * Constructor.
     */
    public MDKWidgetComponentActionHelperImpl() {
        asyncCommandsMap = new HashMap<>();
        widgetHandlerForIntentMap = new HashMap<>();
        atomicInteger = new AtomicInteger(0);
    }

    @Override
    public int getUniqueId() {
        atomicInteger.compareAndSet(65536, 0);
        return atomicInteger.addAndGet(1);
    }

    @Override
    public <I, O> O startAsyncCommandOnWidget(Context context, AsyncWidgetCommandListener widget, AsyncWidgetCommand<I, O> command, I commandParam) {
        removeCommandOnWidget(widget, command.getClass(), true);

        int widgetId = widget.getMDKWidgetDelegate().getUniqueId();

        if (!asyncCommandsMap.containsKey(widgetId)) {
            asyncCommandsMap.put(widgetId, new ArrayList<AsyncWidgetCommand>());
        }
        asyncCommandsMap.get(widgetId).add((AsyncWidgetCommand)command);

        command.setListener(widget);

        return command.execute(context, commandParam);
    }

    @Override
    public void restoreAsyncCommandsOnWidget(AsyncWidgetCommandListener widget) {
        int widgetId = widget.getMDKWidgetDelegate().getUniqueId();

        if (asyncCommandsMap.containsKey(widgetId)) {
            List<AsyncWidgetCommand> asyncCommands = asyncCommandsMap.get(widgetId);

            for (AsyncWidgetCommand command : asyncCommands) {
                if (command != null) {
                    command.setListener(widget);
                }
            }
        }
    }

    @Override
    public void removeCommandListenerOnWidget(AsyncWidgetCommandListener widget, Class<?> commandClass) {
        int widgetId = widget.getMDKWidgetDelegate().getUniqueId();

        if (asyncCommandsMap.containsKey(widgetId)) {
            List<AsyncWidgetCommand> asyncCommands = asyncCommandsMap.get(widgetId);

            for (AsyncWidgetCommand cmd : asyncCommands) {
                if (cmd != null && commandClass.equals(cmd.getClass())) {
                    cmd.setListener(null);
                }
            }
        }
    }

    @Override
    public void removeCommandOnWidget(AsyncWidgetCommandListener widget, Class<?> commandClass, boolean cancel) {
        int widgetId = widget.getMDKWidgetDelegate().getUniqueId();

        if (asyncCommandsMap.containsKey(widgetId)) {
            List<AsyncWidgetCommand> asyncCommands = asyncCommandsMap.get(widgetId);

            List<AsyncWidgetCommand> asyncCommandsToRemove = new ArrayList<>();

            for (AsyncWidgetCommand cmd : asyncCommands) {
                if (cmd != null && commandClass.equals(cmd.getClass())) {
                    if (cancel) {
                        cmd.cancel();
                    }
                    cmd.setListener(null);
                    asyncCommandsToRemove.add(cmd);
                }
            }

            asyncCommands.removeAll(asyncCommandsToRemove);

            if (asyncCommandsMap.get(widgetId).isEmpty()) {
                asyncCommandsMap.remove(widgetId);
            }
        }
    }


    @Override
    public void registerActivityResultHandler(int handlerKey, MDKWidgetComponentActionHandler handler) {
        // Register the handler in the HashMap.
        widgetHandlerForIntentMap.put(handlerKey, handler);
    }

    @Override
    public void unregisterActivityResultHandler(int handlerKey) {
        widgetHandlerForIntentMap.remove(handlerKey);
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        // Retrieve the registered handler at the requestCode key, and give it the data.
        if (widgetHandlerForIntentMap.containsKey(requestCode)) {
            widgetHandlerForIntentMap.get(requestCode).handleActivityResult(resultCode, data);
        }
    }

}
