package com.revolut.domain;

import com.revolut.util.Utils;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Table(name = "account")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.revolut.domain.Account.findAll",
                        query = "SELECT a FROM Account a"
                ),
                @NamedQuery(
                        name="com.revolut.domain.Account.findByAccountNumber",
                        query = "SELECT a FROM Account a where accountNumber=:accountNumber"
                )
        })
public class Account {
    @Transient
    private final Lock accountLock=new ReentrantLock();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Column(nullable = false, unique = true)
    private Integer accountNumber;
    @Column(nullable = false)
    private String accountName;
    @Column(nullable = false, unique = true)
    @Pattern(regexp = Utils.SORT_CODE_REG_EX,
            message = "invalid sort code")
    private String sortCode;
    @Column(nullable = false)
    private Currency currency;
    @ManyToOne(optional = false)
    private User owner;
    @Column(nullable = false)
    private Date creationDate;
    private Date closedDate;
    @Column(nullable = false)
    private Double balance=0d;
    @Column(nullable = true)
    private Double available;

    public Account() { super();}

    public Account(Integer accountNumber, String accountName, String sortCode, Currency currency, User owner) {
        this.accountNumber=accountNumber;
        this.accountName=accountName;
        this.sortCode=sortCode;
        this.currency=currency;
        this.owner=owner;
        this.creationDate=new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountNumber() { return accountNumber; }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() { return accountName; }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSortCode() { return sortCode; }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public User getOwner() { return owner; }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getCreationDate() { return creationDate; }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getClosedDate() { return closedDate; }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Double getBalance() { return balance; }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getAvailable() { return available; }

    public void setAvailable(Double available) {
        this.available = available;
    }

    public void closeAccount(Date closedDate) {
        closedDate=closedDate;
    }

    public boolean isClosed() { return closedDate!=null; }

    public boolean isOpen() { return closedDate==null; }

    public AccountState getAccountState() {
        accountLock.lock();
        AccountState state=null;
        if (isClosed()) state=AccountState.IS_CLOSED;
        if (balance> 0.0)
            state=AccountState.IN_CREDIT;
         else
            state=AccountState.IN_DEBIT;
         return state;
    }

    public AccountState credit(double depositAmount) {
        accountLock.lock();
        if (isClosed()) return AccountState.IS_CLOSED;
        if (depositAmount > 0.0) // is the depositAmount is valid
            balance = balance + depositAmount;
        accountLock.unlock();
        return AccountState.IN_CREDIT;
    }

    /**
     * Withdraw only if the amount is positive and there are available funds.
     *
     * @param amount
     */
    public FundState debit(double amount) {
        accountLock.lock();
        FundState state=null;
        if (balance==0)
            state=FundState.NO_FUNDS_AVAILABLE;
        else if (amount > 0.0 && balance-amount>=0 && isOpen()) {
            balance = balance - amount;
            state=FundState.FUNDS_AVAILABLE;
        } else {
            state=FundState.NOT_ENOUGH_FUNDS_AVIAILABLE;
        }
        accountLock.unlock();
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountName='" + accountName + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", currency=" + currency +
                ", owner=" + owner +
                ", creationDate=" + creationDate +
                ", closedDate=" + closedDate +
                ", balance=" + balance +
                ", available=" + available +
                '}';
    }
}
