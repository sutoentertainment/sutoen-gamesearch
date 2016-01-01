package com.sutoen.sutogamesearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A placeholder fragment containing a simple view.
 */
public class BuyNowActivityFragment extends Fragment {

    public BuyNowActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buy_now, container, false);
        Intent intent = getActivity().getIntent();
        String itemPageUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
        WebView itemPageWebView = (WebView) rootView.findViewById(R.id.buynow_webview);
        itemPageWebView.getSettings().setJavaScriptEnabled(true);
        itemPageWebView.loadUrl(itemPageUrl);
        return rootView;
    }
}
