/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package viola1.agrovc.com.tonguefinal.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import viola1.agrovc.com.tonguefinal.utilities.InjectorUtils;

/**
 * An {@link IntentService} subclass for immediately scheduling a sync with the server off of the
 * main thread. This should only be called when the application is on the
 * screen.
 */
public class UserRegistrationIntentService extends IntentService {
    private static final String LOG_TAG = UserRegistrationIntentService.class.getSimpleName();
    private boolean registerSuccess;

    public UserRegistrationIntentService() {
        super("UserRegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle registerBundle = intent.getExtras();
        //String reg = intent.getExtras().get("name");

        Log.d(LOG_TAG, "User registration intent service started");
        RegisterUser registerUser = InjectorUtils.provideRegisterUser(this.getApplicationContext());

        if (registerBundle != null){
            Log.d(LOG_TAG, "registration details not empty");
            String name = registerBundle.getString("name");
            String dob = registerBundle.getString("dob");
            String gender = registerBundle.getString("gender");
            String email = registerBundle.getString("email");
            String password = registerBundle.getString("password");

            //pass the reg details to the method to be posted to the server: finally
            registerUser.UserRegister(name, dob, gender, email, password);
                    /*
            if (registerUser.UserRegister(name, dob, gender, email, password)){
                registerSuccess = true;
            }
            */
        }else{
            Log.e(LOG_TAG, "login details empty");
        }


    }

    public boolean isRegistrationSuccess() {
        return registerSuccess;
    }
}