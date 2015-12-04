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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AbstractTaskableAction;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.Action;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.NullActionParameterImpl;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.genericdisplay.InDisplayParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.core.services.BeanLoader;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.messages.MobileFwkException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.MasterVisualComponent;

/**
 * <p>
 * Launch a specified Action or Activity when the button is clicked.
 * </p>
 * 
 * 
 */
public class MMActionButton extends MMButton implements OnClickListener {
	/**
	 * Action class
	 */
	private Class<Action<?, ?, ?, ?>> actionClass = null;

	/**
	 * Activity class
	 */
	private Class<Activity> activityClass = null;

	/**
	 * Method to launch.
	 */
	private Method methodToLaunch;

	/**
	 * Use Activity.startActivityForResult or Activity.startActivity
	 */
	private boolean startForResult;

	/**
	 * Constructor
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 * @param p_oDefStyle the android style define in XML
	 */
	public MMActionButton(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.startForResult = false;
			this.setOnClickListener(this);
			this.setTrigger(p_oAttrs);
		}
	}

	/**
	 * Constructor
	 * @param p_oContext the android context
	 * @param p_oAttrs the android view AttributeSet (in layout XML)
	 */
	public MMActionButton(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.startForResult = false;
			this.setOnClickListener(this);
			this.setTrigger(p_oAttrs);
		}
	}

	/**
	 * Action or Activity name and class setter. The XML attributes <code>action</code>, <code>activity</code> and <code>activityForResult</code> are
	 * exclusives as only one class is called.
	 * 
	 * @param p_oAttrs Attributes set in the XML configuration
	 */
	@SuppressWarnings("unchecked")
	private void setTrigger(AttributeSet p_oAttrs) {
		this.actionClass = (Class<Action<?, ?, ?, ?>>) this.readClassAttribute(p_oAttrs, "action");
		if (this.actionClass == null) {
			this.activityClass = (Class<Activity>) this.readClassAttribute(p_oAttrs, "activity");
			if (this.activityClass == null) {
				this.activityClass = (Class<Activity>) this.readClassAttribute(p_oAttrs, "activityForResult");
				if (this.activityClass == null) {
					this.methodToLaunch = this.readMethodAttribute(p_oAttrs);
					if (this.methodToLaunch == null) {
						try {
							this.actionClass = (Class<Action<?, ?, ?, ?>>) Class.forName(this.aivFwkDelegate.getModel());
						} catch (ClassNotFoundException e) {
							this.actionClass = null;
						}
					}
				}
				else {
					this.startForResult = true;
				}
			}
		}
		this.setFocusable(false);
	}

	/**
	 * Read the attribute set and Get a class from it
	 * @param p_oAttrs the attribute test to parse
	 * @param p_sAttributeName the attribute name to get
	 * @return the class parsed from attribute set if exists
	 */
	private final Class<?> readClassAttribute(AttributeSet p_oAttrs, String p_sAttributeName) {
		Class<?> r_oClass =null;
		try {
			String sClassName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, p_sAttributeName);
			if (sClassName != null && sClassName.length() > 0) {
				r_oClass = Class.forName(sClassName);
			}
		} catch (ClassNotFoundException e) {
			// Nothing to do
			Log.d("MMActionButton", "ClassNotFoundException"+p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, p_sAttributeName));
		}
		return r_oClass;
	}

	/**
	 * Read attributes
	 * @param p_oAttrs the view AttributeSet
	 * @return the method to call on button click
	 */
	private final Method readMethodAttribute(AttributeSet p_oAttrs) {
		Method r_oMethod = null;
		try {
			String sMethodName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "methodToLaunch");
			if (sMethodName != null && sMethodName.length() > 0) {
				r_oMethod = this.getContext().getClass().getMethod(sMethodName);
			}
		} catch (NoSuchMethodException e) {
			Log.d("MMActionButton", "NoSuchMethodException"+p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "methodToLaunch"));
		}
		return r_oMethod;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View p_oView) {
		ActionParameter oParam = null;

		if (this.actionClass != null) {
			if (this.getParent() != null && this.getParent() instanceof MasterVisualComponent) {
				MasterVisualComponent oMasterVisualComponent = ((MasterVisualComponent) this.getParent());
				if (oMasterVisualComponent.getViewModel() != null) {
					oParam = new InDisplayParameter();
					((InDisplayParameter) oParam).setId( oMasterVisualComponent.getViewModel().getIdVM() );
				}
			}
			if (oParam == null) {
				oParam = new NullActionParameterImpl();
			}
			Action<?, ?, ?, ?> oTempAction = (Action<?, ?, ?, ?>) BeanLoader.getInstance().getBean(this.actionClass);
			if (oTempAction instanceof AbstractTaskableAction) {
				AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) oTempAction;
				((AbstractMMActivity) this.getContext()).launchAction(oAction, this.actionClass, oParam, this);
			} else {
				Application.getInstance().getController().launchAction(this.actionClass, oParam);
			}
		}
		else if (this.activityClass != null) {
			Intent oIntent= new Intent(this.getContext(), this.activityClass);
			if (this.startForResult) {
				try {
					Field oRequestCodeField = this.activityClass.getField("REQUEST_CODE");
					((AbstractMMActivity) this.getContext()).startActivityForResult(oIntent, oRequestCodeField.getInt(null));
				}
				catch (IllegalArgumentException e) {
					throw new MobileFwkException(e);
				}
				catch (SecurityException e) {
					throw new MobileFwkException(e);
				}
				catch (IllegalAccessException e) {
					throw new MobileFwkException(e);
				}
				catch (NoSuchFieldException e) {
					throw new MobileFwkException(e);
				}
			}
			else {
				this.getContext().startActivity(oIntent);
			}
		}
		else if (this.methodToLaunch != null) {
			try {
				this.methodToLaunch.invoke(this.getContext());
			}
			catch (InvocationTargetException e) {
				throw new MobileFwkException(e);
			}
			catch (IllegalAccessException e) {
				throw new MobileFwkException(e);
			}
		}
	}
}
