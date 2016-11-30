package io.payex.android.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by vince on 11/28/2016.
 */

public interface PayexAPI {
    @GET("/api/merchantsapi/authenticate/{MID2P}/{BIN}/{password}")
    Call<Boolean> authenticate(@Path("MID2P") String MID2P, @Path("BIN") String BIN, @Path("password") String password);
}
