package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.view.View;

/**
 * MMPerformItemClickEventData
 * Class that contain data from a item click event
 * 
 */
public class MMPerformItemClickEventData {
	
	/** the tag */
	private String tag;
	/** the view */
	private View view;
	/** the position */
	private int position;
	/** the id */
	private long id ;
	/** the listview */
	private MMPerformItemClickView listView;
	
	/**
	 * The constructor of an event data
	 * @param p_sTag the tag
	 * @param p_oView the clicked view
	 * @param p_iPosition the view position in ListView
	 * @param p_iId the id of the view
	 * @param p_oListView the ListView
	 */
	public MMPerformItemClickEventData( String p_sTag, View p_oView, int p_iPosition, long p_iId,
			MMPerformItemClickView p_oListView) {
		super();
		this.tag = p_sTag;
		this.view = p_oView;
		this.position = p_iPosition;
		this.id = p_iId;
		this.listView = p_oListView;
	}
	
	/**
	 * perform the click on the listview
	 */
	public void performItemClick() {
		listView.superPerformItemClick(view, position, id);
	}

	/**
	 * Getter for the view
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * Getter for the position
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Getter for the id
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter for the ListView
	 * @return the ListView
	 */
	public MMPerformItemClickView getListView() {
		return listView;
	}

	/**
	 * Getter for the tag
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}
}