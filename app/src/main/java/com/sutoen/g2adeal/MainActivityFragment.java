package com.sutoen.g2adeal;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private List<Deal> m_dealsList;


    protected Handler m_handler;

    /** The CardView widget. */
    //@VisibleForTesting
    CardView mCardView;

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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        m_dealsRecyclerView = (RecyclerView) rootView.findViewById(R.id.deals_recycler_view);
        m_dealsRecyclerView.setHasFixedSize(true);
        m_linearLayoutManager = new LinearLayoutManager(getContext());
        m_linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        m_dealsRecyclerView.setLayoutManager(m_linearLayoutManager);
        m_emptyTextView = (TextView) rootView.findViewById(R.id.empty_textview);
        m_dealsList = new ArrayList<>();
        m_dealsList = createDealsList(10);
        m_dealAdapter = new DealAdapter(m_dealsList, m_dealsRecyclerView);
        m_dealsRecyclerView.setAdapter(m_dealAdapter);


        if (m_dealsList.isEmpty()) {
            m_dealsRecyclerView.setVisibility(View.GONE);
            m_emptyTextView.setVisibility(View.VISIBLE);

        } else {
            m_dealsRecyclerView.setVisibility(View.VISIBLE);
            m_emptyTextView.setVisibility(View.GONE);
        }

        // implement load more for the adapter
        m_handler = new Handler();

        m_dealAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
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
                        int end = start + 10;

                        for (int i = start + 1; i <= end; i++) {
                            m_dealsList.addAll(createDealsList(1));
                            m_dealAdapter.notifyItemInserted(m_dealsList.size());
                        }
                        m_dealAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });


        return rootView;
    }


    //create Deals List to test out the layout
    private List<Deal> createDealsList(int size) {

        List<Deal> result = new ArrayList<Deal>();
        for (int i=1; i <= size; i++) {
            Deal currentDeal = new Deal();
            currentDeal.setPicSource(android.R.drawable.sym_def_app_icon);
            currentDeal.setIcFavSource(android.R.drawable.btn_star);
            currentDeal.setBuyButtonText(getString(R.string.buy_button_text));
            currentDeal.setTitle("Title " + i);;
            currentDeal.setPrice(i);
            currentDeal.setPriceUnit("$");
            result.add(currentDeal);
        }

        return result;
    }
}
