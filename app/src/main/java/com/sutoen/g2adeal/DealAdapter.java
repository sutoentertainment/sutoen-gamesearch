package com.sutoen.g2adeal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SutoNinka on 27/12/15.
 */
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    private List<Deal> m_DealsList;

    public DealAdapter(List<Deal> dealsList) {
        m_DealsList = dealsList;
    }

    @Override
    public int getItemCount() {
        return m_DealsList.size();
    }

    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleDealView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_deal, parent, false);
        return new DealViewHolder(singleDealView);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        Deal currentDeal = m_DealsList.get(position);
        holder.dealPic.setImageResource(currentDeal.getPicSource());
        holder.icFav.setImageResource(currentDeal.getIcFavSource());
        holder.title.setText(currentDeal.getTitle());
        holder.price.setText(currentDeal.getPrice() + " " + currentDeal.getPriceUnit());
        holder.button.setText("Buy now");
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
}
