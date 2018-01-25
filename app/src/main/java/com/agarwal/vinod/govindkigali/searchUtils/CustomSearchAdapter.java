package com.agarwal.vinod.govindkigali.searchUtils;

import android.content.Context;
import android.view.View;

import com.agarwal.vinod.govindkigali.models.Song;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anirudh on 25/1/18.
 */

//ABONDENED

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CustomSearchAdapter extends RecyclerView.Adapter<CustomSearchAdapter.ResultViewHolder> implements Filterable {

    protected final SearchHistoryTable mHistoryDatabase;
    public Integer mDatabaseKey = null;
    protected CharSequence mKey = "";
    protected List<SearchItem> mSuggestions = new ArrayList<>();
    protected List<SearchItem> mResults = new ArrayList<>();
    protected com.lapism.searchview.SearchAdapter.OnSearchItemClickListener mListener;

    public CustomSearchAdapter(Context context) {
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    public CustomSearchAdapter(Context context, List<SearchItem> suggestions) {
        mSuggestions = suggestions;
        mResults = suggestions;
        mHistoryDatabase = new SearchHistoryTable(context);
        getFilter().filter("");
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(com.lapism.searchview.R.layout.search_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        SearchItem item = mResults.get(position);

        viewHolder.icon.setImageResource(item.getIconResource());
        viewHolder.icon.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN);
        viewHolder.text.setTypeface((Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())));
        viewHolder.text.setTextColor(SearchView.getTextColor());

        String itemText = item.getText().toString();
        String itemTextLower = itemText.toLowerCase(Locale.getDefault());

        if (itemTextLower.contains(mKey) && !TextUtils.isEmpty(mKey)) {
            SpannableString s = new SpannableString(itemText);
            s.setSpan(new ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(mKey.toString()), itemTextLower.indexOf(mKey.toString()) + mKey.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.text.setText(item.getText());
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Filter getFilter() {
        return new Filter() {
            protected CharSequence mFilterKey;

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                mKey = constraint.toString().toLowerCase(Locale.getDefault());
                mFilterKey = mKey;

                if (!TextUtils.isEmpty(constraint)) {
                    List<SearchItem> results = new ArrayList<>();
                    List<SearchItem> history = new ArrayList<>();
                    List<SearchItem> databaseAllItems = mHistoryDatabase.getAllItems(mDatabaseKey);

                    if (!databaseAllItems.isEmpty()) {
                        history.addAll(databaseAllItems);
                    }
                    history.addAll(mSuggestions);

                    for (SearchItem item : history) {
                        String string = item.getText().toString().toLowerCase(Locale.getDefault());
                        if (string.contains(mKey)) {
                            results.add(item);
                        }
                    }

                    if (results.size() > 0) {
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (mFilterKey.equals(mKey)) {
                    List<SearchItem> dataSet = new ArrayList<>();

                    if (results.count > 0) {
                        List<?> result = (ArrayList<?>) results.values;
                        for (Object object : result) {
                            if (object instanceof SearchItem) {
                                dataSet.add((SearchItem) object);
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(mKey)) {
                            List<SearchItem> allItems = mHistoryDatabase.getAllItems(mDatabaseKey);
                            if (!allItems.isEmpty()) {
                                dataSet = allItems;
                            }
                        }
                    }

                    setData(dataSet);
                }
            }
        };
    }

    // ---------------------------------------------------------------------------------------------
    public List<SearchItem> getSuggestionsList() {
        return mSuggestions;
    }

    public void setSuggestionsList(List<SearchItem> suggestionsList) {
        mSuggestions = suggestionsList;
        mResults = suggestionsList;
    }

    public List<SearchItem> getResultsList() {
        return mResults;
    }

    public CharSequence getKey() {
        return mKey;
    }

    public void setDatabaseKey(Integer key) {
        mDatabaseKey = key;
        getFilter().filter("");
    }

    public void setData(List<SearchItem> data) {
        if (mResults.isEmpty()) {
            mResults = data;
            if (data.size() != 0) {
                notifyItemRangeInserted(0, data.size());
            }
        } else {
            int previousSize = mResults.size();
            int nextSize = data.size();
            mResults = data;
            if (previousSize == nextSize && nextSize != 0) {
                notifyItemRangeChanged(0, previousSize);
            } else if (previousSize > nextSize) {
                if (nextSize == 0) {
                    notifyItemRangeRemoved(0, previousSize);
                } else {
                    notifyItemRangeChanged(0, nextSize);
                    notifyItemRangeRemoved(nextSize - 1, previousSize);
                }
            } else {
                notifyItemRangeChanged(0, previousSize);
                notifyItemRangeInserted(previousSize, nextSize - previousSize);
            }
        }
    }

    public void setOnSearchItemClickListener(com.lapism.searchview.SearchAdapter.OnSearchItemClickListener listener) {
        mListener = listener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position, String text);
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        protected final ImageView icon;
        protected final TextView text;

        public ResultViewHolder(View view) {
            super(view);
            icon = view.findViewById(com.lapism.searchview.R.id.search_icon);
            text = view.findViewById(com.lapism.searchview.R.id.search_text);
            // add second text
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onSearchItemClick(view, getLayoutPosition(), text.getText().toString());
                    }
                }
            });
        }

    }

}


/*
public class CustomSearchAdapter extends SearchAdapter {

    ArrayList<Song> songs;
    OnCustomSearchItemClickListener customMListener;
    public CustomSearchAdapter(Context context, ArrayList<Song> songs) {
        super(context);
        this.songs = songs;
    }

    public CustomSearchAdapter(Context context, List<SearchItem> suggestions, ArrayList<Song> songs) {
        super(context, suggestions);
        this.songs = songs;
    }

    public interface OnCustomSearchItemClickListener {
        void onCustomSearchItemClick(View view, int position, String text);
    }

    public void setOnSearchItemClickListener(OnCustomSearchItemClickListener listener) {
        customMListener = listener;
        mListener = new OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(View view, int position, String text) {
                customMListener.onCustomSearchItemClick(view,position,text);
            }
        };
    }
}
*/
