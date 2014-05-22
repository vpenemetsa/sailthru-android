package com.sailthru.android.sdk;

import android.test.InstrumentationTestCase;

import java.util.Map;

/**
 * Created by Vijay Penemetsa on 5/21/14.
 */
public class TEST_AppRegister extends InstrumentationTestCase {

    String mApiKey = "419188ee0d0dc748f04e0cd9ea7d7c0f";
    String mAppId = "5362a304fdd5ac3611000481";
    String mEmail = "dhoerl+testa009@sailthru.com";

    final String mExpectedHid = "a1082c2fcb05478f3bb9f35810f819e85362a9ce9dbcd4904500028e1d8afe44d443865835f8d1220fa17912";

    public void testEmailRequest() throws Exception {
        final String expectedEmailSig = "073fcf3bb3a4fdea9b3025d2a01ffae3";

        Map<String, String> request = UTILS_AppRegister.buildRequest(
                getInstrumentation().getContext(), mAppId, mApiKey, mEmail,
                SailthruClient_Abstract.Identification.EMAIL);

        assertEquals(request.get(API_Constants.UR_SIG_KEY), expectedEmailSig);
    }

    public void testAnonymousRequest() throws Exception {
        final String expectedAnonSig = "841e1c12bec027135e5a73d813a88140";

        Map<String, String> request = UTILS_AppRegister.buildRequest(getInstrumentation().getContext(),
                mAppId, mApiKey, null, SailthruClient_Abstract.Identification.ANONYMOUS);
        assertEquals(request.get(API_Constants.UR_SIG_KEY), expectedAnonSig);
    }

    public void testAnonymousToEmailTransitionRequest() throws Exception {
        final String expectedTransitionSig = "370ab25b550aeb5bb4c2f54f6ef06505";

        Map<String, String> request = UTILS_AppRegister.buildRequest(getInstrumentation().getContext(),
                mAppId, mApiKey, "5d42324e438731906c9d238d30ad9da7537bb5aba256ab85110000e47e351477d51d7a5f21f17ac4e0c3927a",
                null);
        assertEquals(request.get(API_Constants.UR_SIG_KEY), expectedTransitionSig);
    }

}