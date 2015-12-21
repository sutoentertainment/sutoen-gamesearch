package com.sutoen.g2adeal;

import android.Manifest;
import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    private static final boolean DEBUG = true;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    public void test_InternetPermission () {
        final String PKG = "com.sutoen.snowball";
        final String ACTIVITY =  PKG + ".MainActivity";
        final String PERMISSION = Manifest.permission.INTERNET;
        assertActivityRequiresPermission(PKG, ACTIVITY, PERMISSION);
    }
}