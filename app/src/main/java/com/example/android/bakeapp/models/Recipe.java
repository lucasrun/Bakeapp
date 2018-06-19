package com.example.android.bakeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Recipe implements Parcelable{

    private int mRecipeId;
    private String mName;
    private String mSteps;
    private String mServings;
    private String mImageURL;
    private ArrayList<Ingredient> mRecipeIngredients = new ArrayList<>();
    private ArrayList<Step> mRecipeSteps = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(int mRecipeId, String mName, String mServings, String mImageURL, ArrayList<Ingredient> mRecipeIngredients, ArrayList<Step> mRecipeSteps) {
        this.mRecipeId = mRecipeId;
        this.mName = mName;
        this.mServings = mServings;
        this.mImageURL = mImageURL;
        this.mRecipeIngredients = mRecipeIngredients;
        this.mRecipeSteps = mRecipeSteps;
    }

    protected Recipe(Parcel in) {
        mRecipeId = in.readInt();
        mName = in.readString();
        mSteps = in.readString();
        mServings = in.readString();
        mImageURL = in.readString();
        mRecipeIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        mRecipeSteps = in.createTypedArrayList(Step.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRecipeId);
        dest.writeString(mName);
        dest.writeString(mSteps);
        dest.writeString(mServings);
        dest.writeString(mImageURL);
        dest.writeTypedList(mRecipeIngredients);
        dest.writeTypedList(mRecipeSteps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public ArrayList<Step> getRecipeSteps() {
        return mRecipeSteps;
    }

    public void setRecipeSteps(ArrayList<Step> mRecipeSteps) {
        this.mRecipeSteps = mRecipeSteps;
    }

    public ArrayList<Ingredient> getRecipeIngredients() {
        return mRecipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<Ingredient> mRecipeIngredients) {
        this.mRecipeIngredients = mRecipeIngredients;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSteps() {
        return mSteps;
    }

    public void setSteps(String mSpets) {
        this.mSteps = mSpets;
    }

    public String getServings() {
        return mServings;
    }

    public void setServings(String mServings) {
        this.mServings = mServings;
    }

    public String getImageUrl() {
        return mImageURL;
    }

    public void setImageUrl(String mImageURL) {
        this.mImageURL = mImageURL;
    }

}