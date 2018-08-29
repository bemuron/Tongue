package viola1.agrovc.com.tonguefinal.constants;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


/*
* Has response messages display to User's in alert dialogs and progress dialogs
* */
public enum EnumAppMessages {
    APP_PREF("APP_PREFS"),
    LOGIN_ERROR_TITLE("Login failed"),
    ERROR_INVALID_INPUT_TITLE("Incorrect input"),
    ERROR_INVALID_PASSWORD("Invalid password. Please ensure that password is more than 4 characters"),
    ERROR_INVALID_EMAIL_OR_PHONE_NO_OR_ID("Invalid email address, Phone number or YoDime id."),
    RESPONSE_STATUS_SUCCESS("success"),
    ERROR_INTERNET_CONNECTION("Please check your internet connection and try again"),
    ERROR_RESOURCE_NOT_FOUND("The resource you are trying to access cannot be found"),
    ERROR_UNKNOWN_ERROR("Error retrieving yodimebeans.response, looks like the server is temporarily down please try again later"),
    RESPONSE_STATUS_FAILURE("false"),
    ERROR_INTERNAL_ERROR("Details for this Email already exist, please try with another."),
    REGISTER_ERROR_TITLE("Registration Failed"),
    PAGING_ERROR("No more data"),
    REGISTER_SUCCESS_TITLE("Successful Registration"),
    SIGNUP_TITLE("Sign up"),
    DIALOG_PROCESSING("Processing ..."),
    DIALOG_LOADING("    Loading ...    "),
    DIALOG_UPDATING("    Updating ...    "),

    PIN_SETUP_SUCCESS_TITLE("Successful PIN setup"),
    PASSWORD_CHANGE_FAILURE_TITLE("Password change failed"),
    PASSWORD_CHANGE_SUCCESS_TITLE("Successful Password change"),
    PIN_CHANGE_FAILURE_TITLE("PIN change failed"),
    PIN_CHANGE_SUCCESS_TITLE("Successful PIN change"),
    PIN_SETUP_FAILURE_TITLE("Pin setup failed"),
    ACCOUNT_HISTORY_SUCCESS_TITLE("Successful loading"),
    ACCOUNT_HISTORY_FAILURE_TITLE("A/C Details loading failed"),
    UTILITY_TXN_HISTORY_SUCCESS_TITLE("Successful loading"),
    UTILITY_TXN_FAILURE_TITLE("Transactions loading failed"),
    SUCCESS_TRUE("true"),
    FAILURE_FALSE("false"),
    SUCCESS_TITLE("Successful"),
    FAILURE_TITLE("Failed"),
    INVALID_COUNTRY("Enter a valid country name"),
    INVALID_DISTRICT("Enter a valid district name"),
    VERIFICATION_ERROR_TITLE("Verification failed"),
    CLEARANCE_SUCCESS_TITLE("Ticket cleared"),
    CLEARANCE_ERROR_TITLE("Clearance failed");

    String name;
    EnumAppMessages(String name) {
       this.name = name;
    }

    public String getValue() {
        return name;
    }
}
