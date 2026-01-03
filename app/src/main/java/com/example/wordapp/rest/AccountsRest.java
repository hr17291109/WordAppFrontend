package com.example.wordapp.rest;

import com.example.wordapp.models.Account;
import java.util.Set;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountsRest {
    @GET("accounts")
    Call<Set<String>> getAccounts();

    @FormUrlEncoded
    @POST("accounts")
    Call<Void> signup(
            @Field("account_id") String account_id,
            @Field("password") String password
    );

    @GET("accounts/{account_id}")
    Call<Account> getAccountInfo(
            @Path("account_id") String account_id,
            @Query("password") String password
    );

    @FormUrlEncoded
    @PUT("accounts/{account_id}/password")
    Call<Void> changePW(
            @Path("account_id") String account_id,
            @Field("new_password") String new_password,
            @Field("old_password") String old_password
    );


}
