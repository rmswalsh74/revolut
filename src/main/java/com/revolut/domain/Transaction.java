package com.revolut.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.revolut.domain.Transaction.findAll",
                        query = "SELECT a FROM Transaction a"
                ),
                @NamedQuery(
                        name = "com.revolut.domain.Transaction.findByUuid",
                        query = "SELECT a FROM Transaction a where uuid=:uuid"
                )
        })
public class Transaction {
    /** ID is always set to the HASH of the Entity to preserve the uniqueness of the object*/
    @Id
    @Column(updatable = false)
    private Integer id;
    @Column(unique = true, nullable = false)
    private UUID uuid;
    @ManyToOne
    private Account from;
    @ManyToOne
    private Account to;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private TransactionType type;
    @Column(nullable = false)
    private TransactionStatus status;
    @Column(nullable = true)
    private String message;
    @Column(nullable = false)
    private Date dateAccepted;
    @Column(nullable = true)
    private Date dateCompleted;

    public Transaction(){}

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                ", type=" + type +
                ", status=" + status +
                ", message=" + message +
                ", date=" + dateAccepted +
                ", date=" + dateCompleted +
                '}';
    }

    /**
     * Each Transaction should be unique.
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                type == that.type &&
                Objects.equals(dateAccepted, that.dateAccepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, amount, type, dateAccepted);
    }

    public Transaction(UUID uuid, Account from, Account to, double amount, TransactionType type, TransactionStatus status, Date date) {
        this.uuid=uuid;
        this.from=from;
        this.to=to;
        this.amount=amount;
        this.type=type;
        this.status=status;
        this.dateAccepted=date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateAccepted() {
        return dateAccepted;
    }

    public void setDateAccepted(Date date) {
        this.dateAccepted = date;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
