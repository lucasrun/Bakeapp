package com.example.android.bakeapp.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.interfaces.TaskCompleted;
import com.example.android.bakeapp.adapters.RecipeAdapter;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.utilities.JsonAsyncTask;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_recipe);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager = new GridLayoutManager(getActivity(), 4);
        } else {
            mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        }

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        final RecipeAdapter mAdapter = new RecipeAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        JsonAsyncTask jsonAsyncTask = new JsonAsyncTask(new TaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Recipe> recipeArray) {
                mAdapter.changeRecipe(recipeArray);
            }
        }, getActivity().getBaseContext());

        jsonAsyncTask.execute(getContext().getString(R.string.JSON_URL));

        return rootView;
    }

}