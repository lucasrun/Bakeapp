package com.example.android.bakeapp.models;
import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private int mId;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbUrl;

    public Step(int mId, String mShortDescription, String mDescription, String mVideoUrl, String mThumbUrl) {
        this.mId = mId;
        this.mShortDescription = mShortDescription;
        this.mDescription = mDescription;
        this.mVideoUrl = mVideoUrl;
        this.mThumbUrl = mThumbUrl;
    }

    protected Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public void setThumbUrl(String mThumbUrl) {
        this.mThumbUrl = mThumbUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }
}
