package com.adeuza.movalysfwk.mobile.mf4android.ui.component.descriptor;

import java.util.ArrayList;
import java.util.List;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMLinearLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTableLayout;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.configurable.ConfigurableVisualComponent;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.descriptor.VisualComponentDescriptorHelper;

/**
 * <p>Helper for visal component</p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author smaitre
 *
 */

public class AndroidVisualComponentDescriptorHelperImpl implements VisualComponentDescriptorHelper {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getLayoutComponentNames() {
		List<String> r_oLstComponents = new ArrayList<>();
		r_oLstComponents.add(ConfigurableVisualComponent.TYPE_MASTER);
		r_oLstComponents.add(MMLinearLayout.class.getName());
		r_oLstComponents.add(MMTableLayout.class.getName());
		
		r_oLstComponents.add(LinearLayout.class.getSimpleName());
		r_oLstComponents.add(TableLayout.class.getSimpleName());
		r_oLstComponents.add(FrameLayout.class.getSimpleName());
		return r_oLstComponents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getReducibleComponentNames() {
		List<String> r_oLstComponents = new ArrayList<>();
		r_oLstComponents.add(TabHost.class.getSimpleName());
		r_oLstComponents.add(TabWidget.class.getSimpleName());
		r_oLstComponents.add(ScrollView.class.getSimpleName());
		return r_oLstComponents;
	}

}
