package com.robsessions.videogamesearch.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.robsessions.videogamesearch.network.GameService;

/**
 * Helper to create a GameViewModel instance
 */
public class GameViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final GameService gameService;

    public GameViewModelFactory(GameService gameService) {
        this.gameService = gameService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new GameViewModel(gameService);
    }
}