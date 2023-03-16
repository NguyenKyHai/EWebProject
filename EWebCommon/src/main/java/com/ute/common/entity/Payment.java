package com.ute.common.entity;

import javax.persistence.*;

@Entity(name = "payment")
public class Payment {

    @Id
    private String vnpTxnRef;

    private String amount;

    @Column(name = "bank_code")
    private String vnpBankCode;

    @Column(name = "bank_tran_no")
    private String vnpBankTranNo;

    @Column(name = "card_type")
    private String vnpCardType;

    @Column(name = "order_info")
    private String vnpOrderInfo;

    @Column(name = "pay_date")
    private String vnpPayDate;

    @Column(name = "response_code")
    private String vnpResponseCode;

    @Column(name = "tmn_code")
    private String vnpTmnCode;

    @Column(name = "transaction_no")
    private String vnpTransactionNo;

    @Column(name = "transaction_status")
    private String vnpTransactionStatus;

    @Column(name = "secure_hash")
    private String vnpSecureHash;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Payment() {
    }

    public Payment(String vnpTxnRef) {
        this.vnpTxnRef = vnpTxnRef;
    }

    public String getVnpTxnRef() {
        return vnpTxnRef;
    }

    public void setVnpTxnRef(String vnpTxnRef) {
        this.vnpTxnRef = vnpTxnRef;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVnpBankCode() {
        return vnpBankCode;
    }

    public void setVnpBankCode(String vnpBankCode) {
        this.vnpBankCode = vnpBankCode;
    }

    public String getVnpBankTranNo() {
        return vnpBankTranNo;
    }

    public void setVnpBankTranNo(String vnpBankTranNo) {
        this.vnpBankTranNo = vnpBankTranNo;
    }

    public String getVnpCardType() {
        return vnpCardType;
    }

    public void setVnpCardType(String vnpCardType) {
        this.vnpCardType = vnpCardType;
    }

    public String getVnpOrderInfo() {
        return vnpOrderInfo;
    }

    public void setVnpOrderInfo(String vnpOrderInfo) {
        this.vnpOrderInfo = vnpOrderInfo;
    }

    public String getVnpPayDate() {
        return vnpPayDate;
    }

    public void setVnpPayDate(String vnpPayDate) {
        this.vnpPayDate = vnpPayDate;
    }

    public String getVnpResponseCode() {
        return vnpResponseCode;
    }

    public void setVnpResponseCode(String vnpResponseCode) {
        this.vnpResponseCode = vnpResponseCode;
    }

    public String getVnpTmnCode() {
        return vnpTmnCode;
    }

    public void setVnpTmnCode(String vnpTmnCode) {
        this.vnpTmnCode = vnpTmnCode;
    }

    public String getVnpTransactionNo() {
        return vnpTransactionNo;
    }

    public void setVnpTransactionNo(String vnpTransactionNo) {
        this.vnpTransactionNo = vnpTransactionNo;
    }

    public String getVnpTransactionStatus() {
        return vnpTransactionStatus;
    }

    public void setVnpTransactionStatus(String vnpTransactionStatus) {
        this.vnpTransactionStatus = vnpTransactionStatus;
    }

    public String getVnpSecureHash() {
        return vnpSecureHash;
    }

    public void setVnpSecureHash(String vnpSecureHash) {
        this.vnpSecureHash = vnpSecureHash;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
