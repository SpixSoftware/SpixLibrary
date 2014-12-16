package com.spixsoftware.spixlibrary.tools.multipageasynctask;

public interface AsyncTaskActionsProvider<Params, Result> {
	public void onPreExecute();

	public Result doInBackground(int offset, int pageLimit, Params... params);

	public int onPostExecute(Result result);

	public void onCancelled();
}
