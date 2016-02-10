package com.adeuza.movalysfwk.mobile.mf4android.fragment;

import java.io.Serializable;

import android.view.ViewGroup;

import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractWorkspaceMasterDetailMMFragmentActivity;
import com.adeuza.movalysfwk.mobile.mf4android.ui.modele.MMListAdapter;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMAdaptableListView;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMPerformItemClickEventData;
import com.adeuza.movalysfwk.mobile.mf4javacommons.event.BusinessEvent;

/**
 * Class to show workspace list panel
 * @author abelliard
 *
 */
public abstract class AbstractMMWorkspaceListFragment extends AbstractMMListFragment {

	/** workspace list handler */
	private WorkspaceListHandler mWorkspaceHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doAfterInflate(ViewGroup p_oRoot) {
		super.doAfterInflate(p_oRoot);
		
		this.mWorkspaceHandler = new WorkspaceListHandler(this.getListViewId(), this.mListAdapter);
		
		this.mWorkspaceHandler.setView( (MMAdaptableListView) p_oRoot.findViewById(getListViewId()) );
		
	}

	/**
	 * Get the workspace list handler
	 * @return the workspace list handler
	 */
	public WorkspaceListHandler getWorkspaceListHandler() {
		return this.mWorkspaceHandler;
	}
	
	/**
	 * @author lmichenaud
	 *
	 */
	public class WorkspaceListHandler implements Serializable {

		private static final String TAG = "WorkspaceListHandler";
		/**
		 * 
		 */
		private static final long serialVersionUID = -599197075611819784L;

		/**
		 * List view id
		 */
		private int resourceId ;

		/**
		 * Adapter
		 */
		private MMListAdapter adapter = null;

		/**
		 * List view
		 */
		private MMAdaptableListView view = null;

		/**
		 * @param p_iResourceId
		 */
		public WorkspaceListHandler(int p_iResourceId, MMListAdapter p_oAdapter) {
			this.resourceId = p_iResourceId ;
			this.adapter = p_oAdapter ;
		}

		/**
		 * @param p_oListView
		 */
		public void setView(MMAdaptableListView p_oView) {
			this.view = p_oView;
		}

		/**
		 * @return
		 */
		public int getResourceId() {
			return this.resourceId;
		}

		/**
		 * @return
		 */
		public MMListAdapter getAdapter() {
			return this.adapter;
		}
		
		/**
		 * @return
		 */
		public MMAdaptableListView getView() {
			return this.view;
		}
	}

	public void doOnMasterListChangeSelectedItem(
			WorkspaceListHandler p_oWorkspaceListHandler,
			BusinessEvent<MMPerformItemClickEventData> p_oBusinessEvent) {
		if ( this.getActivity() != null ) {
			((AbstractWorkspaceMasterDetailMMFragmentActivity) this.getActivity()).doOnMasterListChangeSelectedItem(p_oWorkspaceListHandler, p_oBusinessEvent);
		}
	}
	
}
