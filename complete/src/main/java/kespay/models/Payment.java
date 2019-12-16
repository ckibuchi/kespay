package kespay.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import kespay.enums.PaymentMethod;
import kespay.enums.PaymentStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Entity
@Table(name="payments")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String msisdn;
    private String amount;
    private String county;
    private String subCounty;
    private String parkingLot;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String refID;
    private String error_code1;
    private String error_code2;
    private String desc1;
    private String desc2;
    private String startTime;
    private String endTime;
    private Date paymentDate;
    private String carRegNo;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(String parkingLot) {
        this.parkingLot = parkingLot;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public String getError_code1() {
        return error_code1;
    }

    public void setError_code1(String error_code1) {
        this.error_code1 = error_code1;
    }

    public String getError_code2() {
        return error_code2;
    }

    public void setError_code2(String error_code2) {
        this.error_code2 = error_code2;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
