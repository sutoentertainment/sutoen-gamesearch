package com.sutoen.sutogamesearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The deal model loaded from the server
 */
public class DealModel {
    @SerializedName("thumbnail")
    private String mThumbnail;
    @SerializedName("name")
    private String mTitle;
    @SerializedName("minPrice")
    private float mPrice;
    @Expose(serialize = false, deserialize = false)
    private String mPriceUnit;
    @SerializedName("slug")
    private String mSlug;

    public DealModel() {}

    public DealModel(String title, float price, String priceUnit, String slug) {
        mTitle = title;
        mPrice = price;
        mPriceUnit = priceUnit;
        mSlug = slug;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        mPrice = price;
    }

    public String getPriceUnit() {
        return mPriceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        mPriceUnit = priceUnit;
    }

    public String getSlug() {
        return mSlug;
    }

    public void setSlug(String slug) {
        mSlug = slug;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "DealModel: " + mTitle;
    }
}
