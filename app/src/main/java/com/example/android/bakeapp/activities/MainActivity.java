package com.example.android.bakeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.interfaces.ClickHandler;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.models.Step;
import com.example.android.bakeapp.services.IngredientService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ClickHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onRecipeSelected(int position , Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.PARCEL_RECIPE), recipe);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);

        // adding selected mRecipe to widget
        IngredientService.startActionUpdateIngredientWidgets(this , recipe);

        startActivity(intent);
    }

    @Override
    public void onStepSelected(int position, Step step, ArrayList<Step> steps) {

    }
}