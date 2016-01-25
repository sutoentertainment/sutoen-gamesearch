package com.sutoen.sutogamesearch.models;


import com.google.gson.annotations.SerializedName;

/**
 * The object for getting response from the G2A service
 */
public class G2AQuickSearchModel {
    @SerializedName("docs")
    private DealModel[] mDeals;

    public DealModel[] getDeals() {
        return mDeals;
    }

    public void setDeals(DealModel[] deals) {
        this.mDeals = deals;
    }
}
