package com.greengrowapps.ggarest.authorization;

public class CredentialsImpl implements Credentials {

    private final String username;
    private final String password;

    public CredentialsImpl(String username, String password){
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
