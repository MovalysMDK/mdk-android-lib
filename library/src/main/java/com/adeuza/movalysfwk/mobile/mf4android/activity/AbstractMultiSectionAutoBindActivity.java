package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.util.Log;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMasterRelativeLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMultiSectionLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.ApplicationRGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.ConfigurationsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementConfiguration;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.management.ManagementZone;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.RealVisualComponentDescriptor;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorsHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.CustomizableViewModel;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.model.ViewModel;

/**
 * @author lmichenaud
 * @deprecated Replaced by {@link AbstractMultiSectionAutoBindFragmentActivity}
 */

public abstract class AbstractMultiSectionAutoBindActivity extends AbstractAutoBindMMActivity {

	/**
	 * Wlayout parameter name
	 */
	private static final String ROOTCOMPONENT_PARAMETER = "wlayout";
	
	/**
	 * Application
	 */
	private AndroidApplication application ;
	
	/**
	 * le workspace de la page
	 */
	private MMMultiSectionLayout sectionContainer ;
	
	/**
	 * la configuration associée au multi section container
	 */
	private ManagementConfiguration wconfiguration ;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doAfterSetContentView()
	 */
	@Override
	protected void doAfterSetContentView() {
		super.doAfterSetContentView();
				
		this.application = (AndroidApplication)Application.getInstance();
		if ( this.sectionContainer == null ) {
			this.sectionContainer = (MMMultiSectionLayout) this.findViewById(this.getMultiSectionId());
		}

		//récupération de la configuration du workspace
		this.wconfiguration = ConfigurationsHandler.getInstance().getManagementConfiguration(this.sectionContainer.getModel());
		
		//mise en place de l'auto mapping
		// 1 group = une page
		for(ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {
			for(ManagementZone oZone : oGroup.getVisibleZones()) {
				this.configureAutoBinding(oZone, this.getViewModel());
			}
		}
	}
	
	public abstract int getMultiSectionId();
	
	
	/**
	 * @param p_oZone
	 * @param p_oVm
	 */
	private void configureAutoBinding( ManagementZone p_oZone, ViewModel p_oVm ) {
		int iSectionId = application.getAndroidIdByStringKey(ApplicationRGroup.ID, p_oZone.getSource());
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iSectionId);
	
		//récupération du view model associé
		try {
			
			if ( !p_oZone.hasChild()) {
				ViewModel oViewModel = null;
				try {
					Field oField = p_oVm.getClass().getField(p_oZone.getName());
					oViewModel = (ViewModel) oField.get(p_oVm);
				} catch (NoSuchFieldException e) {
					Method oMethod = p_oVm.getClass().getMethod(p_oZone.getName());
					oViewModel = (ViewModel) oMethod.invoke(p_oVm);
				}
				oZoneLayout.setViewModel(oViewModel);
			}
			else {
				for(ManagementZone oSubZone : p_oZone.getVisibleZones()) {
					this.configureAutoBinding(oSubZone, this.getViewModel());
				}
			}
			
		} catch (SecurityException e) {
			Log.e(Application.LOG_TAG, "Erreur à l'affectation du viewmodel", e);
		} catch (IllegalArgumentException e) {
			Log.e(Application.LOG_TAG, "Erreur à l'affectation du viewmodel", e);
		} catch (IllegalAccessException e) {
			Log.e(Application.LOG_TAG, "Erreur à l'affectation du viewmodel", e);
		} catch (NoSuchMethodException e) {
			Log.e(Application.LOG_TAG, "Erreur à l'affectation du viewmodel", e);
		} catch (InvocationTargetException e) {
			Log.e(Application.LOG_TAG, "Erreur à l'affectation du viewmodel", e);
		}
		
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.inflate(String.valueOf(this.sectionContainer.hashCode()), oZoneLayout, this.getParameters());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#defineContentView()
	 */
	@Override
	protected void defineContentView() {
		Object oConfig = this.getLastCustomNonConfigurationInstance();
		if ( oConfig != null ) {
			// if configuration changed, use the saved wlayout component
			ViewGroup oRootView = (ViewGroup) ((Map<String, Object>) oConfig).get(ROOTCOMPONENT_PARAMETER);
			((ViewGroup) oRootView.getParent()).removeView(oRootView);
			this.sectionContainer = (MMMultiSectionLayout) oRootView.findViewById(this.getMultiSectionId());
			
			this.setContentView(oRootView);
			this.doAfterSetContentView();
			
			this.sectionContainer.init();
		}
		else {
			super.defineContentView();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#inflate(boolean)
	 */
	@Override
	public void inflate(boolean p_bWriteData) {
		Object oConfig = this.getLastCustomNonConfigurationInstance();
		
		// On ne refait pas le inflate dans le cas de la rotation
		if ( oConfig == null ) {
			super.inflate(p_bWriteData);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#localDestroy()
	 */
	@Override
	public void localDestroy() {
		if (wconfiguration!=null) {	
			for(ManagementGroup oGroup : wconfiguration.getVisibleGroups()) {
				for(ManagementZone oZone : oGroup.getVisibleZones()) {
					unInflateZone( oZone);
				}
			}
		}
		super.localDestroy();
	}
	
	/**
	 * @param p_oZone
	 */
	private void unInflateZone( ManagementZone p_oZone ) {
		int iIdSection=application.getAndroidIdByStringKey(ApplicationRGroup.ID,p_oZone.getSource());
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iIdSection);
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.unInflate(String.valueOf(this.sectionContainer.hashCode()), oZoneLayout, this.getParameters());

		for(ManagementZone oSubZone : p_oZone.getVisibleZones()) {
			this.unInflateZone(oSubZone);
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String,Object> r_oMap =  (Map<String, Object>) super.onRetainCustomNonConfigurationInstance();
		r_oMap.put(ROOTCOMPONENT_PARAMETER, this.sectionContainer.getParent());
		return r_oMap;
		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#inflateLayout()
	 */
	@Override
	public void inflateLayout() {
		//mise en place de l'auto mapping
		// 1 group = une page
		MMMasterRelativeLayout oZoneLayout = null;
		RealVisualComponentDescriptor oDescriptor = null;
		int iSectionId=0;

		for(ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {
			for(ManagementZone oZone : oGroup.getVisibleZones()) {
				iSectionId = this.application.getAndroidIdByStringKey(ApplicationRGroup.ID,oZone.getSource());
				oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iSectionId);
				if (oZoneLayout.getViewModel() != null && CustomizableViewModel.class
						.isAssignableFrom(oZoneLayout.getViewModel().getClass())) {

					oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
					oDescriptor.unInflateLayout(String.valueOf(this.sectionContainer.hashCode()), oZoneLayout, this.getParameters());
					oDescriptor.inflateLayout(String.valueOf(this.sectionContainer.hashCode()), oZoneLayout, this.getParameters(), null);
				}
			}
		}
	}
}
