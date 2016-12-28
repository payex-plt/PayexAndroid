package io.payex.android.ui.sale.voided;

import java.io.Serializable;

/**
 * Created by vince on 12/28/2016.
 */

public class VoidObject implements Serializable {
    private String mId;
    private String mCardPAN;
    private String mCreateDate;
    private String mAmount;
    private String mTxnNum;
    private String mApprovalCode;
    private boolean mPaymentVoided;

    public VoidObject(String mId, String mAmount, String mCardPAN, String mCreateDate, String mTxnNum, String mApprovalCode, boolean mPaymentVoided) {
        this.mAmount = mAmount;
        this.mApprovalCode = mApprovalCode;
        this.mCardPAN = mCardPAN;
        this.mCreateDate = mCreateDate;
        this.mId = mId;
        this.mPaymentVoided = mPaymentVoided;
        this.mTxnNum = mTxnNum;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getApprovalCode() {
        return mApprovalCode;
    }

    public String getCardPAN() {
        return mCardPAN;
    }

    public String getCreateDate() {
        return mCreateDate;
    }

    public String getId() {
        return mId;
    }

    public boolean isPaymentVoided() {
        return mPaymentVoided;
    }

    public String getTxnNum() {
        return mTxnNum;
    }
}
