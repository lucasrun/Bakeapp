package com.example.android.bakeapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakeapp.R;
import com.example.android.bakeapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = StepFragment.class.getSimpleName();

    private static final String STEP_VIDEO = "StepVideo";
    private static MediaSessionCompat mMediaSession;
    private View rootView;
    private int mListIndex;
    private ArrayList<Step> mStepList;
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private PlaybackStateCompat.Builder mStateBuilder;

    private ImageView iv_no_video;
    private TextView tv_step_description;
    private Long mPlayerPosition = 0L;
    private Boolean mPlayerReady = true;

    public StepFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_step, container, false);

        tv_step_description = rootView.findViewById(R.id.text_view_step_description);
        iv_no_video = rootView.findViewById(R.id.image_view_no_video);
        mPlayerView = rootView.findViewById(R.id.player_view);
        initializeMediaSession();

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(getString(R.string.PARCEL_STEP));
            mPlayerReady = savedInstanceState.getBoolean(getString(R.string.PLAYER_READY));
            mPlayerPosition = savedInstanceState.getLong(getString(R.string.PLAYER_POSITION));
        }

        if (!TextUtils.isEmpty(mStep.getVideoUrl())) {
            iv_no_video.setVisibility(rootView.INVISIBLE);
            mPlayerView.setFocusable(true);
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        } else {
            Glide.with(getContext()).load(mStep.getThumbUrl()).error(R.drawable.healthy_foot).into(iv_no_video);
            iv_no_video.setVisibility(rootView.VISIBLE);
        }

        tv_step_description.setText(mStep.getDescription());

        return rootView;
    }

    public void setSteps(ArrayList<Step> mStepList) {
        this.mStepList = mStepList;
    }

    public void setStep(Step mStep) {
        this.mStep = mStep;
    }

    public void setListIndex(int mListIndex) {
        this.mListIndex = mListIndex;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), STEP_VIDEO);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), LOG_TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {

            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayerReady = mExoPlayer.getPlayWhenReady();
            releasePlayer();

        }

        mMediaSession.setActive(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null)
            releasePlayer();

        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelable(getString(R.string.PARCEL_STEP), mStep);
        currentState.putBoolean(getString(R.string.PLAYER_READY), mPlayerReady);
        currentState.putLong(getString(R.string.PLAYER_POSITION), mPlayerPosition);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mExoPlayer != null) {
            if (mPlayerReady != null) {
                mExoPlayer.setPlayWhenReady(mPlayerReady);
                mExoPlayer.seekTo(mPlayerPosition);
            }
        } else {
            initializeMediaSession();

            if (!TextUtils.isEmpty(mStep.getVideoUrl())) {
                iv_no_video.setVisibility(rootView.INVISIBLE);
                mPlayerView.setFocusable(true);
                initializePlayer(Uri.parse(mStep.getVideoUrl()));
                mExoPlayer.setPlayWhenReady(mPlayerReady);
                mExoPlayer.seekTo(mPlayerPosition);
            } else {
                Glide.with(getContext()).load(mStep.getThumbUrl()).error(R.drawable.healthy_foot).into(iv_no_video);
                iv_no_video.setVisibility(rootView.VISIBLE);
            }

        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}