package viola1.agrovc.com.tonguefinal.data.network.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import viola1.agrovc.com.tonguefinal.data.network.ConfirmedRequestResult;
import viola1.agrovc.com.tonguefinal.data.network.LanguagesTaught;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequest;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequestResult;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequests;
import viola1.agrovc.com.tonguefinal.data.network.Result;
import viola1.agrovc.com.tonguefinal.data.network.SearchResult;
import viola1.agrovc.com.tonguefinal.data.network.Tutor;
import viola1.agrovc.com.tonguefinal.data.network.Tutors;
import viola1.agrovc.com.tonguefinal.data.network.UserPendingRequests;
import viola1.agrovc.com.tonguefinal.models.Languages;

public interface APIService {

    //getting the app languages
    @GET("languages")
    Call<Languages> getLanguages();

    //the login/signin call
    @FormUrlEncoded
    @POST("login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password);

    //register device with fcm
    @FormUrlEncoded
    @PUT("updateFcm/{user_id}")
    Call<Result> updateFcm(
            @Path("user_id") int user_id,
            @Field("fcm_registration_id") String fcm_registration_id);

    //The register call
    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("date_of_birth") String date_of_birth,
            @Field("gender") String gender,
            @Field("email") String email,
            @Field("password") String password);

    //the search for a language call
    @GET("language_search/{searchString}")
    Call<SearchResult> searchForLanguage(@Path("searchString") String searchString);

    //call to get a list of tutors who teach a specific language
    @GET("tutorList/{language_id}")
    Call<Tutors> getTutorsForLanguage(@Path("language_id") int language_id);

    //call to get a selected tutor's details
    @GET("tutorDetails/{tutor_id}")
    Call<Result> getTutorDetails(@Path("tutor_id") int tutor_id);

    //call to get the languages taught by a selected user
    @GET("tutorLanguages/{tutor_id}")
    Call<LanguagesTaught> getLanguagesForTutor(@Path("tutor_id") int tutor_id);

    //call to get the tutor rating
    @GET("tutorRating/{tutor_id}")
    Call<Tutor> getTutorRating(@Path("tutor_id") int tutor_id);

    //call to post the meeting_schedule
    @FormUrlEncoded
    @POST("meeting_schedule")
    Call<Result> createMeetingSchedule(
            @Field("user_id") int user_id,
            @Field("tutor_id") int tutor_id,
            @Field("language_name") String language_name,
            @Field("meeting_date") String meeting_date,
            @Field("meeting_time") String meeting_time,
            @Field("meeting_location") String location,
            @Field("last_modified_by") int last_modified_by);

    //getting the pending requests for the tutor
    @GET("pending_requests/{tutor_id}")
    Call<PendingRequests> getPendingRequests(@Path("tutor_id") int tutor_id);

    //getting the confirmed requests for the tutor
    @GET("confirmed_requests/{tutor_id}")
    Call<PendingRequests> getConfirmedRequests(@Path("tutor_id") int tutor_id);

    //getting the pending requests for the user
    @GET("user_pending_requests/{user_id}")
    Call<UserPendingRequests> getUserPendingRequests(@Path("user_id") int user_id);

    //call to get a selected pending request's details
    @GET("userPendingRequestDetails/{meeting_id}")
    Call<PendingRequestResult> getUserPendingRequestDetails(@Path("meeting_id") int meeting_id);

    //getting the confirmed requests list for the user
    @GET("user_confirmed_requests/{user_id}")
    Call<UserPendingRequests> getUserConfirmedRequests(@Path("user_id") int user_id);

    //call to get a selected pending request's details for tutor
    @GET("pendingRequestDetails/{meeting_id}")
    Call<PendingRequestResult> getPendingRequestDetails(@Path("meeting_id") int meeting_id);

    //call to get a selected confirmed request's details for tutor
    @GET("confirmedRequestDetails/{meeting_id}")
    Call<ConfirmedRequestResult> getConfirmedRequestDetails(@Path("meeting_id") int meeting_id);

    //call to get a selected confirmed request's details for user
    @GET("userConfirmedRequestDetails/{meeting_id}")
    Call<ConfirmedRequestResult> getConfirmedRequestDetailsForUser(@Path("meeting_id") int meeting_id);

    //call to get meeting details for user
    //without the check on whether its pending, confirmed, complete
    @GET("meetingDetailsForUser/{meeting_id}")
    Call<ConfirmedRequestResult> getMeetingDetailsForUser(@Path("meeting_id") int meeting_id);

    //call to get meeting details for tutor
    //without the check on whether its pending, confirmed, complete
    @GET("meetingDetailsForTutor/{meeting_id}")
    Call<ConfirmedRequestResult> getMeetingDetailsForTutor(@Path("meeting_id") int meeting_id);

    //call to update the meeting_schedule
    @FormUrlEncoded
    @POST("updateMeetingSchedule/{meeting_id}")
    Call<Result> updateMeetingSchedule(
            @Path("meeting_id") int meeting_id,
            @Field("meeting_date") String meeting_date,
            @Field("meeting_time") String meeting_time,
            @Field("meeting_location") String location,
            @Field("last_modified_by") int last_modified_by,
            @Field("notify_user_id") int notify_user_id);

    //call to confirm the meeting_schedule
    @FormUrlEncoded
    @POST("confirmMeetingSchedule/{meeting_id}")
    Call<Result> confirmMeetingSchedule(
            @Path("meeting_id") int meeting_id,
            @Field("last_modified_by") int last_modified_by,
            @Field("notify_user_id") int notify_user_id);

    //call to start the meeting session
    @FormUrlEncoded
    @POST("startMeetingSession/{meeting_id}")
    Call<Result> startMeetingSession(
            @Path("meeting_id") int meeting_id,
            @Field("tutor_id") int tutor_id,
            @Field("notify_user_id") int notify_user_id);

    //call to end the meeting session
    @FormUrlEncoded
    @POST("endMeetingSession/{meeting_id}")
    Call<Result> endMeetingSession(
            @Path("meeting_id") int meeting_id,
            @Field("tutor_id") int tutor_id,
            @Field("notify_user_id") int notify_user_id,
            @Field("time_taken") float time_taken);

    //call to update session with price received
    @FormUrlEncoded
    @POST("sessionPaymentReceived/{meeting_id}")
    Call<Result> sessionPaymentReceived(
            @Path("meeting_id") int meeting_id,
            @Field("notify_user_id") int notify_user_id,
            @Field("session_cost") int session_cost
    );

    //call to reject/delete the meeting_schedule
    @FormUrlEncoded
    @POST("deleteMeetingSchedule/{meeting_id}")
    Call<Result> deleteMeetingSchedule(
            @Path("meeting_id") int meeting_id,
            @Field("last_modified_by") int last_modified_by);

    //updating user
    @FormUrlEncoded
    @POST("update/{user_id}")
    Call<Result> updateUser(
            @Path("user_id") int user_id,
            @Field("name") String name,
            @Field("description") String description,
            @Field("date_of_birth") String date_of_birth,
            @Field("gender") String gender);

    //adding the user languages spoken
    //called during the become tutor process
    @FormUrlEncoded
    @POST("languageSpokenId")
    Call<Result> addlanguageSpokenId(
            @Field("language_id") int language_id,
            @Field("user_id") int user_id);

    //adding the description of the user
    //called during the become tutor process if user
    //retrofit(FormUrlEncoded) requires that we provide atleast one @Field
    @FormUrlEncoded
    @PUT("updateUserRole/{user_id}")
    Call<Result> updateUserRole(
            @Path("user_id") int user_id,
            @Field("userId") int userId);

    //uploading user profile pic
    @Multipart
    @POST("uploadUserProfilePic/{user_id}")
    Call<Result> uploadUserProfilePic(
            @Path("user_id") int user_id,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name);

    //submitting user rating
    //call to reject/delete the meeting_schedule
    @FormUrlEncoded
    @POST("submitUserRating")
    Call<Result> submitUserRating(
            @Field("meeting_id") int meeting_id,
            @Field("tutor_id") int tutor_id,
            @Field("student_id") int student_id,
            @Field("rating_value") float rating_value);

    //submitting tutor rating
    //call to reject/delete the meeting_schedule
    @FormUrlEncoded
    @POST("submitTutorRating")
    Call<Result> submitTutorRating(
            @Field("meeting_id") int meeting_id,
            @Field("user_id") int user_id,
            @Field("tutor_id") int tutor_id,
            @Field("rating_value") float rating_value);

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
