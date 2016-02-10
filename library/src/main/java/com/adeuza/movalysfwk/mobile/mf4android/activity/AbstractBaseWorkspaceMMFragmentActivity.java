package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMBaseWorkspaceFragmentLayout;
import com.adeuza.movalysfwk.mobile.mf4android.ui.wrappers.MMWorkspaceAdapter;

/**
 * The base activity to support fragment workspace
 * @author abelliard
 * @param <WLAYOUT>
 */
public abstract class AbstractBaseWorkspaceMMFragmentActivity<WLAYOUT extends MMBaseWorkspaceFragmentLayout<?>> extends AbstractAutoBindMMActivity {

	/**
	 * Utilisé lorsque l'utilisateur vient d'effectuer un back.
	 */
	protected static final String FROM_BACK = "fromBack";
	
	/**
	 * Wlayout parameter name
	 */
	protected static final String ROOTCOMPONENT_PARAMETER = "wlayout";
	
	/**
	 * the screen workspace
	 */
	private WLAYOUT wlayout ;
	
	/**
	 * Workspace adapter
	 */
	protected MMWorkspaceAdapter wadapter;
	
	/**
	 * {@inheritDoc}
	 * @see com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity#doAfterSetContentView()
	 */
	@Override
	protected void doAfterSetContentView() {
		super.doAfterSetContentView();

		//Retrieving workspace component
		if ( this.wlayout == null ) {
			this.wlayout = (WLAYOUT) this.findViewById(this.getWorkspaceId());
			
			//Retrieving or creating the adapter and set it to the workspace
			if (this.getWadapter() == null && this.wlayout.getAdapter() == null) {
				this.wadapter = this.createWorkspaceAdapter();
				this.wadapter.hideAllColumns();
				this.wlayout.setAdapter(this.wadapter);
			} else {
				this.wadapter = (MMWorkspaceAdapter) this.wlayout.getAdapter();
				this.wadapter.hideAllColumns();
			}
			
			//Retrieving the PageTransformer and setting it to the workspace component if exists
			PageTransformer transformer = this.createPageTransformer();
			if (transformer != null) {
				this.wlayout.setPageTransformer(true, transformer);
			}
		}
	}
	
	/**
	 * return the new page transformer
	 * if this method return null there is no page transformer
	 * @return the PageTransformer
	 */
	protected abstract PageTransformer createPageTransformer();

	/**
	 * return l'identifiant android du composant workspace
	 * @return l'identifiant android du composant workspace
	 */
	public abstract int getWorkspaceId();
	
	/**
	 * Create the workspace adapter
	 * @return workspace adapter
	 */
	protected abstract MMWorkspaceAdapter createWorkspaceAdapter();
	
	/**
	 * Get the Workspace adapter
	 * @return workspace adapter
	 */
	public MMWorkspaceAdapter getWadapter() {
		return this.wadapter;
	}

	/**
	 * Get the workspace layout
	 * @return the workspace layout
	 */
	public WLAYOUT getWlayout() {
		return this.wlayout;
	}


	/**
	 * Nothing to do
	 * @param p_oSource clicked view
	 */
	protected abstract void doOnKeepWorkspaceModifications(View p_oSource);
	
	
	
	/**
	 * A default PageTransformer
	 * @author abelliard
	 *
	 */
	public class DepthPageTransformer implements ViewPager.PageTransformer {
		/** the minimum scale of a page */
	    private static final float MIN_SCALE = 0.75f;

	    /**
	     * Called to transform the current pages
	     * @param p_oView view
	     * @param p_oPosition float position
	     */
	    @Override
		public void transformPage(View p_oView, float p_oPosition) {
	        int pageWidth = p_oView.getWidth();

	        if (p_oPosition < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            p_oView.setAlpha(0);

	        } else if (p_oPosition <= 0) { // [-1,0]
	            // Use the default slide transition when moving to the left page
	            p_oView.setAlpha(1);
	            p_oView.setTranslationX(0);
	            p_oView.setScaleX(1);
	            p_oView.setScaleY(1);

	        } else if (p_oPosition <= 1) { // (0,1]
	            // Fade the page out.
	            p_oView.setAlpha(1 - p_oPosition);

	            // Counteract the default slide transition
	            p_oView.setTranslationX(pageWidth * -p_oPosition);

	            // Scale the page down (between MIN_SCALE and 1)
	            float scaleFactor = MIN_SCALE
	                    + (1 - MIN_SCALE) * (1 - Math.abs(p_oPosition));
	            p_oView.setScaleX(scaleFactor);
	            p_oView.setScaleY(scaleFactor);

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            p_oView.setAlpha(0);
	        }
	    }
	}
}
