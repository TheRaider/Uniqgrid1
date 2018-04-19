package com.uniqgrid.solarenergy.uniqgrid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.utils.service.ClipboardManagerUtil;

public class EnergyViewActivity extends AppCompatActivity {

    WebView webView;
//    boolean showSwipeRefreshLayout = true;
    SwipeRefreshLayout swipeRefreshLayout;
   ProgressBar progressBar;
    String url = "http://54.210.0.223:8080/login";

    ImageView ivRefresh,ivBack;

   // ProgressBar googleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy_view);

        overridePendingTransition(R.anim.slide_in_up,R.anim.hold);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Energy View");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Animation rotation = AnimationUtils.loadAnimation(EnergyViewActivity.this, R.anim.rotation);

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If post requests are there then use webView.loadUrl(url);
                // Here as there are no post requests ,webView.reload(); is used
                view.startAnimation(rotation);
                webView.reload();
            }
        });

     //   googleProgressBar = (ProgressBar) findViewById(R.id.googleProgressBar);



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


    //    googleProgressBar.setIndeterminateDrawable(new ChromeFloatingCirclesDrawable.Builder(this).build());



        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(url);

        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#048BFE"));

//        // Refreshing Page
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                webView.loadUrl(webView.getUrl());
//            }
//        });
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
    }


    public  void loadFinestWebView(){
        new FinestWebView.Builder(this).theme(R.style.FinestWebViewTheme)
                .titleDefault("Things Board")
                .showUrl(true)
                .statusBarColorRes(R.color.bluePrimaryDark)
                .toolbarColorRes(R.color.bluePrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.bluePrimaryLight)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColorRes(R.color.bluePrimaryDark)
                .menuSelector(R.drawable.selector_light_theme)
                .menuTextGravity(Gravity.CENTER)
                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show("http://54.210.0.223:8080/login");
    }

    /*private void setPopUpMenu(){
        PopupMenu popupMenu = new PopupMenu(EnergyViewActivity.this,findViewById(R.id.ivWebViewOptions));
        popupMenu.inflate(R.menu.web_view_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_item_refresh :
                        // If post requests are there then use webView.loadUrl(url);
                        // Here as there are no post requests ,webView.reload(); is used
                        webView.reload();
                        return  true;
                    case R.id.menu_item_share_via:

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share via"));

                        return  true;

                    case R.id.menu_item_copy_link :

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Web Url", webView.getUrl());
                        clipboard.setPrimaryClip(clip);

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Copied to Clipboard", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#FFFFFF"));
                        snackbar.getView().setBackgroundColor(Color.parseColor("#048BFE"));
                        snackbar.show();

                        return  true;

                   *//* case R.id.menu_item_open_with :

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
                        startActivity(browserIntent);

                        return true;
                        *//*
                }
                return  true;
            }
        });

        popupMenu.show();
    }*/


     class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {

//            if (showSwipeRefreshLayout) {
//                if (swipeRefreshLayout.isRefreshing() && progress == 100) {
//                    swipeRefreshLayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    });
//                }
//
//                if (!swipeRefreshLayout.isRefreshing() && progress != 100) {
//                    swipeRefreshLayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            swipeRefreshLayout.setRefreshing(true);
//                        }
//                    });
//                }
//            }

            if (progress == 100) {
                progress=0;
              //  googleProgressBar.setVisibility(View.GONE);
            }else{
              //  googleProgressBar.setVisibility(View.VISIBLE);
            }
          progressBar.setProgress(progress);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_down);
    }


}
