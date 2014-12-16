package com.spixsoftware.spixlibrary.tools;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * class helps you to listen when there is time to load more data to list Set list OnScrolListener this class. Or call
 * {@link #onScrollStateChanged(android.widget.AbsListView, int)} method in your own listener - it is helpfull when you are already using OnScrolListener for some other reasons
 */
public class ListItemsAppedListener implements OnScrollListener {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	/**
	 * How much items must left to fire listener. 20 - by default
	 */
	private int threshold = 20;
	private onScrollListLoadMoreDataListener onMoreItemsToAddHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setOnMoreItemsToAddHandler(onScrollListLoadMoreDataListener onMoreItemsToAddHandler) {
		this.onMoreItemsToAddHandler = onMoreItemsToAddHandler;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (((firstVisibleItem + visibleItemCount) >= (totalItemCount - threshold))) {
			fireOnMoreItemsToAddHandler();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	private void fireOnMoreItemsToAddHandler() {
		if (onMoreItemsToAddHandler != null) {
			onMoreItemsToAddHandler.onScrollListLoadMoreData();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface onScrollListLoadMoreDataListener {

		public void onScrollListLoadMoreData();
	}

}
