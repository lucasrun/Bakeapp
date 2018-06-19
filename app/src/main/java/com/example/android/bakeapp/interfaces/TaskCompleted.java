package com.example.android.bakeapp.interfaces;

import com.example.android.bakeapp.models.Recipe;

import java.util.ArrayList;

public interface TaskCompleted {
    void onTaskCompleted(ArrayList<Recipe> recipeArray);
}