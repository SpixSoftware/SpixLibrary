package com.spixsoftware.spixlibrary.tools.jobservice;

import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseJobService extends IntentService {

    // ===========================================================
    // Constants
    // ===========================================================
    public static final String SERVICE_NAME = BaseJobService.class.getSimpleName();
    // ===========================================================
    // Fields
    // ===========================================================
    protected Collection<? extends Job> loadedJobs;

    private static AtomicBoolean notifiedWhileJobsHasRun = new AtomicBoolean(false);
    // ===========================================================
    // Constructors
    // ===========================================================

    public BaseJobService() {
        super(SERVICE_NAME);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onHandleIntent(Intent intent) {

        prepareJobs();

        runJobs();

        //running jobs again because there is new ones
        if (notifiedWhileJobsHasRun.get()){
            notifiedWhileJobsHasRun.set(false);
            onHandleIntent(intent);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        notifiedWhileJobsHasRun.set(false);
    }
    // ===========================================================
    // Methods
    // ===========================================================

    // Not rename it because it is used via reflection in #net.dycode.eorder2.service.jobservice.JobServiceManager
    protected static void notifyAboutNewJob() {
        notifiedWhileJobsHasRun.set(true);
    }

    protected void prepareJobs() {
        loadedJobs = loadJobs();
        if (loadedJobs == null) {
            loadedJobs = new ArrayList<>(0);
        }
    }

    protected void runJobs() {
        for (Job jobToRun : loadedJobs) {
            onJobStarting(jobToRun);
        }
    }

    protected void onJobStarting(Job jobToBeRunned) {
        jobToBeRunned.onJobStarted();
    }

    public abstract Collection<? extends Job> loadJobs();


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
