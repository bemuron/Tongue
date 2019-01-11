package viola1.agrovc.com.tonguefinal.data.network;

import com.google.gson.annotations.SerializedName;

public class ConfirmedRequestResult {

    @SerializedName("confirmedRequestDetails")
    private PendingRequest confirmedRequestDetails;

    @SerializedName("userConfirmedRequestDetails")
    private PendingRequest userConfirmedRequestDetails;

    @SerializedName("userMeetingDetails")
    private PendingRequest userMeetingDetails;

    @SerializedName("tutorMeetingDetails")
    private PendingRequest tutorMeetingDetails;

    public ConfirmedRequestResult(PendingRequest confirmedRequestDetails, PendingRequest userConfirmedRequestDetails,
                                  PendingRequest userMeetingDetails, PendingRequest tutorMeetingDetails){
        this.confirmedRequestDetails = confirmedRequestDetails;
        this.userConfirmedRequestDetails = userConfirmedRequestDetails;
        this.userMeetingDetails = userMeetingDetails;
        this.tutorMeetingDetails = tutorMeetingDetails;
    }

    public PendingRequest getConfirmedRequestDetails() {
        return confirmedRequestDetails;
    }

    public PendingRequest getUserConfirmedRequestDetails() {
        return userConfirmedRequestDetails;
    }

    public PendingRequest getUserMeetingDetails() {
        return userMeetingDetails;
    }

    public PendingRequest getTutorMeetingDetails() {
        return tutorMeetingDetails;
    }
}
