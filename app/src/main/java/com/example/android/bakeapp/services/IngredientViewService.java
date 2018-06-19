package com.example.android.bakeapp.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientViewService extends RemoteViewsService {

    public IngredientViewFactory onGetViewFactory(Intent intent) {
        return new IngredientViewFactory(this.getApplicationContext());
    }
}