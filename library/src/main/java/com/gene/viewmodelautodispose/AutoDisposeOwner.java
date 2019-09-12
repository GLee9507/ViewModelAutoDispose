package com.gene.viewmodelautodispose;

import androidx.collection.ArraySet;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public interface AutoDisposeOwner {

    AtomicBoolean cleared = new AtomicBoolean(false);

    Set<AutoDispose.Job> jobs = new ArraySet<>();

    default void addJob(AutoDispose.Job job) {
        synchronized (this) {
            if (cleared.get()) {
                job.close();
                return;
            }
            jobs.add(job);
        }
    }

    default void removeJob(AutoDispose.Job job) {
        synchronized (this) {
            if (job != null) {
                if (jobs.remove(job)) {
                    job.close();
                }
            }
        }
    }

    default void clearJobs() {
        cleared.set(true);
        synchronized (this) {
            for (AutoDispose.Job job : jobs) {
                job.close();
            }
            jobs.clear();
        }
    }

}
