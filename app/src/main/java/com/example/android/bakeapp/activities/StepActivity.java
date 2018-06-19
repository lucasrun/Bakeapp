package com.example.android.bakeapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.fragments.StepFragment;
import com.example.android.bakeapp.models.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private static final String TITLE = "Steps";
    private ArrayList<Step> mStepList;
    private Step mStep;
    private int mPosition;
    private boolean mTwoPane;

    private Button buttonNext;
    private Button buttonPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        buttonNext = findViewById(R.id.button_next);
        buttonPrevious = findViewById(R.id.button_previous);

        setTitle(TITLE);

        mTwoPane = false;

        FragmentManager fragmentManager = getSupportFragmentManager();

        try {
            Bundle bundle = getIntent().getExtras();
            mStepList = bundle.getParcelableArrayList(getString(R.string.PARCEL_STEP_LIST));
            mStep =  bundle.getParcelable(getString(R.string.PARCEL_STEP));
            mPosition = bundle.getInt(getString(R.string.STEP_POSITION));
            mTwoPane = bundle.getBoolean(getString(R.string.TWO_PANE));

        } catch(Throwable e) {
            e.printStackTrace();
        }

        if(savedInstanceState != null) {
            mStepList = savedInstanceState.getParcelableArrayList(getString(R.string.PARCEL_STEP_LIST));
            mStep = savedInstanceState.getParcelable(getString(R.string.PARCEL_STEP));
            mPosition = savedInstanceState.getInt(getString(R.string.STEP_POSITION));
            mTwoPane = savedInstanceState.getBoolean(getString(R.string.TWO_PANE));
        }

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition--;

                if (mPosition >= 0) {
                    if (mPosition == 0) {
                        buttonPrevious.setVisibility(View.INVISIBLE);
                    }

                    buttonNext.setVisibility(View.VISIBLE);

                    StepFragment stepNewFragment = new StepFragment();
                    stepNewFragment.setStep(mStepList.get(mPosition));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.player_container, stepNewFragment)
                            .commit();
                } else {
                    buttonPrevious.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPosition == -1) {
                    mPosition = 0;
                }

                mPosition++;

                if (mPosition < mStepList.size()) {
                    if(mPosition == mStepList.size()-1) {
                        buttonNext.setVisibility(View.INVISIBLE);
                    } else {
                        buttonNext.setVisibility(View.VISIBLE);
                    }

                    buttonPrevious.setVisibility(View.VISIBLE);

                    StepFragment stepNewFragment = new StepFragment();
                    stepNewFragment.setStep(mStepList.get(mPosition));
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.player_container, stepNewFragment)
                            .commit();
                } else {
                    buttonNext.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (mPosition == mStepList.size() - 1) {
            buttonNext.setVisibility(View.INVISIBLE);
            buttonPrevious.setVisibility(View.VISIBLE);
        } else if (mPosition == 0) {
            buttonNext.setVisibility(View.VISIBLE);
            buttonPrevious.setVisibility(View.INVISIBLE);
        }

        if (mTwoPane) {
            buttonNext.setVisibility(View.INVISIBLE);
            buttonPrevious.setVisibility(View.INVISIBLE);
        }

        if (savedInstanceState == null) {
            StepFragment stepNewFragment = new StepFragment();
            stepNewFragment.setStep(mStep);

            fragmentManager.beginTransaction()
                    .add(R.id.player_container, stepNewFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);

        currentState.putParcelableArrayList(getString(R.string.PARCEL_STEP_LIST) , mStepList);
        currentState.putParcelable(getString(R.string.PARCEL_STEP) , mStep);
        currentState.putInt(getString(R.string.STEP_POSITION) , mPosition);
        currentState.putBoolean(getString(R.string.TWO_PANE) , mTwoPane);
    }
}