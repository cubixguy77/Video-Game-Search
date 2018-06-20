package com.robsessions.videogamesearch.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for GameList object
 */
public class GameList {

    @SerializedName("results")
    @Expose
    private ArrayList<Game> gameList;

    public GameList append(GameList other) {
        this.gameList.addAll(other.getList());
        return this;
    }

    public Game get(int position) {
        return gameList == null ? null : gameList.get(position);
    }

    public int size() {
        return gameList == null ? 0 : gameList.size();
    }

    private List getList() {
        return this.gameList;
    }
}
