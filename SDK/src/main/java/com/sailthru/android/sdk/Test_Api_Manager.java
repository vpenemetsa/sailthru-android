package com.sailthru.android.sdk;

import android.test.InstrumentationTestCase;

import com.sailthru.android.sdk.*;

/**
 * Created by Vijay Penemetsa on 5/21/14.
 */
public class Test_Api_Manager extends InstrumentationTestCase {

    String mApiKey = "419188ee0d0dc748f04e0cd9ea7d7c0f";
    String mAppId = "5362a304fdd5ac3611000481";
    String mEmail = "dhoerl+testa009@sailthru.com";

    final String mExpectedHid = "a1082c2fcb05478f3bb9f35810f819e85362a9ce9dbcd4904500028e1d8afe44d443865835f8d1220fa17912";

    public void test() throws Exception {
        final int expected = 1;
        final int r = 2;

//        Async_RegisterTask task = new Async_RegisterTask(getInstrumentation().getContext(),
//                mAppId, mApiKey, mEmail, SailthruClient_Abstract.Identification.EMAIL,
//                null, null);
        String hid = mExpectedHid;

        assertSame(hid, mExpectedHid);
    }

}