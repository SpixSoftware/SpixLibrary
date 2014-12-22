package com.spixsoftware.spixlibrary.tools.contentpreparer;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ContentPreparer<ARGS, RESULT> {

    private final HashMap<String, ContentLoader<ARGS, RESULT>> contentLoaders = new HashMap<>();
    private final HashMap<String, RESULT> preparedContents = new HashMap<>();
    private final HashMap<String, ContentLoadingTask<ARGS, RESULT>> runningTasks = new HashMap<>();


    public RESULT getContent(String id, ContentLoader<ARGS, RESULT> contentLoader) {
        if (isContentPrepared(id)) {
            return getAndClearPreparedContent(id);
        }

        if (isContentLoading(id)) {
            waitForContentToLoad(id);
            return getAndClearPreparedContent(id);
        }

        prepareContentAndWait(id, contentLoader);

        return getAndClearPreparedContent(id);
    }

    protected RESULT getAndClearPreparedContent(String id) {
        RESULT preparedContent = preparedContents.get(id);
        clearContent(id);
        return preparedContent;
    }

    protected boolean isContentLoading(String id) {
        return runningTasks.containsKey(id);
    }

    protected void waitForContentToLoad(String id) {
        try {
            ContentLoadingTask<ARGS, RESULT> task = runningTasks.get(id);
            task.onPostExecute(task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
    }


    public void prepareContentAndWait(String id, ContentLoader<ARGS, RESULT> contentLoader) {
        prepareContent(id, contentLoader, true);
    }

    public void prepareContent(String id, ContentLoader<ARGS, RESULT> contentLoader) {
        prepareContent(id, contentLoader, false);
    }


    /**
     * @throws IllegalArgumentException() - Content already prepared
     */
    protected void prepareContent(final String id, ContentLoader<ARGS, RESULT> contentLoader, boolean wait) throws IllegalArgumentException {
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
        runningTasks.put(id, task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        if (wait) {
           waitForContentToLoad(id);
        }
    }

    protected class ContentLoadingTask<ARGS, RESULT> extends AsyncTask<ARGS, Void, RESULT> {

        private final OnContentLoadedListener loadedListener;
        private final ContentLoader<ARGS, RESULT> loader;
        private final String id;
        private ARGS args;

        public ContentLoadingTask(String id, ContentLoader<ARGS, RESULT> loader, OnContentLoadedListener loadedListener) {
            this.loader = loader;
            this.id = id;
            this.loadedListener = loadedListener;
        }

        @Override
        protected void onPreExecute() {
            args = loader.prepareArguments();
        }

        @Override
        protected RESULT doInBackground(ARGS... params) {
            return loader.loadContent(args);
        }

        @Override
        protected void onPostExecute(RESULT result) {
            runningTasks.remove(id);
            loadedListener.onContentLoaded(result);
        }
    }

    public static String ezFormat(Object... args) {
        String format = new String(new char[args.length])
                .replace("\0", "[ %s ]");
        return String.format(format, args);
    }

    protected interface OnContentLoadedListener<RESULT> {
        public void onContentLoaded(RESULT result);
    }

}
