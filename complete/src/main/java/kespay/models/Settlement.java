package kespay.models;

import kespay.enums.PaymentMethod;
import kespay.enums.PaymentStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="settlements")
public class Settlement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    private Date settlementDate;
    private String county;
    private String percent;
    private String totalAmount;
    private String amountPaid;
    private String serviceCharge;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String error_code1;
    private String error_msg1;
    private String error_code2;
    private String error_msg2;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getError_code1() {
        return error_code1;
    }

    public void setError_code1(String error_code1) {
        this.error_code1 = error_code1;
    }

    public String getError_msg1() {
        return error_msg1;
    }

    public void setError_msg1(String error_msg1) {
        this.error_msg1 = error_msg1;
    }

    public String getError_code2() {
        return error_code2;
    }

    public void setError_code2(String error_code2) {
        this.error_code2 = error_code2;
    }

    public String getError_msg2() {
        return error_msg2;
    }

    public void setError_msg2(String error_msg2) {
        this.error_msg2 = error_msg2;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
}
