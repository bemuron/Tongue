package viola1.agrovc.com.tonguefinal.helper;

import android.util.Log;



import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanApp;
import viola1.agrovc.com.tonguefinal.app.localserver.request.response.ResBeanGeneral;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.LocalRetrofitApi;
import viola1.agrovc.com.tonguefinal.dataloaders.retrofit.RetrofitService;


public class Encryption {

    public static String encrypt(String data_to_encrypt){

        /*return Base64.encodeToString(geStringFromByte(data_to_encrypt), Base64.NO_WRAP);*/
        return data_to_encrypt;

    }
    public static int encrypt2(int data_to_encrypt){

        /*return Base64.encodeToString(geStringFromByte(data_to_encrypt), Base64.NO_WRAP);*/
        return data_to_encrypt;

    }
    public static String decrypt(String data_to_decrypt){
      /*  byte[] data1 = Base64.decode(data_to_decrypt, Base64.DEFAULT);
        return geStringFromByte(data1);*/
        return data_to_decrypt;

    }

    private byte[] geStringFromByte(String user_data){
        byte[] data = null;
        try {
            data = user_data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return data;
    }

    private static String geStringFromByte(byte[] sent_data){
        String text1 = null;

        try {
            text1 = new String(sent_data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return text1;
    }

    public static void main(String[] args){

        RetrofitService service = new LocalRetrofitApi().getRetrofitService();
        ReqBeanApp reqBeanApp = new ReqBeanApp();
        reqBeanApp.setMember_email("tim@timo.net");

        Call<ResBeanGeneral> call = service.testEncryption(reqBeanApp);
        call.enqueue(new Callback<ResBeanGeneral>() {
            @Override
            public void onResponse(Call<ResBeanGeneral> call, Response<ResBeanGeneral> response) {

                Log.e("Response",decrypt(response.body().getError()));

            }

            @Override
            public void onFailure(Call<ResBeanGeneral> call, Throwable t) {

            }
        });
    }


}
