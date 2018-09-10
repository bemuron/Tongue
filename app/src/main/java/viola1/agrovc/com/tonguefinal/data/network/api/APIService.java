package viola1.agrovc.com.tonguefinal.data.network.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.models.Languages;

public interface APIService {

    //getting the app languages
    @GET("public/languages")
    Call<Languages> getLanguages();

    //posting a job
    @FormUrlEncoded
    @POST("public/postjob")
    Call<Result> postJob(//@Path("id") int id,
                         //@Path("posted_by") int posted_by,
                         @Field("posted_by") int posted_by,
                         @Field("name") String name,
                         @Field("description") String description,
                         @Field("category_id") int category_id);

    //the login/signin call
    @FormUrlEncoded
    @POST("public/login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password);

    //The register call
    @FormUrlEncoded
    @POST("public/register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("date_of_birth") String date_of_birth,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("password") String password);

    /*
    //The register call
    @FormUrlEncoded
    @POST("public/register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender);

    //the signin call
    @FormUrlEncoded
    @POST("public/login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password);

/   //sending message
    @FormUrlEncoded
    @POST("public/sendmessage")
    Call<MessageResponse> sendMessage(
            @Field("from") int from,
            @Field("to") int to,
            @Field("title") String title,
            @Field("message") String message);

    //updating user
    @FormUrlEncoded
    @POST("public/update/{id}")
    Call<Result> updateUser(
            @Path("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender);

    //getting messages
    @GET("public/messages/{id}")
    Call<Messages> getMessages(@Path("id") int id);*/
}
