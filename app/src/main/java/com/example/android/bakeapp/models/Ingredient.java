package com.example.android.bakeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private double mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredient(double mQuantity, String mMeasure, String mIngredient) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mIngredient = mIngredient;
    }

    protected Ingredient(Parcel in) {
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);
    }
}