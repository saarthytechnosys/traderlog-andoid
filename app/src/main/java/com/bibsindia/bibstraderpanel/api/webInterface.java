package com.bibsindia.bibstraderpanel.api;

import com.bibsindia.bibstraderpanel.model.placeOrder.PlaceOrder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface webInterface {
    //Login
    @FormUrlEncoded
    @POST("trader/login")
    Call<ResponseBody> Login(@Query("device_type") String deviceType,
                             @Query("secret_key") String secretKey,
                             @Field("mobile_no") String mobileNo,
                             @Field("password") String password,
                             @Field("fcm_token") String token
    );

    //Send Otp
    @FormUrlEncoded
    @POST("trader/sendotp")
    Call<ResponseBody> SendOtp(@Field("mobile_no") String mobileNo);

    //Verify Otp
    @GET("trader/confirmotp")
    Call<ResponseBody> VerifyOtp(@Query("mobile_no") String mobileNo, @Query("otp") String otp);

    //Reset Password
    @FormUrlEncoded
    @POST("trader/reset")
    Call<ResponseBody> Reset(@Field("password") String password,
                             @Field("confirm_password") String confirm_password,
                             @Field("mobile_no") String mobile_no);

    //Dashboards
    @GET("dashboard/trader")
    Call<ResponseBody> Dashboards(@Header("Authorization") String token);

    //OrderHistory - D
    @GET("Order-list/trader")
    Call<ResponseBody> OrderHistory(@Header("Authorization") String token,
                                    @Query("limit") int limit,
                                    @Query("offset") int offset,
                                    @Query("order_no") String order_no,
                                    @Query("start_date") String start_date,
                                    @Query("end_date") String end_date,
                                    @Query("m_id") String m_id
    );


    //OrderDetails - D
    @GET("Order-detail/trader/{order_no}")
    Call<ResponseBody> OrderDetails(@Header("Authorization") String token, @Path("order_no") String order_no);


    //SavePDF - D
    @GET("pdf/{order_no}/{m_id}")
    Call<ResponseBody> SavePDF(@Header("Authorization") String token, @Path("order_no") String order_no, @Path("m_id") int m_id);


    //CataloguesList - D
    @GET("trader/catalogue-price/list/{m_id}")
    Call<ResponseBody> CataloguesList(@Header("Authorization") String token,
                                      @Path("m_id") int m_id,
                                      @Query("limit") int limit,
                                      @Query("offset") int offset,
                                      @Query("catalogue_name") String catalogue_name,
                                      @Query("catalogue_no") String catalogue_no,
                                      @Query("product_id") String product_id,
                                      @Query("fabric_id") String fabric_id,
                                      @Query("subfabric_id") String subfabric_id
    );

    //CataloguesListShare - D
    @GET("trader/catalogue/list/{m_id}")
    Call<ResponseBody> CataloguesListShare(@Header("Authorization") String token,
                                           @Path("m_id") int m_id,
                                           @Query("limit") int limit,
                                           @Query("offset") int offset,
                                           @Query("catalogue_name") String catalogue_name,
                                           @Query("catalogue_no") String catalogue_no,
                                           @Query("product_id") String product_id,
                                           @Query("fabric_id") String fabric_id,
                                           @Query("subfabric_id") String subfabric_id
    );

    //Place Order - D
    @POST("addOrder/{catalogue_id}/{m_id}")
    Call<ResponseBody> PlacesOrder(@Header("Authorization") String token, @Path("catalogue_id") int catalogue_id, @Path("m_id") int m_id, @Body PlaceOrder mPlaceOrder);

    //Share Order - D
    @GET("shareurl/{catalogue_id}/{m_id}")
    Call<ResponseBody> ShareLink(@Header("Authorization") String token, @Path("catalogue_id") int catalogue_id, @Path("m_id") int m_id);

    //ProductList - D
    @GET("trader/get/product-list/{m_id}")
    Call<ResponseBody> ProductList(@Header("Authorization") String token, @Path("m_id") int m_id);

    //FabricList - D
    @GET("trader/get/fabric-list/{m_id}")
    Call<ResponseBody> FabricList(@Header("Authorization") String token, @Path("m_id") int m_id);

    //VariantList - D
    @GET("trader/get/subfabric-list/{m_id}")
    Call<ResponseBody> VariantList(@Header("Authorization") String token,@Path("m_id") int m_id, @Query("fabric_id") int fabric_id);

    //ManufacturerList - D
    @GET("manufacturer/get/manufacturer-list")
    Call<ResponseBody> ManufacturerList(@Header("Authorization") String token);

    //Get Profile Details - D
    @GET("trader/edit/{m_id}")
    Call<ResponseBody> GetProfileDetails(@Header("Authorization") String token, @Path("m_id") int m_id);

    //Update Profile Details - D
    @POST("trader/update-profile/{m_id}")
    Call<ResponseBody> UpdateProfileDetails(@Header("Authorization") String token,
                                            @Path("m_id") int m_id,
                                            @Query("agent_name") String agent_name,
                                            @Query("party_name") String party_name,
                                            @Query("gst_no") String gst_no,
                                            @Query("mobile_no") String mobile_no,
                                            @Query("email") String email,
                                            @Query("state") int state,
                                            @Query("city") String city,
                                            @Query("_method") String _method
    );

    //State List
    @GET("stateList")
    Call<ResponseBody> GetState();

}
