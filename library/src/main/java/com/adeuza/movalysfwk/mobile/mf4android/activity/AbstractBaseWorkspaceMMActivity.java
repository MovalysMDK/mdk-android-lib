package com.adeuza.movalysfwk.mobile.mf4android.activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMMasterRelativeLayout;
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
 * Abstract base workspace activity
 * @param <WLAYOUT> base workspace layout
 * @author lmichenaud
 * @deprecated Replaced by {@link AbstractBaseWorkspaceMMFragmentActivity}
 */
public abstract class AbstractBaseWorkspaceMMActivity<WLAYOUT extends MMBaseWorkspaceLayout<?>> extends AbstractAutoBindMMActivity {

	/**
	 * Utilisé lorsque l'utilisateur vient d'effectuer un back.
	 */
	protected static final String FROM_BACK = "fromBack";
	
	/**
	 * Wlayout parameter name
	 */
	protected static final String ROOTCOMPONENT_PARAMETER = "wlayout";
	
	/**
	 * la configuration associée au workspace
	 */
	private ManagementConfiguration wconfiguration ;
	
	/**
	 * le workspace de la page
	 */
	private WLAYOUT wlayout ;
	
	/**
	 * do after set content view
	 * @param p_oLayout layout
	 */
	protected abstract void doAfterSetContentView( WLAYOUT p_oLayout );
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doAfterSetContentView()
	 */
	@Override
	protected void doAfterSetContentView() {
		super.doAfterSetContentView();

		//récupération du composant workspace		
		if ( this.wlayout == null ) {
			this.wlayout = (WLAYOUT) this.findViewById(this.getWorkspaceId());
		}
		
		this.doAfterSetContentView(this.wlayout);

		//récupération de la configuration du workspace
		this.wconfiguration = ConfigurationsHandler.getInstance().getManagementConfiguration(this.wlayout.getModel());
		
		//mise en place de l'auto mapping
		// 1 group = une page
		for(ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {
			for(ManagementZone oZone : oGroup.getVisibleZones()) {
				this.configureAutoBinding(oZone, this.getViewModel());
			}
		}
	}

	/**
	 * return l'identifiant android du composant workspace
	 * @return l'identifiant android du composant workspace
	 */
	public abstract int getWorkspaceId();
	
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
			this.wlayout = (WLAYOUT) oRootView.findViewById(this.getWorkspaceId());
			
			this.setContentView(oRootView);
			this.doAfterSetContentView();
			
			this.wlayout.init();
		}
		else {
			super.defineContentView();
		}
	}
	
	/**
	 * configure auto binding
	 * @param p_oZone management zone
	 * @param p_oVm view model
	 */
	private void configureAutoBinding( ManagementZone p_oZone, ViewModel p_oVm ) {
		int iSectionId = this.getAndroidApplication().getAndroidIdByStringKey(ApplicationRGroup.ID, p_oZone.getSource());
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iSectionId);
		if ( oZoneLayout == null ) {
			oZoneLayout = this.wlayout.getSectionLayoutByIdInHiddenColumns(iSectionId);
		}
	
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
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (IllegalArgumentException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (IllegalAccessException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (NoSuchMethodException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		} catch (InvocationTargetException e) {
			Log.e("MMWORKSPACE", "Erreur à l'affectation du viewmodel", e);
		}
		
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.inflate(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters());
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
	
	
	/** Nothing to do
	 * @param p_oSource clicked view
	 */
	protected abstract void doOnKeepWorkspaceModifications(View p_oSource);
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractAutoBindMMActivity#localDestroy()
	 */
	@Override
	public void localDestroy() {
		if ( this.wconfiguration != null ) {	
			for(ManagementGroup oGroup : this.wconfiguration.getVisibleGroups()) {
				for(ManagementZone oZone : oGroup.getVisibleZones()) {
					unInflateZone( oZone);
				}
			}
		}
		super.localDestroy();
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
				iSectionId = this.getAndroidApplication().getAndroidIdByStringKey(ApplicationRGroup.ID,oZone.getSource());
				oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iSectionId);
				if (oZoneLayout==null) {
					oZoneLayout=wlayout.getSectionLayoutByIdInHiddenColumns(iSectionId);
				}

				if (oZoneLayout.getViewModel() != null && CustomizableViewModel.class
						.isAssignableFrom(oZoneLayout.getViewModel().getClass())) {

					oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
					oDescriptor.unInflateLayout(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters());
					oDescriptor.inflateLayout(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters(), null);
				}
			}
		}
	}
	
	/**
	 * uninflate zone
	 * @param p_oZone management zone
	 */
	private void unInflateZone( ManagementZone p_oZone ) {
		int iIdSection= this.getAndroidApplication().getAndroidIdByStringKey(ApplicationRGroup.ID,p_oZone.getSource());
		MMMasterRelativeLayout oZoneLayout = (MMMasterRelativeLayout) this.findViewById(iIdSection);
		if (oZoneLayout==null) {
			oZoneLayout= wlayout.getSectionLayoutByIdInHiddenColumns(iIdSection);
		}
		RealVisualComponentDescriptor oDescriptor = VisualComponentDescriptorsHandler.getInstance().getRealDescriptor(oZoneLayout.getName());
		oDescriptor.unInflate(String.valueOf(this.wlayout.hashCode()), oZoneLayout, this.getParameters());

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
		r_oMap.put(ROOTCOMPONENT_PARAMETER, this.wlayout.getParent());
		return r_oMap;
	}

	/**
	 * GETTER
	 * @return W layout
	 */
	public WLAYOUT getWlayout() {
		return this.wlayout;
	}
}
