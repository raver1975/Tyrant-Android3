package com.tyrant;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.klemstinegroup.tyrantandroid.TyrantAndroid;

public class AndroidLauncher extends AndroidApplication {

    //private static final String AD_UNIT_ID = "ca-app-pub-1813447557997883/3993852432";
    //    protected AdView adView;
    protected View gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        FrameLayout layout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);


        View gameView = createGameView(cfg);
        layout.addView(gameView);
        RelativeLayout rl=new RelativeLayout(this);

        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params1.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        rl.setPadding(0,50,0,0);
        rl.setLayoutParams(params1);
        //  final AdView admobView = createAdView();
        //admobView.setAdListener(new AdListener() {
        //  @Override
        //  public void onAdLeftApplication() {
        //      super.onAdLeftApplication();
        //      admobView.setVisibility(View.GONE);
        //  }
        //});
        //rl.addView(admobView);
        layout.addView(rl);
        setContentView(layout);
        //startAdvertising(admobView);
    }

//    private AdView createAdView() {
//        adView = new AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId(AD_UNIT_ID);
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
////
//        adView.setLayoutParams(params);
//        //adView.setId(adView.getId()); // this is an arbitrary id, allows for relative positioning in createGameView()
//
////        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
////        params.addRule(RelativeLayout.RIGHT_OF, gameView.getId());
////        adView.setLayoutParams(params);
//        adView.setBackgroundColor(Color.TRANSPARENT);
//        return adView;
//    }

    private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(new TyrantAndroid(), cfg);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//
//        gameView.setLayoutParams(params);
        return gameView;
    }

//    private void startAdvertising(AdView adView) {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (adView != null) adView.resume();
//    }

//    @Override
//    public void onPause() {
//        if (adView != null) adView.pause();
//        super.onPause();
//    }

//    @Override
//    public void onDestroy() {
//        if (adView != null) adView.destroy();
//        super.onDestroy();
//    }

    @Override
    public void onBackPressed() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        LinearLayout ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.VERTICAL);
//
//        Button b1 = new Button(this);
//        b1.setText("Quit");
//        b1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        ll.addView(b1);
//
//        Button b2 = new Button(this);
//        b2.setText("TheInvader360");
//        b2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));
//                dialog.dismiss();
//            }
//        });
//        ll.addView(b2);
//
//        dialog.setContentView(ll);
//        dialog.show();
    }
}
