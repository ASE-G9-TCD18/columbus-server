package com.group9.columbus.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.group9.columbus.test.login.UserLoginSignupTest;
import com.group9.columbus.test.usermanagement.UserManagementControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    UserLoginSignupTest.class, 
    UserManagementControllerTest.class 
})
public class TestSuite {

}
