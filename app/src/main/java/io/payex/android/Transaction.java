package io.payex.android;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by vince on 12/23/2016.
 */

public class Transaction {
    private long transactionId;
    private Timestamp createDate;
    private String currency;
    private int amount;
    private String cardNumber;
    private String cardBrand;
    private String merchantTxnNumber;
    private int merchantId;
    private int transactionTypeId;

    public Transaction() {}

    public Transaction(int amount, String cardBrand, String cardNumber, Timestamp createDate, String currency, int merchantId, String merchantTxnNumber, long transactionId, int transactionTypeId) {
        this.amount = amount;
        this.cardBrand = cardBrand;
        this.cardNumber = cardNumber;
        this.createDate = createDate;
        this.currency = currency;
        this.merchantId = merchantId;
        this.merchantTxnNumber = merchantTxnNumber;
        this.transactionId = transactionId;
        this.transactionTypeId = transactionTypeId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @SerializedName("CardBrand")
    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantTxnNumber() {
        return merchantTxnNumber;
    }

    public void setMerchantTxnNumber(String merchantTxnNumber) {
        this.merchantTxnNumber = merchantTxnNumber;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(int transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
}
