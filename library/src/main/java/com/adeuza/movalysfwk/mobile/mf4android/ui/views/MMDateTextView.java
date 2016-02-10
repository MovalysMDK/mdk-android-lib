package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.util.AttributeSet;

public class MMDateTextView extends MMDateTimeTextView {

	public MMDateTextView(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		
		configuration=Mode.date;
		
	}

}
