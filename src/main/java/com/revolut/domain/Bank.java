package com.revolut.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bank")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.revolut.domain.Bank.findAll",
                        query = "SELECT b FROM Bank b"
                ),
                @NamedQuery(
                        name="com.revolut.domain.Bank.findByName",
                        query="SELECT b from Bank b where name=:name"
                )
        })

public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String name;

    public Bank() { super();}

    public Bank(String name) {
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(name, bank.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Bank{" +
                ", name='" + name + '\'' +
                '}';
    }
}
