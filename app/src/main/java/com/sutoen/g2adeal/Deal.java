package com.sutoen.g2adeal;

/**
 * Created by SutoNinka on 27/12/15.
 */
public class Deal {
    private String m_picSource;
    private int m_icFavSource;
    private String m_title;
    private float m_price;
    private String m_priceUnit;
    private String m_slug;

    public Deal() {}

    public Deal(String picSource, int icFavSource, String title, float price, String priceUnit, String buyButtonText,
                String slug) {
        m_picSource = picSource;
        m_icFavSource = icFavSource;
        m_title = title;
        m_price = price;
        m_priceUnit = priceUnit;
        m_buyButtonText = buyButtonText;
        m_slug = slug;
    }

    public String getBuyButtonText() {
        return m_buyButtonText;
    }

    public void setBuyButtonText(String buyButtonText) {
        m_buyButtonText = buyButtonText;
    }

    private String m_buyButtonText;

    public String getPicSource() {
        return m_picSource;
    }

    public void setPicSource(String picSource) {
        m_picSource = picSource;
    }

    public int getIcFavSource() {
        return m_icFavSource;
    }

    public void setIcFavSource(int icFavSource) {
        m_icFavSource = icFavSource;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        m_title = title;
    }

    public float getPrice() {
        return m_price;
    }

    public void setPrice(float price) {
        m_price = price;
    }

    public String getPriceUnit() {
        return m_priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        m_priceUnit = priceUnit;
    }

    public String getSlug() {
        return m_slug;
    }

    public void setSlug(String slug) {
        m_slug = slug;
    }

    @Override
    public String toString() {
        return "Deal: " + m_title;
    }
}
