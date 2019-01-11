package viola1.agrovc.com.tonguefinal.data.network;

import java.util.ArrayList;

public class UserPendingRequests {
    private ArrayList<PendingRequest> user_pending_requests;

    private ArrayList<PendingRequest> user_confirmed_requests;

    public UserPendingRequests() {

    }

    public ArrayList<PendingRequest> getUserPendingRequestList() {
        return user_pending_requests;
    }

    public void setUserPendingRequests(ArrayList<PendingRequest> user_pending_requests) {
        this.user_pending_requests = user_pending_requests;
    }

    public ArrayList<PendingRequest> getUser_confirmed_requests() {
        return user_confirmed_requests;
    }

    public void setUser_confirmed_requests(ArrayList<PendingRequest> user_confirmed_requests) {
        this.user_confirmed_requests = user_confirmed_requests;
    }
}
