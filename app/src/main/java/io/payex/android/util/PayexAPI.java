package io.payex.android.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by vince on 11/28/2016.
 */

public interface PayexAPI {
    @GET("/api/merchantsapi/authenticate/{BIN}/{MID2P}/{password}")
    Call<Integer> authenticate(@Path("BIN") String BIN, @Path("MID2P") String MID2P, @Path("password") String password);
}
