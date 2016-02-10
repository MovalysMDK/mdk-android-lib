package com.adeuza.movalysfwk.mobile.mf4android.ui.component;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMCheckBoxGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMDoublePickerGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEditText;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMEmailSimpleEditTextGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMNumberPickerGroup;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMURLEditViewGroup;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor;

/**
 * <p>Define the class used to view the custom field</p>
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 * @author emalespine
 */
public class UIComponentClassAccessorImpl implements UIComponentClassAccessor {
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getUILabelClass()
	 */
	@Override
	public String getUILabelClass() {
		return MMTextView.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getTextBoxClass()
	 */
	@Override
	public String getTextBoxClass() {
		return MMEditText.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getCheckBoxGroupClass()
	 */
	@Override
	public String getCheckBoxGroupClass() {
		return MMCheckBoxGroup.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getUrlClass()
	 */
	@Override
	public String getUrlClass() {
		return MMURLEditViewGroup.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getEmailClass()
	 */
	@Override
	public String getEmailClass() {
		return MMEmailSimpleEditTextGroup.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getIntegerClass()
	 */
	@Override
	public String getIntegerClass() {
		return MMNumberPickerGroup.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getDoubleClass()
	 */
	@Override
	public String getDoubleClass() {
		return MMDoublePickerGroup.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getDateClass()
	 */
	@Override
	public String getDateClass() {
		return MMEditText.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getDateTimeClass()
	 */
	@Override
	public String getDateTimeClass() {
		return MMEditText.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getTimeClass()
	 */
	@Override
	public String getTimeClass() {
		return MMEditText.class.getName();
	}
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.component.UIComponentClassAccessor#getDurationClass()
	 */
	@Override
	public String getDurationClass() {
		return MMEditText.class.getName();
	}
}