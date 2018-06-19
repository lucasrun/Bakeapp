package com.example.android.bakeapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakeapp.R;
import com.example.android.bakeapp.interfaces.ClickHandler;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.models.Step;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> implements ClickHandler {

    private static final String S1 = "\n";
    private static final String S2 = " servings";

    ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Recipe> mRecipe;

    public RecipeAdapter(Context mContext, ArrayList<Recipe> mRecipe) {
        this.mContext = mContext;
        this.mRecipe = mRecipe;
    }

    @Override
    public void onRecipeSelected(int position, Recipe recipe) {

    }

    @Override
    public void onStepSelected(int position, Step step, ArrayList<Step> steps) {

    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;
        layoutId = R.layout.item_recipe;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        holder.tv_recipe.setText(mRecipe.get(position).getName() + S1 + mRecipe.get(position).getServings() + S2);

        Glide.with(mContext).load(mRecipe.get(position).getImageUrl()).error(R.drawable.healthy_foot).into(holder.iv_recipe);
    }

    public void changeRecipe(ArrayList<Recipe> allRecipies) {
        this.mRecipe = allRecipies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null)
            return 0;
        else
            return mRecipe.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tv_recipe;
        final ImageView iv_recipe;

        RecipeAdapterViewHolder(View view) {
            super(view);
            tv_recipe = view.findViewById(R.id.text_view_recipe);
            iv_recipe = view.findViewById(R.id.image_view_recipe);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipe.get(adapterPosition);
            mClickHandler = (ClickHandler) mContext;
            mClickHandler.onRecipeSelected(adapterPosition, recipe);

        }
    }
}