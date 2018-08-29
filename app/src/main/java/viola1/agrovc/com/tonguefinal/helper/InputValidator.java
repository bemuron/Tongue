package viola1.agrovc.com.tonguefinal.helper;

import android.view.View;
import android.widget.EditText;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import viola1.agrovc.com.tonguefinal.constants.AppNums;


public class InputValidator {

    /*
    * Validates input user phone number must be 10 characters
    * */
    public  boolean isPhoneNumberValid(String phone_number){
        //matches 10-digit numbers only
        String regexStr = "^[0-9]{10}$";
        return  phone_number.matches(regexStr);
    }


    /**
     * Validate hex with regular expression
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean isEmailValid(final String hex) {
        Pattern pattern;
        Matcher matcher;

        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    /* Validates user password must be 4 or characters*/
    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= AppNums.MIN_PASSWORD_LENGTH;

    }

  /**  public boolean isBankDetailsValid(View view) {

        EditText edtBankName = (EditText) view.findViewById(R.id.edtBankName);
        EditText edtBankAccountName = (EditText) view.findViewById(R.id.edtBankAcName);
        EditText edtBankAccountNumber = (EditText) view.findViewById(R.id.edtBankAcNumber);
        EditText edtBankBranch = (EditText) view.findViewById(R.id.edtBankBranch);

        if(isValidBankAccountNumber(edtBankAccountNumber.getText().toString())){

            if(isValidBankName(edtBankName.getText().toString())){

                if(isValidBankAccountName(edtBankAccountName.getText().toString())){

                    if(isValidBankBranch(edtBankBranch.getText().toString())){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isValidBankBranch(String s) {
        return s!=null && s.length()>= AppNums.MIN_BANK_BRANCH_LENGTH;
    }

    private boolean isValidBankAccountName(String s) {
        return (s!=null && s.contains(" ")) && s.length()>0;
    }

    private boolean isValidBankName(String s) {
        return s!=null && s.length()>= AppNums.MIN_BANK_NAME_LENGTH;
    }

    private boolean isValidBankAccountNumber(String s) {
        return s!=null && s.length()>=AppNums.MIN_BANK_ACCOUNT_NUMBER_LENGTH;
    }*/
}
