package com.app.todos.domain.users;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(BigInteger id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User updateDetails(User userUpdated) {
        setEmail(userUpdated.getEmail());
        setName(userUpdated.getName());
        setPassword(userUpdated.getPassword());
        return this;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
