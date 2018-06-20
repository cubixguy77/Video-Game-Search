package com.robsessions.videogamesearch.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The contract for the gateway to the Giant Bombs API
 */
public interface GameService {

    @GET("/api/search?api_key=b6fbed01a4cb308be2ef201901707bddaed26132&format=json&resources=game&field_list=name,description,deck,image")
    Call<GameList> searchGames(@Query("query") String query, @Query("page") int page);

    @GET("/api/games?api_key=b6fbed01a4cb308be2ef201901707bddaed26132&format=json&resources=game&field_list=name,deck,description,image&limit=10&sort=date_added:desc")
    Call<GameList> getLatestGames();
}
