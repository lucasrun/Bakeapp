package com.example.android.bakeapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.models.Ingredient;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.providers.RecipeWidgetProvider;

import java.util.ArrayList;

public class IngredientViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = IngredientViewFactory.class.getSimpleName();
    private static final String NULL_OBJECT_REFERENCE = "Trying to reference to null object";

    private Context mContext;
    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients;

    public IngredientViewFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onDataSetChanged() {
        mRecipe = RecipeWidgetProvider.getRecipe();
        try {
            mIngredients = mRecipe.getRecipeIngredients();
        } catch (Exception e) {
            Log.e(LOG_TAG, NULL_OBJECT_REFERENCE);
        }
    }

    @Override
    public int getCount() {
        if (mIngredients == null)
            return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.text_view_quantity_widget, mContext.getString(R.string.EMPTY) + mIngredients.get(position).getQuantity());
        views.setTextViewText(R.id.text_view_measure_widget, mIngredients.get(position).getMeasure());
        views.setTextViewText(R.id.text_view_ingredient_widget, mIngredients.get(position).getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}