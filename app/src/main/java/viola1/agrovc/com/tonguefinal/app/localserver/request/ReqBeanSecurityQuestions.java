package viola1.agrovc.com.tonguefinal.app.localserver.request;

/**
 * Created by VIOLA1 on 23-Apr-18.
 */

public class ReqBeanSecurityQuestions {
    private String answerOne;
    private String answerTwo;
    private String answerThree;

    private String member_email;
    public ReqBeanSecurityQuestions() {

    }

    public String getAnswerOne() {
        return answerOne;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }



    public String getAnswerTwo() {
        return answerTwo;
    }
    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }



    public String getMember_email() {
        return member_email;
    }

    public void setMember_email(String member_email) {
        this.member_email = member_email;
    }
}

