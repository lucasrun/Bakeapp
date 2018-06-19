package com.example.android.bakeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.adapters.StepAdapter;
import com.example.android.bakeapp.fragments.StepFragment;
import com.example.android.bakeapp.interfaces.ClickHandler;
import com.example.android.bakeapp.models.Ingredient;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.models.Step;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements ClickHandler {

    private static final String S1 = "Ingredients: \n";
    private static final String S2 = "- ";
    private static final String S3 = " ";
    private static final String S4 = " of ";
    private static final String S5 = "\n";

    private RecyclerView rv;
    private TextView tv_ingredient;

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_ingredient = findViewById(R.id.text_view_ingredient);
        rv = findViewById(R.id.recyclerview_steps);

        try {
            mRecipe =  getIntent().getExtras().getParcelable(getString(R.string.PARCEL_RECIPE));
        } catch(Throwable e) {
            e.printStackTrace();
        }

        if(mRecipe != null){
            setTitle(mRecipe.getName());

            ArrayList<Ingredient> ingredientList = mRecipe.getRecipeIngredients();

            StringBuilder builder = new StringBuilder();

            builder.append(S1);
            for(int i=0; i<ingredientList.size(); i++) {
                builder.append(S2);
                builder.append(ingredientList.get(i).getQuantity());
                builder.append(S3);
                builder.append(ingredientList.get(i).getMeasure());
                builder.append(S4);
                builder.append(ingredientList.get(i).getIngredient());
                builder.append(S5);
            }

            tv_ingredient.setText(builder.toString());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            rv.setLayoutManager(linearLayoutManager);

            rv.setNestedScrollingEnabled(false);
            rv.setFocusable(false);
            StepAdapter stepAdapter = new StepAdapter(this, mRecipe.getRecipeSteps());
            rv.setAdapter(stepAdapter);

            if (findViewById(R.id.player_container) != null) {
                mTwoPane = true;
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    StepFragment stepFragment = new StepFragment();
                    stepFragment.setStep(mRecipe.getRecipeSteps().get(0));
                    fragmentManager.beginTransaction()
                            .replace(R.id.player_container , stepFragment)
                            .commit();
            } else {
                mTwoPane = false;
            }
        }
    }

    @Override
    public void onRecipeSelected(int position, Recipe recipe) {

    }

    @Override
    public void onStepSelected(int position, Step step , ArrayList<Step> steps) {

        if(mTwoPane) {
            StepFragment stepNewFragment = new StepFragment();
            stepNewFragment.setStep(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.player_container, stepNewFragment)
                    .commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean(getString(R.string.TWO_PANE) , mTwoPane);
            bundle.putParcelable(getString(R.string.PARCEL_STEP), step);
            bundle.putInt(getString(R.string.STEP_POSITION) , position);
            bundle.putParcelableArrayList(getString(R.string.PARCEL_STEP_LIST), steps);
            Intent myIntent = new Intent(this, StepActivity.class);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }
    }

}