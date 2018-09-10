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

package viola1.agrovc.com.tonguefinal.utilities;

import android.content.Context;

import viola1.agrovc.com.tonguefinal.AppExecutors;
import viola1.agrovc.com.tonguefinal.data.TongueRepository;
import viola1.agrovc.com.tonguefinal.data.database.TongueDatabase;
import viola1.agrovc.com.tonguefinal.data.network.FetchLanguages;
import viola1.agrovc.com.tonguefinal.data.network.LoginUser;
import viola1.agrovc.com.tonguefinal.data.network.RegisterUser;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.HomeViewModelFactory;
import viola1.agrovc.com.tonguefinal.presentation.viewmodels.LoginRegistrationViewModelFactory;


/**
 * Provides static methods to inject the various classes needed for Sunshine i.e
 * The purpose of InjectorUtils is to provide static methods for dependency injection.
 * Dependency injection is the idea that you should make required components available for a class,
 * instead of creating them within the class itself.
 */
public class InjectorUtils {

    public static TongueRepository provideRepository(Context context) {
        TongueDatabase database = TongueDatabase.getInstance(context.getApplicationContext());

        AppExecutors executors = AppExecutors.getInstance();

        RegisterUser registerUser =
                RegisterUser.getInstance(context.getApplicationContext(), executors);

        LoginUser loginUser =
                LoginUser.getInstance(context.getApplicationContext(), executors);

        FetchLanguages fetchLanguages =
                FetchLanguages.getInstance(context.getApplicationContext(), executors);

        return TongueRepository.getInstance(database.languagesDao(), database.usersDao(), fetchLanguages,
                registerUser, loginUser, executors);
    }

    /*public static PostFixAppJob providePostFixAppJob(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return PostFixAppJob.getInstance(context.getApplicationContext(), executors);
    }*/

    public static LoginUser provideLoginUser(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return LoginUser.getInstance(context.getApplicationContext(), executors);
    }

    public static RegisterUser provideRegisterUser(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return RegisterUser.getInstance(context.getApplicationContext(), executors);
    }

    public static FetchLanguages provideFetchCategories(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return FetchLanguages.getInstance(context.getApplicationContext(), executors);
    }

//    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, Date date) {
//        SunshineRepository repository = provideRepository(context.getApplicationContext());
//        return new DetailViewModelFactory(repository, date);
//    }

    public static HomeViewModelFactory provideMainActivityViewModelFactory(Context context) {
        TongueRepository repository = provideRepository(context.getApplicationContext());
        return new HomeViewModelFactory(repository);
    }

    public static LoginRegistrationViewModelFactory provideLoginRegistrationViewModelFactory(Context context) {
        TongueRepository repository = provideRepository(context.getApplicationContext());
        return new LoginRegistrationViewModelFactory(repository);
    }

    /*public static PostJobViewModelFactory providePostJobActivityViewModelFactory(Context context) {
        FixAppRepository repository = provideRepository(context.getApplicationContext());
        return new PostJobViewModelFactory(repository);
    }*/

    /*
    * Dependency injection is the idea that you should make required components available
    * for a class, instead of creating them within the class itself. An example of how the
    * Sunshine code does this is that instead of constructing the WeatherNetworkDatasource
    * within the SunshineRepository, the WeatherNetworkDatasource is created via InjectorUtilis
    * and passed into the SunshineRepository constructor. One of the benefits of this is that
    * components are easier to replace when you're testing. You can learn more about dependency
    * injection here. For now, know that the methods in InjectorUtils create the classes you
    * need, so they can be passed into constructors.
    * */
}