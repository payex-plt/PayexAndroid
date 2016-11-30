package io.payex.android;

/**
 * Created by vince on 11/28/2016.
 */

public class Merchant {
    private String merchantId;
    private String accessCode;
    private String hashSecret;
    private String voidUser;
    private String voidPassword;

    public Merchant(String merchantId, String voidUser, String voidPassword, String accessCode, String hashSecret) {
        this.accessCode = accessCode;
        this.hashSecret = hashSecret;
        this.merchantId = merchantId;
        this.voidPassword = voidPassword;
        this.voidUser = voidUser;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getVoidPassword() {
        return voidPassword;
    }

    public String getVoidUser() {
        return voidUser;
    }
}
