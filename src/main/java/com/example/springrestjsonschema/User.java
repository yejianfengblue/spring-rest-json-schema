package com.example.springrestjsonschema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue
    Long id;

    @Getter
    String username;

    String password;

    @Getter
    String status;

    @JsonCreator
    public User(@JsonProperty String username, @JsonProperty String password) {

        this.username = username;
        this.password = password;
    }

    @JsonIgnore
    String getPassword() {
        return password;
    }

    @JsonProperty
    void setPassword(String password) {
        this.password = password;
    }
}
