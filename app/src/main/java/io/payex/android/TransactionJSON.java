package io.payex.android;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by vince on 12/27/2016.
 */

public class TransactionJSON implements Serializable {
    public long TransactionId;
    public String CreateDate;
    public String Currency;
    public int Amount;
    public String CardNumber;
    public String CardBrand;
    public String MerchantTxnNumber;
    public int MerchantId;
    public int TransactionTypeId;

    @Override
    public String toString() {
        return TransactionId + ":" + CardBrand + ":" + CardNumber + ":" + Amount;
    }
}
