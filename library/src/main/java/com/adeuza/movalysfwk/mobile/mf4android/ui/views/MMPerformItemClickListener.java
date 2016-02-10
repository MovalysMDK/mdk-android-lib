package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.view.View;

public interface MMPerformItemClickListener {
	
	public boolean onPerformItemClick( View p_oView, int p_iPosition, long p_iId, MMPerformItemClickView p_oListView );
}