package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
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
 * <p>
 * Copyright (c) 2012
 * <p>
 * Company: Adeuza
 * 
 * @author smaitre
 * @author ktilhou
 * 
 */
public class MMActionImageButton extends MMImageButton implements OnClickListener {
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
	 */
	public MMActionImageButton(Context p_oContext, AttributeSet p_oAttrs, int p_oDefStyle) {
		super(p_oContext, p_oAttrs, p_oDefStyle);
		if(!isInEditMode()) {
			this.startForResult = false;
			this.setOnClickListener(this);
			this.setTrigger(p_oAttrs);
		}
	}

	/**
	 * Constructor
	 */
	public MMActionImageButton(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.startForResult = false;
			this.setOnClickListener(this);
			this.setTrigger(p_oAttrs);
		}
	}

	/**
	 * Constructor
	 */
	public MMActionImageButton(Context p_oContext) {
		super(p_oContext);
		if(!isInEditMode()) {
			this.startForResult = false;
			this.setOnClickListener(this);
			this.setFocusable(false);
		}
	}

	/**
	 * Action or Activity name and class setter. The XML attributes <code>action</code>, <code>activity</code> and <code>activityForResult</code> are
	 * exclusives as only one class is called.
	 * 
	 * @param p_oAttrs
	 *            Attributes set in the XML configuration
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
							this.actionClass = (Class<Action<?, ?, ?, ?>>) Class.forName(this.getModel());
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

	private final Class<?> readClassAttribute(AttributeSet p_oAttrs, String p_sAttributeName) {
		Class<?> r_oClass =null;
		try {
			String sClassName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, p_sAttributeName);
			if (sClassName != null && sClassName.length() > 0) {
				r_oClass = Class.forName(sClassName);
			}
		} catch (ClassNotFoundException e) {
			r_oClass =null;

		}
		return r_oClass;
	}

	private final Method readMethodAttribute(AttributeSet p_oAttrs) {
		Method r_oMethod = null;
		try {
			String sMethodName = p_oAttrs.getAttributeValue(Application.MOVALYSXMLNS, "methodToLaunch");
			if (sMethodName != null && sMethodName.length() > 0) {
				r_oMethod = this.getContext().getClass().getMethod(sMethodName);
			}
		} catch (NoSuchMethodException e) {
			r_oMethod = null;
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
		ActionParameter r_oParam = null;
		if (this.actionClass != null) {
			Action<?, ?, ?, ?> oTempAction = (Action<?, ?, ?, ?>) BeanLoader.getInstance().getBean(this.actionClass);
			r_oParam = oTempAction.getEmptyInParameter();
			if ( this.getParent() != null && this.getParent() instanceof MasterVisualComponent 
					&& r_oParam instanceof InDisplayParameter) {
				MasterVisualComponent<?> oMasterVisualComponent = ((MasterVisualComponent<?>) this.getParent());
				((InDisplayParameter) r_oParam).setId( oMasterVisualComponent.getViewModel().getIdVM() );
			}

			if (r_oParam == null) {
				r_oParam = new NullActionParameterImpl();
			}

			if (oTempAction instanceof AbstractTaskableAction) {
				AbstractTaskableAction<?, ?, ?, ?> oAction = (AbstractTaskableAction<?, ?, ?, ?>) oTempAction;
				((AbstractMMActivity) this.getContext()).launchAction(oAction, this.actionClass, r_oParam, this);
			} else {
				Application.getInstance().getController().launchAction(this.actionClass, r_oParam);
			}
		}
		else if (this.activityClass != null) {
			Intent oIntent= new Intent(this.getContext(), this.activityClass);
			if (this.startForResult) {
				try {
					Field oRequestCodeField = this.activityClass.getField("REQUEST_CODE");
					((AbstractMMActivity) this.getContext()).startActivityForResult(oIntent,  oRequestCodeField.getInt(null));
				}catch (IllegalArgumentException e) {
					throw new MobileFwkException(e);
				}catch (SecurityException e) {
					throw new MobileFwkException(e);
				}catch (IllegalAccessException e) {
					throw new MobileFwkException(e);
				}catch (NoSuchFieldException e) {
					throw new MobileFwkException(e);
				}
			}else {
				this.getContext().startActivity(oIntent);
			}
		}
		else if (this.methodToLaunch != null) {
			try {
				this.methodToLaunch.invoke(this.getContext());
			}catch (InvocationTargetException e) {
				throw new MobileFwkException(e);
			}catch (IllegalAccessException e) {
				throw new MobileFwkException(e);
			}
		}
	}
}
