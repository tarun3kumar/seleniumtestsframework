package com.seleniumtests.dataobject;

/**
 * User account object for TestLink.
 */
public class User {

    private String userID;
    private String password;

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public void setUserID(final String userID) {
        this.userID = userID;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("User [UserId = " + userID + ", ").append("Password = " + password + "]")
                            .toString();
    }
}
