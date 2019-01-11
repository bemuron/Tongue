package viola1.agrovc.com.tonguefinal.data.network;

import java.util.ArrayList;

public class PendingRequests {
    private ArrayList<PendingRequest> pending_requests;
    private ArrayList<PendingRequest> confirmed_requests;

    public PendingRequests() {

    }

    public ArrayList<PendingRequest> getPendingRequestList() {
        return pending_requests;
    }

    public void setPendingRequests(ArrayList<PendingRequest> pending_requests) {
        this.pending_requests = pending_requests;
    }

    public ArrayList<PendingRequest> getConfirmed_requests() {
        return confirmed_requests;
    }

    public void setConfirmed_requests(ArrayList<PendingRequest> confirmed_requests) {
        this.confirmed_requests = confirmed_requests;
    }
}
