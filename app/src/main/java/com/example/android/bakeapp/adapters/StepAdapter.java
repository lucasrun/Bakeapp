package com.example.android.bakeapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakeapp.R;
import com.example.android.bakeapp.interfaces.ClickHandler;
import com.example.android.bakeapp.models.Recipe;
import com.example.android.bakeapp.models.Step;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> implements ClickHandler {
    ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Step> mSteps;

    public StepAdapter(Context mContext, ArrayList<Step> mSteps) {
        this.mContext = mContext;
        this.mSteps = mSteps;
    }

    @Override
    public StepAdapter.StepAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;
        layoutId = R.layout.item_step;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);

        return new StepAdapter.StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepAdapterViewHolder holder, int position) {
        holder.tv_step_description.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    @Override
    public void onRecipeSelected(int position, Recipe recipe) {

    }

    @Override
    public void onStepSelected(int position, Step step, ArrayList<Step> steps) {

    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tv_step_description;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            tv_step_description = itemView.findViewById(R.id.text_view_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler = (ClickHandler) mContext;
            mClickHandler.onStepSelected(adapterPosition, step, mSteps);
        }
    }
}