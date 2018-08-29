package viola1.agrovc.com.tonguefinal.app.localserver;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */


public class UserPaymentAccount {

    private String mobile_money_number;
    private String mobile_money_network;

    private String bank_name;
    private String bank_ac_name;
    private String bank_ac_number;
    private String bank_branch;

    public String getMobileMoneyNumber() {
        return mobile_money_number;
    }

    public void setMobileMoneyNumber(String mobileMoneyNumber) {
        this.mobile_money_number = mobileMoneyNumber;
    }

    public String getMobileMoneyNetwork() {
        return mobile_money_network;
    }

    public void setMobileMoneyNetwork(String mobileMoneyNetwork) {
        this.mobile_money_network = mobileMoneyNetwork;
    }

    public String getBankName() {
        return bank_name;
    }

    public void setBankName(String bankName) {
        this.bank_name = bankName;
    }

    public String getBankAccountName() {
        return bank_ac_name;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bank_ac_name = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bank_ac_number;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bank_ac_number = bankAccountNumber;
    }

    public String getBankBranch() {
        return bank_branch;
    }

    public void setBankBranch(String bankBranch) {
        this.bank_branch = bankBranch;
    }
}

