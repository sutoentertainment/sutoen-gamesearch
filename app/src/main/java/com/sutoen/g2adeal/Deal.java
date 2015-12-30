package com.sutoen.g2adeal;

/**
 * Created by SutoNinka on 27/12/15.
 */
public class Deal {
    private int m_picSource;
    private int m_icFavSource;
    private String m_title;
    private float m_price;
    private String m_priceUnit;

    public Deal(int picSource, int icFavSource, String title, float price, String priceUnit, String buyButtonText) {
        m_picSource = picSource;
        m_icFavSource = icFavSource;
        m_title = title;
        m_price = price;
        m_priceUnit = priceUnit;
        m_buyButtonText = buyButtonText;
    }

    public String getBuyButtonText() {
        return m_buyButtonText;
    }

    public void setBuyButtonText(String m_buyButtonText) {
        this.m_buyButtonText = m_buyButtonText;
    }

    private String m_buyButtonText;

    public int getPicSource() {
        return m_picSource;
    }

    public void setPicSource(int m_picSource) {
        this.m_picSource = m_picSource;
    }

    public int getIcFavSource() {
        return m_icFavSource;
    }

    public void setIcFavSource(int m_icFavSource) {
        this.m_icFavSource = m_icFavSource;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String m_title) {
        this.m_title = m_title;
    }

    public float getPrice() {
        return m_price;
    }

    public void setPrice(float m_price) {
        this.m_price = m_price;
    }

    public String getPriceUnit() {
        return m_priceUnit;
    }

    public void setPriceUnit(String m_priceUnit) {
        this.m_priceUnit = m_priceUnit;
    }

    @Override
    public String toString() {
        return "Deal: " + m_title;
    }
}
