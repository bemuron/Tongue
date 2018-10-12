package viola1.agrovc.com.tonguefinal.presentation.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import viola1.agrovc.com.tonguefinal.R;

import java.util.List;

import viola1.agrovc.com.tonguefinal.data.network.Tutor;

/**
 * Created by Emo on 5/10/2017.
 */

public class TutorListAdapter extends RecyclerView.Adapter<TutorListAdapter.MyViewHolder> {

    private List<Tutor> tutorList;
    private LayoutInflater inflater;
    Context context;
    private TutorListAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, date, iconText, issueName;
        public ImageView iconImp, imgProfile;
        public LinearLayout tutorContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.text_view_tutor_name);
            content = view.findViewById(R.id.text_view_tutor_desc);
            //date = view.findViewById(R.id.text_view_note_date);
            issueName = view.findViewById(R.id.text_view_tutor_rating);
            iconText = view.findViewById(R.id.icon_text);
            //iconBack = view.findViewById(R.id.icon_back);
            iconFront = view.findViewById(R.id.icon_front);
            //iconImp = view.findViewById(R.id.icon_star);
            imgProfile = view.findViewById(R.id.icon_profile);
            tutorContainer = view.findViewById(R.id.tutor_container);
            iconContainer = view.findViewById(R.id.icon_container);
            //view.setOnLongClickListener(this);
        }
    }

    public TutorListAdapter(Context context, List<Tutor> tutors, TutorListAdapterListener listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.tutorList = tutors;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tutor_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Tutor tutor = tutorList.get(position);

        // displaying text view data
        holder.title.setText(tutor.getName());
        holder.content.setText(tutor.getDescription());
        //holder.issueName.setText("Issue: "+ note.getIssueName());

        // displaying the first letter of From in icon text
        holder.iconText.setText(tutor.getName().substring(0, 1));

        // display profile image
        applyProfilePicture(holder, tutor);

        // apply click events
        applyClickEvents(holder, position);

    }

    //handling different click events
    private void applyClickEvents(MyViewHolder holder, final int position) {
        /*holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });*/

        holder.tutorContainer.setOnClickListener(view -> listener.onTutorRowClicked(position));
    }

    private void applyProfilePicture(MyViewHolder holder, Tutor tutor) {
        if (!TextUtils.isEmpty(tutor.getProfile_pic())) {
            Glide.with(context).load(tutor.getProfile_pic())
                    .thumbnail(0.5f)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(tutor.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return tutorList.get(position).getUser_id();
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public interface TutorListAdapterListener {

        void onTutorRowClicked(int position);

    }

}
