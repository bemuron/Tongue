package viola1.agrovc.com.tonguefinal.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import viola1.agrovc.com.tonguefinal.constants.AppPrefs;


public class GeneralMethods {

    public void showToastToUser(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    public void showLocationDialog(Context context, String title, String message) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.StyledDialog);*/
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public ProgressDialog progressDialog(Context context, String title, String message){
       /* ProgressDialog progressDialog = new ProgressDialog(context,R.style.StyledDialog);*/
        ProgressDialog progressDialog = new ProgressDialog(context);

        if(title!= null){
            progressDialog.setTitle(title);
        }
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        return  progressDialog;






    }


    /*public String getMemberEmail(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(AppPrefs.APP_PREFS, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(AppPrefs.PREF_EMAIL,"");
        if(email.length()==0){
            activity.startActivity(new Intent(activity, Login.class));
            activity.finish();
        }
        return email;
    }*/

    public String getMemberid(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(AppPrefs.APP_PREFS, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(AppPrefs.PREF_USER_ID,"");

        return id;
    }






    public void getExceptionLocation(String tag, String error_class, String error_method, Exception e) {
        Log.e(tag, "***** Exception in class "+error_class+"--->  "+error_method+"()");
        e.printStackTrace();
    }

    /*public String getMemberUserTpe(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(AppPrefs.APP_PREFS, Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString(AppPrefs.PREF_USER_TYPE,"");
        if(userType.length()==0){
            activity.startActivity(new Intent(activity, Login.class));
            activity.finish();
        }
        return userType;
    }*/


    }
