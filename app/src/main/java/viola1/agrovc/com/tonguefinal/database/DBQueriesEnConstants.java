package viola1.agrovc.com.tonguefinal.database;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class DBQueriesEnConstants {

    public static String DATABASE_NAME = "agrolink.db";
    public static int DATABASE_VERSION = 5;

    //table for user's biodata
    public static String TABLE_USER_BIODATA = "TABLE_USER_BIODATA";
    public static String COL_USER_ID = "USER_ID";
    public static String COL_USER_FIRST_NAME = "USER_FIRST_NAME";
    public static String COL_USER_LAST_NAME = "USER_LAST_NAME";
    public static String COL_USER_CONTACT = "USER_CONTACT";
    public static String COL_USER_SEX = "USER_SEX";
    public static String COL_USER_REG_DATE = "USER_REG_DATE";
    public static String COL_USER_BUSINESS = "BUSINESS_NAME";
    public static String COL_USER_EMAIL = "USER_EMAIL";
    public static String COL_USERNAME = "USERNAME";
    public static String COL_USER_ROLE = "USER_ROLE";
    public static String COL_USER_STATUS = "USER_STATUS";

    public static String COL_USER_COUNTRY = "USER_COUNTRY";
    public static String COL_USER_DISTRICT = "USER_DISTRICT";
    public static String COL_USER_PARISH = "USER_PARISH";
    public static String COL_USER_SUBPARISH = "USER_SUBPARISH";
    public static String COL_USER_VILLAGE = "USER_VILLAGE";

    public static String COL_USER_OPT_COUNTRY = "USER_OPT_COUNTRY";
    public static String COL_USER_OPT_DISTRICT = "USER_OPT_DISTRICT";
    public static String COL_USER_OPT_PARISH = "USER_OPT_PARISH";
    public static String COL_USER_OPT_SUBPARISH = "USER_OPT_SUBPARISH";
    public static String COL_USER_OPT_VILLAGE = "USER_OPT_VILLAGE";
    /*
    * END  OF USER TABLE
    * */

    /*
    * START OF PAYMENT ACCOUNT TABLE
    * */

    //table for user's payment account
    public static String TABLE_PAYMENT_ACCOUNTS = "TABLE_PAYMENT_ACCOUNTS";
    public static String COL_USER_REF_ID = "USER_ID";
    public static String COL_MOBILE_MONEY_NUMBER = "MOBILE_MONEY_NUMBER";
    public static String COL_MOBILE_MONEY_NETWORK = "MOBILE_MONEY_NETWORK";
    public static String COL_BANK_NAME = "BANK_NAME";
    public static String COL_BANK_ACCOUNT_NAME = "BANK_ACCOUNT_NAME";
    public static String COL_BANK_ACCOUNT_NUMBER = "BANK_ACCOUNT_NO";
    public static String COL_BANK_BRANCH = "BANK_BRANCH";


    public static String queryCreateUserBiodataTable = "CREATE TABLE if not exists " +TABLE_USER_BIODATA+ " (" +
            "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_ID + " TEXT, " +
            COL_USER_FIRST_NAME + " TEXT, " +
            COL_USER_LAST_NAME + " TEXT, " +
            COL_USER_CONTACT + " TEXT, " +
            COL_USER_SEX + " TEXT, " +
            COL_USER_REG_DATE + " TEXT, " +
            COL_USER_BUSINESS + " TEXT, " +
            COL_USER_EMAIL + " TEXT, " +
            COL_USERNAME + " TEXT, " +
            COL_USER_ROLE + " TEXT, " +
            COL_USER_STATUS + " TEXT, " +
            COL_USER_COUNTRY + " TEXT, " +
            COL_USER_DISTRICT + " TEXT, " +
            COL_USER_PARISH + " TEXT, " +
            COL_USER_SUBPARISH + " TEXT, " +
            COL_USER_VILLAGE + " TEXT, " +
            COL_USER_OPT_COUNTRY + " TEXT, " +
            COL_USER_OPT_DISTRICT + " TEXT, " +
            COL_USER_OPT_PARISH + " TEXT, " +
            COL_USER_OPT_SUBPARISH + " TEXT, " +
            COL_USER_OPT_VILLAGE + " TEXT " +
            " );";











    public static String queryCreatePaymentAccountsTable = "CREATE TABLE if not exists " +TABLE_PAYMENT_ACCOUNTS+ " (" +
            "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_REF_ID + " TEXT, " +
            COL_USER_EMAIL + " TEXT, " +
            COL_MOBILE_MONEY_NETWORK + " TEXT, " +
            COL_MOBILE_MONEY_NUMBER + " TEXT, " +
            COL_BANK_NAME + " TEXT, " +
            COL_BANK_ACCOUNT_NAME + " TEXT, " +
            COL_BANK_ACCOUNT_NUMBER + " TEXT, " +
            COL_BANK_BRANCH + " TEXT " +
            " );";

}
