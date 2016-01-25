package com.sutoen.sutogamesearch.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sutoen.sutogamesearch.interfaces.OnLoadMoreListener;
import com.sutoen.sutogamesearch.R;
import com.sutoen.sutogamesearch.models.DealModel;

import java.util.List;


public class DealAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final String G2A_HOME_URL = "https://www.g2a.com";

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private final int VISIBLE_THRESHOLD = 8;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    private List<DealModel> mDealsList;

    public DealAdapter(List<DealModel> dealsList, RecyclerView recyclerView) {
        mDealsList = dealsList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            mTotalItemCount = linearLayoutManager.getItemCount();
                            mLastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!mLoading
                                    && mTotalItemCount <= (mLastVisibleItem + VISIBLE_THRESHOLD)) {
                                // End has been reached
                                // Do something
                                if (mOnLoadMoreListener != null) {
                                    mOnLoadMoreListener.onLoadMore();
                                }
                                mLoading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return mDealsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDealsList.get(position) == null) {
            return VIEW_PROG;
        }
        return VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder currentViewHolder;

        if (viewType == VIEW_ITEM) {
            View singleDealView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_single_deal, parent, false);
            currentViewHolder = new DealViewHolder(singleDealView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_progress_bar, parent, false);
            currentViewHolder = new ProgressViewHolder(v);
        }
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DealViewHolder) {
            final DealModel currentDeal = mDealsList.get(position);

            // Load the thumbnail into the place holder using Picasso
            // if the thumbnail is not loaded using the place holder which is this app's logo
            Context dealThumbnailContext = ((DealViewHolder) holder).dealPic.getContext();
            String thumbnailUrl = currentDeal.getThumbnail();
            Picasso.with(dealThumbnailContext)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.logo)
                    .resize(50, 50)
                    .centerCrop()
                    .into(((DealViewHolder) holder).dealPic);
            ((DealViewHolder) holder).title.setText(currentDeal.getTitle());
            ((DealViewHolder) holder).price.setText(currentDeal.getPrice() + " " + currentDeal.getPriceUnit());
            ((DealViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String toVisitUrl = G2A_HOME_URL + currentDeal.getSlug();
                    Intent goToItemPageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(toVisitUrl));
                    v.getContext().startActivity(goToItemPageIntent);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public void setLoaded() {
        mLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {

        ImageView dealPic;
        TextView title;
        TextView price;
        Button button;

        public DealViewHolder(View singleDeal) {
            super(singleDeal);
            dealPic = (ImageView) singleDeal.findViewById(R.id.deal_picture_imageview);
            title = (TextView) singleDeal.findViewById(R.id.deal_title_textView);
            price = (TextView) singleDeal.findViewById(R.id.deal_price_textView);
            button = (Button) singleDeal.findViewById(R.id.deal_buy_button);
        }

    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.deal_loading_progressbar);
        }
    }
}
