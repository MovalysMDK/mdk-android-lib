package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.configuration.entity.AbstractEntityFieldConfiguration;

public class MMDateEditText extends MMDateTimeEditText {

	public MMDateEditText(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		this.setDataType(AbstractEntityFieldConfiguration.DataType.TYPE_DATE);
	}

}
