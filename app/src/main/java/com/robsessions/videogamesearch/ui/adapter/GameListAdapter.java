package com.robsessions.videogamesearch.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robsessions.videogamesearch.R;
import com.robsessions.videogamesearch.network.Game;
import com.robsessions.videogamesearch.network.GameList;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays the individual list items in the game list
 */
public class GameListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private GameList games;
    private final Context context;

    public GameListAdapter(GameList games, Context context) {
        this.games = games;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GameViewHolder) holder).bind(games.get(position));
    }

    @Override
    public int getItemCount() {
       return games.size();
    }

    public void setList(GameList games) {
        this.games = games;
        notifyDataSetChanged();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_game) ImageView game_image;
        @BindView(R.id.text_title) TextView title;

        GameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Game game) {
            Picasso
                .with(context)
                .load(game.getImage().getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .fit()
                .into(game_image);
            title.setText(game.getTitle());
        }
    }
}
