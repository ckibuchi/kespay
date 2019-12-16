package kespay.models;


import kespay.enums.PayBillStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="churches")
public class Church implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String altName;
    private String payBill;
    @Enumerated(EnumType.STRING)
    PayBillStatus status;
    private Date creationDate;
    private Date lastRenewed;
    private Date expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    public String getPayBill() {
        return payBill;
    }

    public void setPayBill(String payBill) {
        this.payBill = payBill;
    }

    public PayBillStatus getStatus() {
        return status;
    }

    public void setStatus(PayBillStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastRenewed() {
        return lastRenewed;
    }

    public void setLastRenewed(Date lastRenewed) {
        this.lastRenewed = lastRenewed;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
