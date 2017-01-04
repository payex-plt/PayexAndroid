package io.payex.android.util;

import java.util.List;

import io.payex.android.Transaction;
import io.payex.android.TransactionJSON;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vince on 11/28/2016.
 */

public interface PayexAPI {
    @GET("/api/merchantsapi/authenticate/{BIN}/{MID2P}/{password}")
    Call<Integer> authenticate(@Path("BIN") String BIN, @Path("MID2P") String MID2P, @Path("password") String password);

    @POST("/api/transactionsapi")
    Call<Transaction> newTransaction(@Body Transaction txn);

    @GET("/api/transactionsapi")
    Call<List<TransactionJSON>> getTransactions();

    @GET("/api/sendemailsapi/salesslip/{email}/{id}")
    Call<Boolean> emailSalesSlip(@Path("email") String email, @Path("id") long id);

    @GET("/api/sendemailsapi/voidslip/{email}/{id}")
    Call<Boolean> emailVoidSlip(@Path("email") String email, @Path("id") long id);
}
