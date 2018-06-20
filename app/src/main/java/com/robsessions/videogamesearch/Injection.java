package com.robsessions.videogamesearch;

import com.robsessions.videogamesearch.network.GameService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utility class to facilitate dependency injection
 */
public class Injection {

    private static GameService INSTANCE_GAME_SERVICE = null;

    /**
     * Gets an instance of the school service object
     * Returns the existing instance if already built (singleton pattern).
     *
     * @return The SchoolService instance
     */
    public static GameService getGameService() {
        if (INSTANCE_GAME_SERVICE == null) {
            INSTANCE_GAME_SERVICE = new Retrofit.Builder()
                    .baseUrl("http://www.giantbomb.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GameService.class);
        }

        return INSTANCE_GAME_SERVICE;
    }
}
