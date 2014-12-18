package com.spixsoftware.spixlibrary.tools.jobservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JobServiceManager {

    private Context context;

    public JobServiceManager(Context context) {
        this.context = context;
    }

    public void notifyJobHasBenAdded(Class<? extends BaseJobService> jobServiceClass) {
        if (isServiceRunning(jobServiceClass)) {
            callNotifyMethodInJobService(jobServiceClass);
        } else {
            startService(jobServiceClass);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void callNotifyMethodInJobService(Class<? extends BaseJobService> jobService) {
        Method m = null;
        try {
            m = jobService.getMethod("notifyAboutNewJob");
            Object result = m.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void startService(Class<? extends BaseJobService> jobServiceClassToStart) {
        Intent startServiceIntent = new Intent(context, jobServiceClassToStart);
        context.startService(startServiceIntent);
    }


}
