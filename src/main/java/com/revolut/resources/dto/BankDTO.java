package com.revolut.resources.dto;

public class BankDTO {
    String name;

    public BankDTO(){}

    public BankDTO(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
