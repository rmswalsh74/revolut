package com.revolut.domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.revolut.domain.User.findAll",
                        query = "SELECT u FROM User u"
                ),
                @NamedQuery(
                        name = "com.revolut.domain.User.findByName",
                        query = "SELECT u FROM User u where name=:name"
                )
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @ManyToOne(optional = false)
    private Bank bank;
    @Column(nullable = false, unique = true)
    private String name;

    public User() { super();}

    public User(Bank bank, String name) {
        this.bank=bank;
        this.name=name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bank getBank() { return bank; }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(bank, user.bank) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bank, name);
    }

    @Override
    public String toString() {
        return "User{" +
                ", bank=" + bank +
                ", name='" + name + '\'' +
                '}';
    }
}
