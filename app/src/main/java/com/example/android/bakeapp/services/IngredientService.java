package com.example.android.bakeapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.adapters.RecipeAdapter;
import com.example.android.bakeapp.interfaces.TaskCompleted;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.providers.RecipeWidgetProvider;
import com.example.android.bakeapp.utilities.JsonAsyncTask;

import java.util.ArrayList;

public class IngredientService extends IntentService {

    public static final String ACTION_INGREDIENTS = "com.example.adroid.bakeapp.action.ingrediens";
    public static final String ACTION_UPDATE_INGREDIENTS_WIDGET = "com.example.android.bakeapp.action.update_ingrediens_widget";
    public static final String PARCEL_RECIPE = "RecipeService";
    public static final String INGREDIENT_SERVICE = "IngredientService";
    Recipe mRecipe;

    public IngredientService() {
        super(INGREDIENT_SERVICE);
    }

    public static void startActionUpdateIngredientWidgets(Context context, Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_RECIPE, recipe);
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGET);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INGREDIENTS.equals(action)) {
                changeIngredients();

            } else if (ACTION_UPDATE_INGREDIENTS_WIDGET.equals(action)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    mRecipe = bundle.getParcelable(PARCEL_RECIPE);
                }

                updateIngredintsWidget(mRecipe);
            }
        }
    }

    private void changeIngredients() {
        final RecipeAdapter mAdapter = new RecipeAdapter(this, null);
        JsonAsyncTask jsonAsyncTask = new JsonAsyncTask(new TaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<Recipe> recipeArray) {
                mAdapter.changeRecipe(recipeArray);
            }
        }, this.getBaseContext());

        jsonAsyncTask.execute(this.getString(R.string.JSON_URL));
    }

    private void updateIngredintsWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateIngredientWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}