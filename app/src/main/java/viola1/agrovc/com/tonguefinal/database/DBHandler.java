package viola1.agrovc.com.tonguefinal.database;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import viola1.agrovc.com.tonguefinal.app.localserver.UserBioData;
import viola1.agrovc.com.tonguefinal.app.localserver.UserLocation;
import viola1.agrovc.com.tonguefinal.app.localserver.UserPaymentAccount;
import viola1.agrovc.com.tonguefinal.app.localserver.request.ReqBeanUpdateBiodata;
import viola1.agrovc.com.tonguefinal.enums.EnumLocationType;
import viola1.agrovc.com.tonguefinal.helper.GeneralMethods;


public class DBHandler extends SQLiteOpenHelper{

    private static DBHandler DB_HANDLER = null;
    public static DBHandler getInstance(Context context){
        if(DB_HANDLER == null){
            DB_HANDLER = new DBHandler(context, DBQueriesEnConstants.DATABASE_NAME, null, DBQueriesEnConstants.DATABASE_VERSION);
        }
        return DB_HANDLER;
    }

    private DBHandler(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(DBQueriesEnConstants.queryCreateUserBiodataTable);

        sqLiteDatabase.execSQL(DBQueriesEnConstants.queryCreatePaymentAccountsTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DBQueriesEnConstants.TABLE_USER_BIODATA+";");

        onCreate(sqLiteDatabase);
    }

    public void saveUserProfileInformation(UserBioData userBioData, UserLocation userLocationMandatory, UserLocation userLocationOptional){

        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DBQueriesEnConstants.TABLE_USER_BIODATA+";");

            sqLiteDatabase.execSQL(DBQueriesEnConstants.queryCreateUserBiodataTable);

            ContentValues values = new ContentValues();
            values.put(DBQueriesEnConstants.COL_USER_FIRST_NAME, userBioData.getFirstName());
            values.put(DBQueriesEnConstants.COL_USER_LAST_NAME, userBioData.getLastName());
            values.put(DBQueriesEnConstants.COL_USER_CONTACT, userBioData.getContact());
            values.put(DBQueriesEnConstants.COL_USER_SEX, userBioData.getSex());
            values.put(DBQueriesEnConstants.COL_USER_REG_DATE, userBioData.getRegDate());
            values.put(DBQueriesEnConstants.COL_USERNAME, userBioData.getUserName());
            values.put(DBQueriesEnConstants.COL_USER_BUSINESS, userBioData.getBusinessName());
            values.put(DBQueriesEnConstants.COL_USER_EMAIL, userBioData.getEmail());
            values.put(DBQueriesEnConstants.COL_USER_ROLE, userBioData.getRole());
            values.put(DBQueriesEnConstants.COL_USER_STATUS, userBioData.getStatus());

            values.put(DBQueriesEnConstants.COL_USER_COUNTRY,
                    userLocationMandatory.getCountry()!=null?userLocationMandatory.getCountry():"");
            values.put(DBQueriesEnConstants.COL_USER_DISTRICT,
                    userLocationMandatory.getDistrict()!=null?userLocationMandatory.getDistrict():"");
            values.put(DBQueriesEnConstants.COL_USER_PARISH,
                    userLocationMandatory.getParish()!=null?userLocationMandatory.getParish():"");
            values.put(DBQueriesEnConstants.COL_USER_SUBPARISH,
                    userLocationMandatory.getSubparish()!=null?userLocationMandatory.getSubparish():"");
            values.put(DBQueriesEnConstants.COL_USER_VILLAGE,
                    userLocationMandatory.getVillage()!=null?userLocationMandatory.getVillage():"");

            values.put(DBQueriesEnConstants.COL_USER_OPT_COUNTRY,
                    userLocationOptional.getOpt_country()!=null?userLocationOptional.getOpt_country():"");
            values.put(DBQueriesEnConstants.COL_USER_OPT_DISTRICT,
                    userLocationOptional.getOpt_district()!=null?userLocationOptional.getOpt_district():"");
            values.put(DBQueriesEnConstants.COL_USER_OPT_PARISH,
                    userLocationOptional.getOpt_parish()!=null?userLocationOptional.getOpt_parish():"");
            values.put(DBQueriesEnConstants.COL_USER_OPT_SUBPARISH,
                    userLocationOptional.getOpt_subparish()!=null?userLocationOptional.getOpt_subparish():"");
            values.put(DBQueriesEnConstants.COL_USER_OPT_VILLAGE,
                    userLocationOptional.getOpt_village()!=null?userLocationOptional.getOpt_village():"");


            sqLiteDatabase.insert(DBQueriesEnConstants.TABLE_USER_BIODATA,null,values);
        }catch (Exception e){

            new GeneralMethods().getExceptionLocation("DBHandler","DBHandler","saveUserProfileInformation",e);

        }

    }

    private void update(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        /*UPDATE table SET
                column1 = value1,
                column2 = value2
        WHERE condition*/

/*
        sqLiteDatabase.execSQL("UPDATE "+ DBQueriesEnConstants.TABLE_DB_STATUS+" SET "+ DBQueriesEnConstants.COLUMN_DB_STATUS+ " = \""
                +database_status+"\" WHERE "+ DBQueriesEnConstants.COLUMN_ID+ " = "+1+";");
*/

    }

    /*
    * Gets the user's biodata Information
    * */
    public UserBioData getUserBioData(String member_email){

        try{


            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            String query = "SELECT * FROM "+ DBQueriesEnConstants.TABLE_USER_BIODATA+ " where USER_EMAIL = \'"+member_email+"\';";

            sqLiteDatabase.beginTransaction();

            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToFirst();

            String firstName = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_LAST_NAME));
            String contact = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_CONTACT));
            String sex = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_SEX));
            String regDate = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_REG_DATE));
            String userName = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USERNAME));
            String businessName = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_BUSINESS));
            String email = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_EMAIL));
            String role = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_ROLE));
            String status = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_STATUS));

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return new UserBioData(firstName, lastName, contact, sex, regDate, userName, businessName, email, role, status);
        }catch (Exception e){
            return null;
        }

    }


    /*
    * Invokes the right method to save a user's location i.e either the mandatory location
    * or the optional location
    * */
    public boolean callMethodToSaveLocationBasedOnLocationType(UserLocation userLocation, String locationType, String member_email) {

        try{
            if(locationType.equalsIgnoreCase(EnumLocationType.MANDATORY.getValue())){
                return updateMandatoryLocation(userLocation,member_email);
            }else {
                return  updateOptionalLocation(userLocation,member_email);
            }
        }catch (Exception e){
            return false;
        }


    }

    /*
    * Saves the optional user location
    * */
    private boolean updateOptionalLocation(UserLocation userLocationMandatory, String member_email) {

        try{
            String update_query = "UPDATE "+DBQueriesEnConstants.TABLE_USER_BIODATA+" SET "+
                    DBQueriesEnConstants.COL_USER_OPT_COUNTRY +"= \'"+userLocationMandatory.getCountry()+"\' ,"+
                    DBQueriesEnConstants.COL_USER_OPT_PARISH +"= \'"+userLocationMandatory.getParish() +"\' ,"+
                    DBQueriesEnConstants.COL_USER_OPT_SUBPARISH +"= \'"+userLocationMandatory.getSubparish()+"\' ,"+
                    DBQueriesEnConstants.COL_USER_OPT_VILLAGE +"= \'"+userLocationMandatory.getVillage()+"\' ,"+
                    DBQueriesEnConstants.COL_USER_OPT_DISTRICT +"= \'"+userLocationMandatory.getDistrict()+"\' "+
                    "WHERE "+DBQueriesEnConstants.COL_USER_EMAIL+" = \'"+member_email+"\';";

            Log.e("DBHAndler",update_query);

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.execSQL(update_query);

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return true;
        }catch (Exception e){
            new GeneralMethods().getExceptionLocation("DBHandler","DBHandler","updateOptionalLocation",e);
            return false;
        }

    }

    /*
    * Saves the Mandatory user location
    * */
    private boolean updateMandatoryLocation(UserLocation userLocationMandatory, String member_email) {

        Log.e("DBHandler",userLocationMandatory.getCountry()+" Email :"+member_email);

        try{
            String update_query = "UPDATE "+DBQueriesEnConstants.TABLE_USER_BIODATA+" SET "+
                    DBQueriesEnConstants.COL_USER_COUNTRY +" = \'"+userLocationMandatory.getCountry()+"\', "+
                    DBQueriesEnConstants.COL_USER_PARISH +" = \'"+userLocationMandatory.getParish() +"\', "+
                    DBQueriesEnConstants.COL_USER_SUBPARISH +" = \'"+userLocationMandatory.getSubparish()+"\', "+
                    DBQueriesEnConstants.COL_USER_VILLAGE +" = \'"+userLocationMandatory.getVillage()+"\', "+
                    DBQueriesEnConstants.COL_USER_DISTRICT +" = \'"+userLocationMandatory.getDistrict()+"\' "+
                    "WHERE "+DBQueriesEnConstants.COL_USER_EMAIL+" = \'"+member_email+"\';";

            Log.e("DBHAndler",update_query);

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            /*sqLiteDatabase.execSQL(update_query);*/

            ContentValues cv =new ContentValues();
            cv.put(DBQueriesEnConstants.COL_USER_COUNTRY,userLocationMandatory.getCountry());
            cv.put(DBQueriesEnConstants.COL_USER_PARISH,userLocationMandatory.getParish());
            cv.put(DBQueriesEnConstants.COL_USER_SUBPARISH,userLocationMandatory.getSubparish());
            cv.put(DBQueriesEnConstants.COL_USER_VILLAGE,userLocationMandatory.getVillage());
            cv.put(DBQueriesEnConstants.COL_USER_DISTRICT,userLocationMandatory.getDistrict());

            sqLiteDatabase.update(DBQueriesEnConstants.TABLE_USER_BIODATA,cv,DBQueriesEnConstants.COL_USER_EMAIL+" = '"+member_email+"'",null);


            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();


            Log.e("DBHAndler","Finished update statement");

            return true;
        }catch (Exception e){
            new GeneralMethods().getExceptionLocation("DBHandler","DBHandler","updateMandatoryLocation",e);
            return false;
        }

    }

    /*
    * Get user's location details
    * */
    public UserLocation getUserLocationBothMandatoryAndOptional(String memberEmail) {
        try{


            String query = "SELECT * FROM "+ DBQueriesEnConstants.TABLE_USER_BIODATA+ " where USER_EMAIL = \'"+memberEmail+"\'";

            Log.e("DBHAndle",query);

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToFirst();

            Log.e("Handler", String.valueOf(cursor.getCount()));

            String country = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_COUNTRY));
            String district = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_DISTRICT));
            String parish = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_PARISH));
            String subparish = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_SUBPARISH));
            String village = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_VILLAGE));
            String opt_country = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_OPT_COUNTRY));
            String opt_district = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_OPT_DISTRICT));
            String opt_parish = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_OPT_PARISH));
            String opt_subparish = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_OPT_SUBPARISH));
            String opt_village = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_USER_OPT_VILLAGE));

            Log.e("DBHAndler","Count cursor"+cursor.getCount());
            Log.e("DBHAndler",country);
            Log.e("DBHAndler",district);
            Log.e("DBHAndler",parish);
            Log.e("DBHAndler",subparish);
            Log.e("DBHAndler",village);

            UserLocation userLocation = new UserLocation();

            userLocation.setCountry(country);
            userLocation.setDistrict(district);
            userLocation.setParish(parish);
            userLocation.setSubparish(subparish);
            userLocation.setVillage(village);
            userLocation.setOpt_country(opt_country);
            userLocation.setOpt_district(opt_district);
            userLocation.setOpt_parish(opt_parish);
            userLocation.setOpt_subparish(opt_subparish);
            userLocation.setOpt_village(opt_village);

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return userLocation;


        }catch (Exception e){
            new GeneralMethods().getExceptionLocation("DBHandler","DBHAndler","getUserLocationBothMandatoryAndOptional",e);
            return null;
        }

    }

    /*
    * Update the User's biodata from their General tab of the Profile Page
    * */
    public boolean updateUserBioData(ReqBeanUpdateBiodata reqBean, String memberEmail) {

        try{
            String update_query = "UPDATE "+DBQueriesEnConstants.TABLE_USER_BIODATA+" SET "+
                    DBQueriesEnConstants.COL_USER_CONTACT +" = \'"+reqBean.getContact()+"\' ,"+
                    DBQueriesEnConstants.COL_USER_FIRST_NAME +" = \'"+reqBean.getFirst_name() +"\' ,"+
                    DBQueriesEnConstants.COL_USERNAME +" = \'"+reqBean.getUsername() +"\' ,"+
                    DBQueriesEnConstants.COL_USER_LAST_NAME +" = \'"+reqBean.getLast_name()+"\' ,"+
                    DBQueriesEnConstants.COL_USER_ROLE +" = \'"+reqBean.getUser_type()+"\' ,"+
                    DBQueriesEnConstants.COL_USER_BUSINESS +" = \'"+reqBean.getBusiness_name()+"\' "+
                    "WHERE "+DBQueriesEnConstants.COL_USER_EMAIL+" = \'"+memberEmail+"\';";

            Log.e("DBHAndler",update_query);

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.execSQL(update_query);

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return true;
        }catch (Exception e){
            new GeneralMethods().getExceptionLocation("DBHandler","DBHandler","updateUserBioData",e);
            return false;
        }

    }

    /*
    * Updates the user's payment accounts information from the Accounts tab of the user's profile page
    * */
    public boolean upDateUserPaymentAccountsDetails(UserPaymentAccount userPaymentAccount, String memberEmail) {
        try{
            String update_query = "UPDATE "+DBQueriesEnConstants.TABLE_PAYMENT_ACCOUNTS+" SET "+
                    DBQueriesEnConstants.COL_MOBILE_MONEY_NUMBER +" = \'"+userPaymentAccount.getMobileMoneyNumber()+"\' ,"+
                    DBQueriesEnConstants.COL_MOBILE_MONEY_NETWORK +" = \'"+userPaymentAccount.getMobileMoneyNetwork() +"\' ,"+
                    DBQueriesEnConstants.COL_BANK_NAME +" = \'"+userPaymentAccount.getBankName() +"\' ,"+
                    DBQueriesEnConstants.COL_BANK_ACCOUNT_NAME +" = \'"+userPaymentAccount.getBankAccountName()+"\' ,"+
                    DBQueriesEnConstants.COL_BANK_ACCOUNT_NUMBER +" = \'"+userPaymentAccount.getBankAccountNumber()+"\' ,"+
                    DBQueriesEnConstants.COL_BANK_BRANCH +" = \'"+userPaymentAccount.getBankBranch()+"\' "+
                    "WHERE "+DBQueriesEnConstants.COL_USER_EMAIL+" = \'"+memberEmail+"\';";

            Log.e("DBHAndler",update_query);

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.execSQL(update_query);

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return true;

        }catch (Exception e){

            new GeneralMethods().getExceptionLocation("DBHandler","DBHandler","upDateUserPaymentAccountsDetails",e);
            return false;
        }
    }

    public void saveUserPaymentAccount(UserPaymentAccount userPaymentAccount, String email) {

        try{

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();

            sqLiteDatabase.beginTransaction();

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DBQueriesEnConstants.TABLE_PAYMENT_ACCOUNTS+";");

            sqLiteDatabase.execSQL(DBQueriesEnConstants.queryCreatePaymentAccountsTable);

            ContentValues values = new ContentValues();

            values.put(DBQueriesEnConstants.COL_USER_EMAIL,email);

         values.put(DBQueriesEnConstants.COL_MOBILE_MONEY_NUMBER,
                  userPaymentAccount.getMobileMoneyNumber()!=null?userPaymentAccount.getMobileMoneyNumber():"");

            values.put(DBQueriesEnConstants.COL_MOBILE_MONEY_NETWORK,
                    userPaymentAccount.getMobileMoneyNetwork()!=null?userPaymentAccount.getMobileMoneyNetwork():"");

            values.put(DBQueriesEnConstants.COL_BANK_NAME,
                    userPaymentAccount.getBankName()!=null?userPaymentAccount.getBankName():"");

            values.put(DBQueriesEnConstants.COL_BANK_ACCOUNT_NAME,
                    userPaymentAccount.getBankAccountName()!=null?userPaymentAccount.getBankAccountName():"");

            values.put(DBQueriesEnConstants.COL_BANK_ACCOUNT_NUMBER,
                    userPaymentAccount.getBankAccountNumber()!= null?userPaymentAccount.getBankAccountNumber():"");

            values.put(DBQueriesEnConstants.COL_BANK_BRANCH,
                    userPaymentAccount.getBankBranch()!=null?userPaymentAccount.getBankBranch():"");

            sqLiteDatabase.insert(DBQueriesEnConstants.TABLE_PAYMENT_ACCOUNTS,null,values);

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

        }catch (Exception e){

            new GeneralMethods().getExceptionLocation("DBHandler","DBHandler","saveUserPaymentAccount",e);

        }

    }

    public UserPaymentAccount getUserPaymentAccountDetails(String memberEmail) {
        try{


            String query = "SELECT * FROM "+ DBQueriesEnConstants.TABLE_PAYMENT_ACCOUNTS+ " where USER_EMAIL = \'"+memberEmail+"\'";

            Log.e("DBHAndle",query);

            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();

            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToFirst();

            String mobileMoneyNumber = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_MOBILE_MONEY_NUMBER));
            String mobileMoneyNetwork = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_MOBILE_MONEY_NETWORK));

            String bankName = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_BANK_NAME));
            String bankAccountNumber = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_BANK_ACCOUNT_NUMBER));
            String bankAccountName = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_BANK_ACCOUNT_NAME));
            String bankBranch = cursor.getString(cursor.getColumnIndex(DBQueriesEnConstants.COL_BANK_BRANCH));

            UserPaymentAccount userPaymentAccount = new UserPaymentAccount();

            userPaymentAccount.setMobileMoneyNumber(mobileMoneyNumber);
            userPaymentAccount.setMobileMoneyNetwork(mobileMoneyNetwork);

            userPaymentAccount.setBankName(bankName);
            userPaymentAccount.setBankAccountNumber(bankAccountNumber);
            userPaymentAccount.setBankAccountName(bankAccountName);
            userPaymentAccount.setBankBranch(bankBranch);

            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            return userPaymentAccount;


        }catch (Exception e){
            new GeneralMethods().getExceptionLocation("DBHandler","DBHAndler","getUserPaymentAccountDetails",e);
            return null;
        }
    }
}
