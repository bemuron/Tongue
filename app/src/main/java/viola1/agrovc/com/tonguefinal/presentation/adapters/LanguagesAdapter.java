package viola1.agrovc.com.tonguefinal.presentation.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import viola1.agrovc.com.tonguefinal.R;
import viola1.agrovc.com.tonguefinal.data.database.Language;
import viola1.agrovc.com.tonguefinal.data.network.Tutor;
import viola1.agrovc.com.tonguefinal.presentation.ui.activities.TutorActivity;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.LanguagesAdapterViewHolder>{

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    // The context we use to utility methods, app resources and layout inflaters
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onItemClick method whenever
     * an item is clicked in the list.
     */
    private final LanguagesAdapterOnItemClickHandler mClickHandler;
    /*
     * Flag to determine if we want to use a separate view for the list item that represents
     * today. This flag will be true when the phone is in portrait mode and false when the phone
     * is in landscape. This flag will be set in the constructor of the adapter by accessing
     * boolean resources.
     */
    //private final boolean mUseTodayLayout;
    private List<Language> languageList;

    /**
     * Creates a LanguagesAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public LanguagesAdapter(@NonNull Context context, LanguagesAdapterOnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        //mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public LanguagesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //int layoutId = getLayoutIdByType(viewType);
        View view = LayoutInflater.from(mContext).inflate(R.layout.languages_grid_item, viewGroup, false);
        view.setFocusable(true);
        return new LanguagesAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param languagesAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull LanguagesAdapterViewHolder languagesAdapterViewHolder, int position) {
        Language currentLanguage = languageList.get(position);

        String catName = currentLanguage.getName();
        languagesAdapterViewHolder.languageName.setText(catName);

        currentLanguage.setColor(getRandomMaterialColor("400"));

        // displaying the first letter of From in icon text
        //languagesAdapterViewHolder.languageName.setText(currentLanguage.getName().substring(0, 1));

        //displaying random color
        applyRandomColor(languagesAdapterViewHolder, currentLanguage);

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of categories
     */
    @Override
    public int getItemCount() {
        if (null == languageList) return 0;
        return languageList.size();
    }

    /**
     * Returns an integer code related to the type of View we want the ViewHolder to be at a given
     * position. This method is useful when we want to use different layouts for different items
     * depending on their position. In Sunshine, we take advantage of this method to provide a
     * different layout for the "today" layout. The "today" layout is only shown in portrait mode
     * with the first item in the list.
     *
     * @param position index within our RecyclerView and list
     * @return the view type (today or future day)
     */
    /*
    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }
    */

    /**
     * Swaps the list used by the CategoriesAdapter for its categories data. This method is called by
     * {@link viola1.agrovc.com.tonguefinal.presentation.ui.activities.HomeActivity} after a load
     * has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newLanguages the new list of languages to use as CategoriesAdapter's data source
     */
    public void swapForecast(final List<Language> newLanguages) {
        // If there was no forecast data, then recreate all of the list
        if (languageList == null) {
            languageList = newLanguages;
            notifyDataSetChanged();
        }
            /*
        } else {
            /*
             * Otherwise we use DiffUtil to calculate the changes and update accordingly. This
             * shows the four methods you need to override to return a DiffUtil callback. The
             * old list is the current list stored in mForecast, while the new list is the new
             * values passed in from observing the database.
             */

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return languageList.size();
                }

                @Override
                public int getNewListSize() {
                    return newLanguages.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return languageList.get(oldItemPosition).getLanguage_id() ==
                            newLanguages.get(newItemPosition).getLanguage_id();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Language newLanguage = newLanguages.get(newItemPosition);
                    Language oldLanuage = languageList.get(oldItemPosition);
                    return newLanguage.getLanguage_id() == oldLanuage.getLanguage_id();
                           // && newCategory.getCategoryName().equals(oldCategory.getCategoryName());
                }
            });
            languageList = newLanguages;
            result.dispatchUpdatesTo(this);
        }

        /**
         * get the language picture if available otherwise set a random color
         * */
    private void applyRandomColor(LanguagesAdapterViewHolder holder, Language language) {
            holder.languageImage.setImageResource(R.drawable.bg_square);
            holder.languageImage.setColorFilter(language.getColor());
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface LanguagesAdapterOnItemClickHandler {
        void onItemClick(int language_id, String languageName);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class LanguagesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView languageImage;
        final TextView languageName;
        public FrameLayout frameLayout;

        LanguagesAdapterViewHolder(View view) {
            super(view);

            languageImage = view.findViewById(R.id.language_pic);
            languageName = view.findViewById(R.id.language_name);
            frameLayout = view.findViewById(R.id.gridItemContainer);


            languageImage.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onItemClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int languageId = languageList.get(adapterPosition).getLanguage_id();
            String languageName = languageList.get(adapterPosition).getName();
            mClickHandler.onItemClick(languageId, languageName);
        }
    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + typeColor, "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
}
