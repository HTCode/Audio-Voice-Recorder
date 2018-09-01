package com.rsma.audiovoicerecorder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.rsma.audiovoicerecorder.activities.MainActivity;

public class AdmobHelper {
    private Context mContext;
    private AdRequest.Builder builder;
    private AdView mAdBanner;
    private InterstitialAd mAdInterstitial;

    private String TAG = "AdmobHelper";

    // ==========================================================================================
    public AdmobHelper(Context context, String appId, String interId, int bannerResId) {
        MobileAds.initialize(context, appId);
        mContext = context;
        builder = new AdRequest.Builder();

        mAdInterstitial = new InterstitialAd(mContext);
        mAdInterstitial.setAdUnitId(interId);
        mAdInterstitial.loadAd( builder.build() );

        mAdBanner = ((Activity)mContext).findViewById(bannerResId);
        mAdBanner.loadAd( builder.build() );
    }

    // ==========================================================================================
    public AdmobHelper(Context context, String interId, int bannerResId) {
        mContext = context;
        builder = new AdRequest.Builder();

        mAdInterstitial = new InterstitialAd(mContext);
        mAdInterstitial.setAdUnitId(interId);
        mAdInterstitial.loadAd( builder.build() );

        mAdBanner = ((Activity)mContext).findViewById(bannerResId);
        mAdBanner.loadAd( builder.build() );
    }
    // ==========================================================================================
    public void showInterOnLoad() {
        mAdInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInter();
            }
        });
        showInter();
    }
    // ==========================================================================================
    public void showInter() {
        if (mAdInterstitial.isLoaded()) {
            mAdInterstitial.show();
            mAdInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    mAdInterstitial.loadAd( builder.build() );
                }
            });
        }
        else {
            Log.d("AdmobHelper", "mAdInterstitial not loaded");
        }
    }
    // ==========================================================================================
    public void confirmExitAd() {
        LayoutInflater factory = LayoutInflater.from(mContext);
        final View view = factory.inflate(R.layout.confirm_exit, null);

        final ProgressBar loadingProressView = view.findViewById(R.id.loadingImgView);

        final AdView exitAdBanner = view.findViewById(R.id.exitAdBanner);
        exitAdBanner.loadAd(new AdRequest.Builder().build());
        exitAdBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                loadingProressView.setVisibility(View.GONE);
                exitAdBanner.setVisibility(View.VISIBLE);
            }
        });

        final AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setView(view);

        mAlertDialog
                .setTitle("Do you want to exit ?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ((Activity) mContext).finish();
                    }
                }).create().show();
    }
    // ==========================================================================================
}
