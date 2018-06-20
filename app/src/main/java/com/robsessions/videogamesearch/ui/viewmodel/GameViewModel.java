package com.robsessions.videogamesearch.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.robsessions.videogamesearch.network.GameList;
import com.robsessions.videogamesearch.network.GameService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This viewModel maintains an observable list of games to which the view layer can subscribe
 */
public class GameViewModel extends ViewModel {

    private MutableLiveData<GameList> gameList = new MutableLiveData<>();
    private SingleLiveEvent<Void> networkError = new SingleLiveEvent<>();

    private GameService gameService;

    private int page = 1;
    private String query;

    GameViewModel(GameService gameService) {
        this.gameService = gameService;
    }

    public LiveData<GameList> getGameList() {
        return this.gameList;
    }

    public SingleLiveEvent<Void> getNetworkError() {
        return networkError;
    }

    public void loadMore() {
        if (!TextUtils.isEmpty(query))
            searchByPage(query, ++page);
    }

    public void search(String query) {
        searchByPage(query, 1);
    }

    private void searchByPage(String query, int page) {
        this.query = query;
        this.page = page;

        Call<GameList> listResponse = query.isEmpty() ? gameService.getLatestGames() : gameService.searchGames(query, page);
        listResponse.enqueue(new Callback<GameList>() {
            @Override
            public void onResponse(@NonNull Call<GameList> call, @NonNull Response<GameList> response) {
                if (response.isSuccessful() && gameList != null) {
                    if (page > 1 && gameList.getValue() != null) {
                        gameList.setValue(gameList.getValue().append(response.body()));
                    } else {
                        gameList.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GameList> call, @NonNull Throwable t) {
                networkError.call();
            }
        });
    }
}
