package com.robsessions.videogamesearch.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.robsessions.videogamesearch.Injection;
import com.robsessions.videogamesearch.R;
import com.robsessions.videogamesearch.network.GameList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Displays the list of Games, including a search filter
 */
public class GameListFragment extends Fragment {

    private static final String BUNDLE_KEY_QUERY = "query";

    @BindView(R.id.list_games) RecyclerView list;
    @BindView(R.id.text_no_games) TextView noGamesIndicator;
    @BindView(R.id.list_loading_progress_bar) ContentLoadingProgressBar listLoadingProgressBar;
    @BindString(R.string.network_error) String networkError;

    private GameListAdapter gameListAdapter;
    private GameViewModel viewModel;
    private Unbinder unbinder;
    private CharSequence query;
    private SearchView searchView;

    public GameListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
        setupViewModel();

        if (savedInstanceState == null) {
            performSearch("");
        } else {
            query = savedInstanceState.getCharSequence(BUNDLE_KEY_QUERY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(BUNDLE_KEY_QUERY, searchView.getQuery());
    }

    private void setupListAdapter() {
        gameListAdapter = new GameListAdapter(new GameList(), getContext());
        list.setAdapter(gameListAdapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupViewModel() {
        GameViewModelFactory factory = new GameViewModelFactory(Injection.getGameService());
        viewModel = ViewModelProviders.of(this, factory).get(GameViewModel.class);

        /* subscribe to changes in the game list */
        viewModel.getGameList().observe(this, gameList -> {
            boolean isEmpty = gameList == null || gameList.size() == 0;
            gameListAdapter.setList(gameList);
            list.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
            noGamesIndicator.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
            listLoadingProgressBar.hide();
        });

        /* subscribe to network errors */
        viewModel.getNetworkError().observe(this, aVoid -> renderNetworkError());
    }

    private void performSearch(String query) {
        listLoadingProgressBar.show();
        viewModel.search(query);
    }

    private void renderNetworkError() {
        listLoadingProgressBar.hide();
        Toast.makeText(getContext(), networkError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GameListFragment.this.query = query;
                performSearch(query);
                searchView.clearFocus(); // closes keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (!TextUtils.isEmpty(query)) {
            searchViewItem.expandActionView();
            searchView.setQuery(query, false); // we don't need to resubmit, the ViewModel retained our list
            searchView.clearFocus();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
}