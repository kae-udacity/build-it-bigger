package com.udacity.gradle.builditbigger;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kenneth on 03/01/2018.
 */

public class EndpointsAsyncTaskIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;
    private AtomicBoolean idleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return idleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    /**
     * Sets the new idle state, if idleNow is true, it pings the {@link ResourceCallback}.
     * @param idleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean idleNow) {
        this.idleNow.set(idleNow);
        if (idleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}
