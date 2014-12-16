package com.spixsoftware.spixlibrary.tools.multipageasynctask;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

/**
 * Helps Load Item to list dynamically, by pages
 */
public abstract class MultiPageAsyncTask<Params, Result> implements AsyncTaskActionsProvider<Params, Result> {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int DEFAULT_LIMIT = 50;
	// ===========================================================
	// Fields
	// ===========================================================

	private int pageLimit = DEFAULT_LIMIT;
	private int offset;

	private boolean hasMoreData = true;

	private DynamicDataLoader<Params, Result> dataLoader;

	// ===========================================================
	// Constructors
	// ===========================================================
	public MultiPageAsyncTask() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPageLimit() {
		return pageLimit;
	}

	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public void onPreExecute() {
	}

	public abstract Result doInBackground(int offset, int pageLimit, @SuppressWarnings("unchecked") Params... params);

	public void onCancelled() {
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean loadMore(Params... params) {
		if ((dataLoader != null && dataLoader.getStatus() != Status.FINISHED) || !hasMoreData) {
			return false;
		}
		dataLoader = new DynamicDataLoader<Params, Result>(offset, pageLimit);
		dataLoader.asyncTaskActionsProvider = this;
		dataLoader.execute(params);
		return true;
	}

	public void cancel() {
		if (dataLoader != null) {
			dataLoader.cancel(true);
		}
	}

	public void reset() {
		cancel();
		this.offset = 0;
		this.hasMoreData = true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@SuppressWarnings("hiding")
	protected class DynamicDataLoader<Params, Result> extends AsyncTask<Params, Void, Result> {

		private AsyncTaskActionsProvider<Params, Result> asyncTaskActionsProvider;

		private final int offset;
		private final int pageLimit;

		public DynamicDataLoader(int offset, int pageLimit) {
			this.offset = offset;
			this.pageLimit = pageLimit;
		}

		@Override
		public void onPreExecute() {
			asyncTaskActionsProvider.onPreExecute();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Result doInBackground(Params... params) {
			return asyncTaskActionsProvider.doInBackground(offset, pageLimit, params);
		}

		@Override
		public void onPostExecute(Result result) {
			final int newOffset = asyncTaskActionsProvider.onPostExecute(result);
			if (MultiPageAsyncTask.this.offset + pageLimit != newOffset) {
				MultiPageAsyncTask.this.hasMoreData = false;
			}
			MultiPageAsyncTask.this.offset = newOffset;
		}

		@Override
		public void onCancelled() {
			asyncTaskActionsProvider.onCancelled();
		}

	}

}
