package com.sutoen.sutogamesearch.network;

import com.sutoen.sutogamesearch.models.G2AQuickSearchModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface G2AService {
    @GET(ApiConstants.G2A_DEAL_QUICK_SEARCH_URL)
    Call<G2AQuickSearchModel> getDeals(@Query("start") String start, @Query("rows") String rows);
}
