
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


/**
 * Created by smn on 27/9/17.
 */

public interface APILogin {
    @FormUrlEncoded
    @POST("")
    Call<LoginResponse> userLogIn(@FieldMap Map<String, String> credentialMap);

    @Multipart
    @POST("")
    Call<LoginResponse> registerUser(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("first_name") RequestBody fname
            , @Part("middle_name") RequestBody mname,
            @Part("last_name") RequestBody lname,
            @Part("mobile") RequestBody mobile,
            @Part("default_size") RequestBody defaultsize,
            @Part("mobile_type") RequestBody mobileType,
            @Part("device_token") RequestBody deviceTocken,
            @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST("")
    Call<CheckEmailResponse> userCheckEmail(@Field("email") String email);


    @FormUrlEncoded
    @POST("")
    Call<LoginResponse> userlogOut(@Field("user_id") String userId, @Field("mobile_type") String email);



    @FormUrlEncoded
    @POST("")
    Call<LoginResponse> userProfile(@FieldMap Map<String, String> credentialMap);


    @Multipart
    @POST("")
    Call<LoginResponse> edtitProfile(@Part("user_id") RequestBody userId,
                                     @Part("mobile") RequestBody mobile,
                                     @Part("email") RequestBody email,
                                     @Part("first_name") RequestBody firstname,
                                     @Part("middle_name") RequestBody middlename,
                                     @Part("last_name") RequestBody lastname,
                                     @Part MultipartBody.Part image);


    @GET
    Call<ResponseBody> downlload(@Url String fileUrl);

    



    /*@Multipart
    @POST("")
    Call<SubmitResponse> prdouctDetail_request_to_sell(
            @Part("user_id") RequestBody userId,
            @Part("product_id") RequestBody pr
            @Part("product_sku") RequestBody pro,
            @Part("size") RequestBody produi,
            @Part("box_condition") RequestBody producn,
            @Part("issues") RequestBody prodtIssue,
            @Part("other_issues") RequestBodyct erissue,
            @Part("price") RequestBody pice,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4,
            @Part MultipartBody.Part image5,
            @Part MultipartBody.Part image6,
            @Part MultipartBody.Part image7,
            @Part List<MultipartBody.Part> bImageList
    );*/


    @Multipart
    @POST("")
    Call<SubmitResponse> prdouctDetail_request_to_sell(
            @Part("user_id") RequestBody userId,
            @Part("product_id") RequestBody pructId,
            @Part("product_sku") RequestBody protSku,
            @Part("size") RequestBody produtSize,
            @Part("box_condition") RequestBody prooxcondtion,
            @Part("issues") RequestBody pssue,
            @Part("other_issues") RequestBody pssue,
            @Part("price") RequestBody productPrice,
            @Part List<MultipartBody.Part> pimageList,
            @Part List<MultipartBody.Part> bImageList
    );


    // List for Sell product list

    @FormUrlEncoded
    @POST("")
    Call<ProductForSellResponse> displayProductForSell(@FieldMap Map<String, String> credentialMap);






}
