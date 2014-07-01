package com.sailthru.android.sdk.impl.tests;

import android.test.InstrumentationTestCase;

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
        final String expectedEmailSig = "100e4efcf26e19d4928b7cbb975b9ad2";

        Map<String, String> request = userRegisterUtils.buildRequest(
                getInstrumentation().getContext(), mAppId, mApiKey, mEmail,
                Sailthru.Identification.EMAIL);

        assertEquals(request.get(ApiConstants.UR_SIG_KEY), expectedEmailSig);
    }

    public void testAnonymousRequest() throws Exception {
        final String expectedAnonSig = "1ee148c342cffcdd135c175791c2b3e8";

        Map<String, String> request = userRegisterUtils.buildRequest(getInstrumentation().getContext(),
                mAppId, mApiKey, null, Sailthru.Identification.ANONYMOUS);
        assertEquals(request.get(ApiConstants.UR_SIG_KEY), expectedAnonSig);
    }

    public void testAnonymousToEmailTransitionRequest() throws Exception {
        final String expectedTransitionSig = "95aafeff5e394448e2568495f3d27b6d";

        Map<String, String> request = userRegisterUtils.buildRequest(getInstrumentation().getContext(),
                mAppId, mApiKey, "5d42324e438731906c9d238d30ad9da7537bb5aba256ab85110000e47e351477d51d7a5f21f17ac4e0c3927a",
                null);
        assertEquals(request.get(ApiConstants.UR_SIG_KEY), expectedTransitionSig);
    }


}