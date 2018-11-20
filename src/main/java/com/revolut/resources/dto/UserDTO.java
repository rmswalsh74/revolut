package com.revolut.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    private String name;
    private String bankName;

    public UserDTO(){}

    public UserDTO(String name, String bankName){
        this.name=name;
        this.bankName=bankName;
    }
    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value = "bankName")
    public String getBankName() {
        return bankName;
    }

    @JsonProperty
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
