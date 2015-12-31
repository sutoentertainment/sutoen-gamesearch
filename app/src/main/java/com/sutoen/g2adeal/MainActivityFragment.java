package com.sutoen.g2adeal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private RecyclerView m_dealsRecyclerView;
    private DealAdapter m_dealAdapter;
    private LinearLayoutManager m_linearLayoutManager;
    private TextView m_emptyTextView;


    private FetchDealsTask m_fetchDealsTask;

    private final int NUM_OF_ITEMS_IN_SINGLE_LOAD = 10;

    private List<Deal> m_dealsList;
    private List<Deal> m_newLoadedList;

    protected Handler m_handler;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    public static MainActivityFragment newInstance() {
        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("ducanh", "onCreate");
        super.onCreate(savedInstanceState);
        m_newLoadedList = new ArrayList<>();
        m_fetchDealsTask = new FetchDealsTask();
        m_fetchDealsTask.execute(Integer.toString(0), Integer.toString(NUM_OF_ITEMS_IN_SINGLE_LOAD));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ducanh", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        m_dealsRecyclerView = (RecyclerView) rootView.findViewById(R.id.deals_recycler_view);
        m_dealsRecyclerView.setHasFixedSize(true);
        m_linearLayoutManager = new LinearLayoutManager(getContext());
        m_linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        m_dealsRecyclerView.setLayoutManager(m_linearLayoutManager);
        m_emptyTextView = (TextView) rootView.findViewById(R.id.empty_textview);
        m_dealsList = new ArrayList<>();

        // Load a place holder before loading in order for app look more responsive
        for (int i = 0; i < NUM_OF_ITEMS_IN_SINGLE_LOAD; ++i) {
            m_dealsList.add(new Deal(Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888), android.R.drawable.btn_star_big_on, "added", 1, "eur", "buy", "/slug"));
        }

        m_dealsList.addAll(m_newLoadedList);
        m_dealAdapter = new DealAdapter(m_dealsList, m_dealsRecyclerView);
        m_dealsRecyclerView.setAdapter(m_dealAdapter);

        m_handler = new Handler();

        m_dealAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i("ducanhView", "handler");
                //add null , so the adapter will check view_type and show progress bar at bottom
                m_dealsList.add(null);
                m_dealAdapter.notifyItemInserted(m_dealsList.size() - 1);

                m_handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        m_dealsList.remove(m_dealsList.size() - 1);
                        m_dealAdapter.notifyItemRemoved(m_dealsList.size());
                        //add items one by one
                        int start = m_dealsList.size();
                        int end = start + NUM_OF_ITEMS_IN_SINGLE_LOAD;

                        m_fetchDealsTask = new FetchDealsTask();
                        m_fetchDealsTask.execute(Integer.toString(start+1), Integer.toString(NUM_OF_ITEMS_IN_SINGLE_LOAD));
//
//                        for (int i = start +1; i <= end; ++i) {
//                            Log.i("ducanhView", "add deal");
//                            m_dealsList.add(new Deal("", android.R.drawable.btn_star_big_on, "added", 1, "eur", "buy", "/slug"));
//                            m_dealAdapter.notifyItemInserted(m_dealsList.size());
//                        }
                        m_dealsList.addAll(m_newLoadedList);
                        m_dealAdapter.notifyDataSetChanged();
                        m_dealAdapter.setLoaded();
                    }
                }, 2000);

            }
        });


        if (m_dealsList.isEmpty()) {
            m_dealsRecyclerView.setVisibility(View.GONE);
            m_emptyTextView.setVisibility(View.VISIBLE);
        } else {
            m_dealsRecyclerView.setVisibility(View.VISIBLE);
            m_emptyTextView.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        Log.i("ducanh", "onstart");
        super.onStart();
    }

    public class FetchDealsTask extends AsyncTask<String, Void, List<Deal>> {

        private int isStartAtBegin;

        private List<Deal> getDealsDataFromJson(String dealJsonString)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String KEY_DOCS = "docs";
            final String KEY_NAME = "name";
            final String KEY_SLUG = "slug";
            final String KEY_MIN_PRICE = "minPrice";
            final String KEY_THUMBNAIL = "thumbnail";

            JSONObject dealJson = new JSONObject(dealJsonString);
            JSONArray dealsArray = dealJson.getJSONArray(KEY_DOCS);

            List<Deal> results = new ArrayList<>();

            for (int i = 0; i < dealsArray.length(); i++) {
                float price = (float) dealsArray.getJSONObject(i).getDouble(KEY_MIN_PRICE);
                String title = dealsArray.getJSONObject(i).getString(KEY_NAME);
                String slug = dealsArray.getJSONObject(i).getString(KEY_SLUG);
                String picSrc = dealsArray.getJSONObject(i).getString(KEY_THUMBNAIL);;

                Deal currentDeal = new Deal();
                currentDeal.setPrice(price);
                currentDeal.setTitle(title);
                currentDeal.setSlug(slug);
                Bitmap picSrcBitmap =  downloadBitmap(picSrc);
                currentDeal.setPicSource(picSrcBitmap);
                currentDeal.setIcFavSource(android.R.drawable.btn_star);
                currentDeal.setBuyButtonText(getString(R.string.buy_button_text));
                currentDeal.setPriceUnit("€");
                results.add(currentDeal);
            }
            return results;
        }

        @Override
        protected List<Deal> doInBackground(String... params) {

            Log.i("ducanh", "do in background");
            isStartAtBegin = Integer.parseInt(params[0]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the G2A query
                final String DEAL_QUICK_SEARCH_URL = "https://www.g2a.com/lucene/search/quick";
                final String START_PARAM = "start";
                final String ROWS_PARAM = "rows";

                Uri uri = Uri.parse(DEAL_QUICK_SEARCH_URL).buildUpon()
                        .appendQueryParameter(START_PARAM, params[0])
                        .appendQueryParameter(ROWS_PARAM, params[1])
                        .build();

                URL url = new URL(uri.toString());
                // Create the request to G2A, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getLocalizedMessage());
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getDealsDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Deal> deals) {
            super.onPostExecute(deals);

            if(deals != null){
                if(isStartAtBegin == 0) {
                    Log.i("ducanh", "m_newLoadedList == null");
                    m_dealsList.clear();
                    m_dealsList.addAll(deals);
                } else {
                    m_newLoadedList = null;
                    m_newLoadedList = new ArrayList<>();
                    m_newLoadedList.addAll(deals);
                }

            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            Log.d("URLCONNECTIONERROR", e.toString());
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
        return null;
    }
}
