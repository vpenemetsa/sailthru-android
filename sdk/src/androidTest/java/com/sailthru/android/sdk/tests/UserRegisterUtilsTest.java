package com.sailthru.android.sdk.tests;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiConstants;
import com.sailthru.android.sdk.impl.utils.UserRegisterUtils;

import java.util.Map;

/**
 * Created by Vijay Penemetsa on 5/21/14.
 */
public class UserRegisterUtilsTest extends InstrumentationTestCase {

    String mApiKey = "419188ee0d0dc748f04e0cd9ea7d7c0f";
    String mAppId = "5362a304fdd5ac3611000481";
    String mEmail = "dhoerl+testa009@sailthru.com";

    UserRegisterUtils userRegisterUtils;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        userRegisterUtils = new UserRegisterUtils();
    }

    public void testEmailRequest() throws Exception {
        final String expectedEmailSig = "f918b6f757126ff17ffed3d084a88ab4";

        Map<String, String> request = userRegisterUtils.buildRequest(
                getInstrumentation().getContext(), mAppId, mApiKey, mEmail,
                Sailthru.Identification.EMAIL);
        Log.d("*********^%$@#$%^&*^%$#%^&*(", request.get(ApiConstants.UR_SIG_KEY));
        assertEquals(expectedEmailSig, request.get(ApiConstants.UR_SIG_KEY));
    }

    public void testAnonymousRequest() throws Exception {
        final String expectedAnonSig = "01a42c081e8ab996d5f135eba9b0619";

        Map<String, String> request = userRegisterUtils.buildRequest(getInstrumentation().getContext(),
                mAppId, mApiKey, null, Sailthru.Identification.ANONYMOUS);
        assertEquals(expectedAnonSig, request.get(ApiConstants.UR_SIG_KEY));
    }

    public void testAnonymousToEmailTransitionRequest() throws Exception {
        final String expectedTransitionSig = "4719a6d01ade2d4f277e75e3706fbc43";

        Map<String, String> request = userRegisterUtils.buildRequest(getInstrumentation().getContext(),
                mAppId, mApiKey, "5d42324e438731906c9d238d30ad9da7537bb5aba256ab85110000e47e351477d51d7a5f21f17ac4e0c3927a",
                null);
        assertEquals(expectedTransitionSig, request.get(ApiConstants.UR_SIG_KEY));
    }


}