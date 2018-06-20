package com.robsessions.videogamesearch.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

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

    GameViewModel(GameService gameService) {
        this.gameService = gameService;
    }

    public LiveData<GameList> getGameList() {
        return this.gameList;
    }

    SingleLiveEvent<Void> getNetworkError() {
        return networkError;
    }

    void search(String query) {
        Call<GameList> listResponse = query.isEmpty() ? gameService.getLatestGames() : gameService.searchGames(query);
        listResponse.enqueue(new Callback<GameList>() {
            @Override
            public void onResponse(@NonNull Call<GameList> call, @NonNull Response<GameList> response) {

                if (response.isSuccessful()) {
                    gameList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GameList> call, @NonNull Throwable t) {
                networkError.call();
            }
        });
    }
}
