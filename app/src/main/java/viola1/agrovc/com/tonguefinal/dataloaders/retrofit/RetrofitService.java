
package viola1.agrovc.com.tonguefinal.dataloaders.retrofit;




import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanApp;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanLogin;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanMaid;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanSecurityQuestions;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanSignup;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanTutor;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanUpDateLocation;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanUpdateBiodata;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanUserProfile;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanEmployer;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanGeneral;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanLogin;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanMaid;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanSecurityQuestions;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanSignup;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanTutor;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanUserProfile;


public interface RetrofitService {
    @POST("authenticate")
    Call<ResBeanLogin> authenticate(@Body ReqBeanLogin request);
     @POST("authenticateStudent")
     Call<ResBeanLogin> authenticateStudent(@Body ReqBeanLogin request);
     @POST("registerUser")
    Call<ResBeanSignup> registerUser(@Body ReqBeanSignup request);
    @POST("registerUserStudent")
    Call<ResBeanSignup> registerUserStudent(@Body ReqBeanSignup request);
    @POST("profile-default")
    Call<ResBeanUserProfile> getUserProfileData(@Body ReqBeanUserProfile request);
    @POST("updateAppUserBiodata")
    Call<ResBeanGeneral> updateUserProfileBioDataOnServer(@Body ReqBeanUpdateBiodata request);
    @POST("updateAppUserLocation")
    Call<ResBeanGeneral> updateUserProfileLocationOnServer(@Body ReqBeanUpDateLocation request);



    @POST("registerTutor")
    Call<ResBeanTutor> registerTutor(@Body ReqBeanTutor request);
    @POST("registerMaid")
    Call<ResBeanMaid> registerMaid(@Body ReqBeanMaid request);




    @POST("recoverPasswordQuestions")
    Call<ResBeanSecurityQuestions> SecurityQuestions(@Body ReqBeanSecurityQuestions request);
    @POST("testEncryption")
    Call<ResBeanGeneral> testEncryption(@Body ReqBeanApp reqBeanApp);

}

