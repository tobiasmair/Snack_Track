package edu.mci.snacktrack.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue
    private final UUID userId;

    private String username;
    private String password;

    public User(String username, String password){
        this.userId = UUID.randomUUID();
        setUsername(username);
        setPassword(password);
    }

    // getter & setter
    public UUID getUserId(){
        return userId;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
