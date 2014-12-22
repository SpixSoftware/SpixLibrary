package com.spixsoftware.spixlibrary.tools.contentpreparer;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public abstract class ContentPreparer<ARGS, RESULT> {

    private final HashMap<String, ContentLoader<ARGS, RESULT>> contentLoaders = new HashMap<>();
    private final HashMap<String, RESULT> preparedContents = new HashMap<>();
    private final HashMap<String, AsyncTask<ARGS, Void, RESULT>> tasks = new HashMap<>();


    public RESULT getContent(String id, ContentLoader<ARGS, RESULT> contentLoader) {
        if (isContentPrepared(id)) {
            return preparedContents.get(id);
        }

        prepareContentAndWait(id, contentLoader);

        RESULT preparedConent = preparedContents.get(id);
        clearContent(id);
        return preparedConent;
    }

    protected boolean isContentPrepared(String id) {
        return preparedContents.containsKey(id);
    }

    protected void addPreparedContent(String id, RESULT content) {
        preparedContents.put(id, content);
    }

    protected void clearContent(String id) {
        contentLoaders.remove(id);
        preparedContents.remove(id);
        tasks.remove(id);
    }


    public void prepareContentAndWait(String id, ContentLoader<ARGS, RESULT> contentLoader) {
        prepareContent(id, contentLoader, true);
    }

    public void prepareContent(String id, ContentLoader<ARGS, RESULT> contentLoader) {
        prepareContent(id, contentLoader, false);
    }

    protected void prepareContent(final String id, ContentLoader<ARGS, RESULT> contentLoader, boolean wait) {
        if (isContentPrepared(id)) {
            throw new IllegalArgumentException("Content already prepared");
        }

        OnContentLoadedListener listener = new OnContentLoadedListener<RESULT>() {
            @Override
            public void onContentLoaded(final RESULT result) {
                addPreparedContent(id, result);
            }
        };

        ContentLoadingTask<ARGS, RESULT> task = new ContentLoadingTask<>(id, contentLoader, listener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (wait) {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    protected class ContentLoadingTask<ARGS, RESULT> extends AsyncTask<ARGS, Void, RESULT> {

        private final OnContentLoadedListener loadedListener;
        private final ContentLoader<ARGS, RESULT> loader;
        private final String id;

        public ContentLoadingTask(String id, ContentLoader<ARGS, RESULT> loader, OnContentLoadedListener loadedListener) {
            this.loader = loader;
            this.id = id;
            this.loadedListener = loadedListener;
        }

        @Override
        protected RESULT doInBackground(ARGS... params) {
            return loader.loadContent(params);
        }

        @Override
        protected void onPostExecute(RESULT result) {
            loadedListener.onContentLoaded(result);
        }
    }

    protected interface OnContentLoadedListener<RESULT> {
        public void onContentLoaded(RESULT result);
    }

}
