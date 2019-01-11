package viola1.agrovc.com.tonguefinal.data.network;

import com.google.gson.annotations.SerializedName;

import viola1.agrovc.com.tonguefinal.models.User;

public class PendingRequestResult {

    @SerializedName("pendingRequestDetails")
    private PendingRequest pendingRequestDetails;
    private PendingRequest userPendingRequestDetails;

    public PendingRequestResult(PendingRequest pendingRequestDetails, PendingRequest userPendingRequestDetails){
        this.pendingRequestDetails = pendingRequestDetails;
        this.userPendingRequestDetails = userPendingRequestDetails;

    }

    public PendingRequest getPendingRequestDetails() {
        return pendingRequestDetails;
    }

    public PendingRequest getUserPendingRequestDetails() {
        return userPendingRequestDetails;
    }
}
