package com.adeuza.movalysfwk.mobile.mf4android.test;

/**
 * Created by sbernardin on 24/06/15.
 */
public class WaitHelper {

    public static void waitForMillis(final long millis) {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + millis;

        while (System.currentTimeMillis() < endTime);
    }

}
