package com.sutoen.sutogamesearch;

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

import java.util.List;

/**
 * Created by SutoNinka on 27/12/15.
 */
public class DealAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final String G2A_HOME_URL = "https://www.g2a.com";

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 8;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean m_loading;
    private OnLoadMoreListener m_onLoadMoreListener;

    private List<Deal> m_dealsList;

    public DealAdapter(List<Deal> dealsList, RecyclerView recyclerView) {
        m_dealsList = dealsList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!m_loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (m_onLoadMoreListener != null) {
                                    m_onLoadMoreListener.onLoadMore();
                                }
                                m_loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return m_dealsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (m_dealsList.get(position) == null) {
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
            final Deal currentDeal = m_dealsList.get(position);
            ((DealViewHolder) holder).dealPic.setImageBitmap(currentDeal.getPicSource());
            ((DealViewHolder) holder).icFav.setImageResource(currentDeal.getIcFavSource());
            ((DealViewHolder) holder).title.setText(currentDeal.getTitle());
            ((DealViewHolder) holder).price.setText(currentDeal.getPrice() + " " + currentDeal.getPriceUnit());
            ((DealViewHolder) holder).button.setText(currentDeal.getBuyButtonText());
            ((DealViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String toVisitUrl = G2A_HOME_URL + currentDeal.getSlug();
//                    Intent goToItemPageIntent = new Intent(v.getContext(), BuyNowActivity.class);
//                    goToItemPageIntent.putExtra(Intent.EXTRA_TEXT, toVisitUrl);
//                    v.getContext().startActivity(goToItemPageIntent);
//                    Log.i("ducanh", G2A_HOME_URL + currentDeal.getSlug());
                    Intent goToItemPageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(toVisitUrl));
                    v.getContext().startActivity(goToItemPageIntent);
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public void setLoaded() {
        m_loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        m_onLoadMoreListener = onLoadMoreListener;
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {

        ImageView dealPic;
        ImageView icFav;
        TextView title;
        TextView price;
        Button button;

        public DealViewHolder(View singleDeal) {
            super(singleDeal);
            dealPic = (ImageView) singleDeal.findViewById(R.id.deal_picture_imageview);
            icFav = (ImageView) singleDeal.findViewById(R.id.deal_favourite_imageview);
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
