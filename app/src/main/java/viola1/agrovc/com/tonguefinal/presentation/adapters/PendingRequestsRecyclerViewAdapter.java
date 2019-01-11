package viola1.agrovc.com.tonguefinal.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.data.network.PendingRequest;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.TutorPendingRequestsListFragment;
import viola1.agrovc.com.tonguefinal.presentation.ui.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link TutorPendingRequestsListFragment.OnPendingRequestListInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PendingRequestsRecyclerViewAdapter extends RecyclerView.Adapter<PendingRequestsRecyclerViewAdapter.ViewHolder> {

    private final List<PendingRequest> pendingRequestList;
    private final PendingRequestListenerAdapter mListener;
    private Context context;

    public PendingRequestsRecyclerViewAdapter(Context context, List<PendingRequest> pendingRequestList, PendingRequestListenerAdapter listener) {
        this.pendingRequestList = pendingRequestList;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_request_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PendingRequest pendingRequest = pendingRequestList.get(position);
        //holder.mItem = pendingRequestList.get(position);
        holder.userName.setText(pendingRequest.getName());
        holder.requestFor.append(" " + pendingRequest.getLanguage_name());
        holder.dateCreated.append(" " + pendingRequest.getCreated_on());

        // displaying the first letter of From in icon text
        holder.iconText.setText(pendingRequest.getName().substring(0, 1));

        // display profile image
        applyProfilePicture(holder, pendingRequest);

        // apply click events
        applyClickEvents(holder, position);

    }

    //handling different click events
    private void applyClickEvents(PendingRequestsRecyclerViewAdapter.ViewHolder holder, final int position) {
        /*holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });*/

        holder.pendingRequestContainer.setOnClickListener(view -> mListener.onPendingRequestRowClicked(position));
    }

    private void applyProfilePicture(PendingRequestsRecyclerViewAdapter.ViewHolder holder, PendingRequest pendingRequest) {
        if (!TextUtils.isEmpty(pendingRequest.getProfile_pic())) {
            Glide.with(context).load("http://apps.incubation.billbrain.tech/tongueApp/assets/images/profile_pics/"+pendingRequest.getProfile_pic())
                    .thumbnail(0.5f)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(pendingRequest.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, requestFor, iconText, dateCreated;
        public CircleImageView imgProfile;
        public LinearLayout pendingRequestContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public ViewHolder(View view) {
            super(view);
            pendingRequestContainer = view.findViewById(R.id.pending_request_container);
            userName = view.findViewById(R.id.text_view_pr_user_name);
            dateCreated = view.findViewById(R.id.text_view_pr_date_created);
            requestFor = view.findViewById(R.id.text_view_pr_language);
            iconText = view.findViewById(R.id.pr_icon_text);
            iconFront = view.findViewById(R.id.pr_icon_front);
            imgProfile = view.findViewById(R.id.pr_icon_profile);
        }

        /*@Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }

    public interface PendingRequestListenerAdapter{
        void onPendingRequestRowClicked(int position);

    }
}
