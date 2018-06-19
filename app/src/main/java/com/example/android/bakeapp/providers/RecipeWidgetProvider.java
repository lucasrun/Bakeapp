package com.example.android.bakeapp.providers;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.services.IngredientService;
import com.example.android.bakeapp.services.IngredientViewService;


public class RecipeWidgetProvider extends AppWidgetProvider {

    private static Recipe mRecipe;

    public static Recipe getRecipe() {
        return mRecipe;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int appWidgetId) {

        mRecipe = recipe;

        if (mRecipe != null) {
            Intent intent = new Intent(context, IngredientViewService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setRemoteAdapter(R.id.list_view_widget, intent);
            ComponentName component = new ComponentName(context, RecipeWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(component, views);
        }
    }

    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetManager,
                                               Recipe recipe, int[] appWidgetIds) {
        for (int item : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, item);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientService.startActionUpdateIngredientWidgets(context, mRecipe);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}