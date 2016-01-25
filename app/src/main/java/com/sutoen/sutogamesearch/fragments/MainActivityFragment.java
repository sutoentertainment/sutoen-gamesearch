package com.sutoen.sutogamesearch.fragments;

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

import com.sutoen.sutogamesearch.models.DealModel;
import com.sutoen.sutogamesearch.models.G2AQuickSearchModel;
import com.sutoen.sutogamesearch.network.ApiConstants;
import com.sutoen.sutogamesearch.network.G2AService;
import com.sutoen.sutogamesearch.views.adapters.DealAdapter;
import com.sutoen.sutogamesearch.interfaces.OnLoadMoreListener;
import com.sutoen.sutogamesearch.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A main fragment to show quick search results in CardViews contained in RecyclerView
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private RecyclerView mDealsRecyclerView;
    private DealAdapter mDealAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FetchDealsTask mFetchDealsTask;
    private Retrofit mRetrofit;
    private G2AService mG2AService;

    protected Handler handler;

    private final int NUM_OF_ITEMS_IN_SINGLE_LOAD = 10;

    private List<DealModel> mDealsList;
    private List<DealModel> mNewLoadedList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.G2A_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mG2AService = mRetrofit.create(G2AService.class);
        mNewLoadedList = new ArrayList<>();
        mFetchDealsTask = new FetchDealsTask();
        mFetchDealsTask.execute(Integer.toString(0), Integer.toString(NUM_OF_ITEMS_IN_SINGLE_LOAD));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mDealsRecyclerView = (RecyclerView) rootView.findViewById(R.id.deals_recycler_view);
        mDealsRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDealsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDealsList = new ArrayList<>();

        // Load place holders deal before loading in order for app look more responsive
        for (int i = 0; i < NUM_OF_ITEMS_IN_SINGLE_LOAD; ++i) {
            mDealsList.add(new DealModel());
        }

        mDealsList.addAll(mNewLoadedList);
        mDealAdapter = new DealAdapter(mDealsList, mDealsRecyclerView);
        mDealsRecyclerView.setAdapter(mDealAdapter);

        handler = new Handler();

        mDealAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                mDealsList.add(null);
                mDealAdapter.notifyItemInserted(mDealsList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        mDealsList.remove(mDealsList.size() - 1);
                        mDealAdapter.notifyItemRemoved(mDealsList.size());

                        // Add new items to the list
                        int start = mDealsList.size();
                        mFetchDealsTask = new FetchDealsTask();
                        mFetchDealsTask.execute(Integer.toString(start+1), Integer.toString(NUM_OF_ITEMS_IN_SINGLE_LOAD));

                        mDealsList.addAll(mNewLoadedList);
                        mDealAdapter.notifyDataSetChanged();
                        mDealAdapter.setLoaded();
                    }
                }, 2000);

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * This class is executed in background in order to load deals getting from the server
     * using Retrofit2 synchronous call, alternatively, we can use Retrofit2 asynchronous
     */
    public class FetchDealsTask extends AsyncTask<String, Void, List<DealModel>> {

        private int isStartAtBegin;

        @Override
        protected List<DealModel> doInBackground(String... params) {
            isStartAtBegin = Integer.parseInt(params[0]);

            // Getting results from the server by using synchronous call of Retrofit2 client
            List<DealModel> results = new ArrayList<>();
            try {
                Response<G2AQuickSearchModel> response = (mG2AService.getDeals()).execute();
                if (response.code() == 200) {
                    List<DealModel> docModels = Arrays.asList(response.body().getDeals());
                    for(DealModel deal : docModels) {
                        deal.setPriceUnit("€");
                    }
                    results.addAll(docModels);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, e.getLocalizedMessage());
            }
            return results;
        }

        @Override
        protected void onPostExecute(final List<DealModel> deals) {
            super.onPostExecute(deals);

            // if the deals first load add all deals to the list
            // otherwise add all new deals loaded to mNewLoadedList in order to use for load more
            if(deals != null){
                if(isStartAtBegin == 0) {
                    mDealsList.clear();
                    mDealsList.addAll(deals);
                } else {
                    mNewLoadedList = null;
                    mNewLoadedList = new ArrayList<>();
                    mNewLoadedList.addAll(deals);
                }

            }
        }
    }
}
