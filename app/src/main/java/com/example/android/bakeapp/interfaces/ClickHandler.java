package com.example.android.bakeapp.interfaces;

import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.models.Step;

import java.util.ArrayList;

public interface ClickHandler {
    void onRecipeSelected(int position, Recipe recipe);

    void onStepSelected(int position, Step step, ArrayList<Step> steps);
}