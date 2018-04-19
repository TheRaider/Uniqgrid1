package com.uniqgrid.solarenergy.uniqgrid;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import Utils.BottomNavigationViewBehaviour;
import Utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView ivDrawerIcon,ivRefresh;
    TextView tvLogo;
    DrawerLayout drawer;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    TextView tvNavEstName,tvNavEmail;
    String navEstName="",navEmail="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvLogo = (TextView)findViewById(R.id.tvLogo);
        // Changing Font of Uniqgrid
        Typeface ocrExtendedFont = Typeface.createFromAsset(getAssets(),  "fonts/ocrExtended.TTF");
        tvLogo.setTypeface(ocrExtendedFont);


      // Setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Uniqgrid");
        }


        // Side Navigation Bar
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ivDrawerIcon = (ImageView) findViewById(R.id.ivDrawerIcon);
        ivDrawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        tvNavEstName = (TextView) headerView.findViewById(R.id.tvNavEstName);
        tvNavEmail = (TextView) headerView.findViewById(R.id.tvNavEmail);


        // Bottom Navigation Bar
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Helper Class to disable shift mode in bottom navigation drawer when there are more than 3 items
        // Disabling shift mode using helper class
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

//        // Hiding Bottom navigation bar on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationViewBehaviour());

        // Select Home
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);



        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);

        final Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotation);

            // Set onClick listener for button press action
        ivRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(rotation);

                   // send request
                    refreshData();
                    loadData();

                }
            });

        loadData();
    }




    public  void refreshData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String email = app_preferences.getString("email","abcd");

        AndroidNetworking.get("https://www.ragic.com/uniqgrid/crm3/1?where=1000507,eq,"+email+"&api")
                .addHeaders("Authorization","Basic ajZNMW9hMFQrV2lqT2NxdURuTzJGbS8yRnhrY0crQmpUdGt0R1RPNFhHSldKK2lTL3dBWVhKOG1ScHpEQXVNOQ==") // posting header
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        String crctPassword = "";
                        String successMsg="abcd";
                        JSONObject content = new JSONObject();
                        Iterator<String> keys = response.keys();

                        if(response.length() == 0 || !(keys.hasNext())){
                            Snackbar.make(findViewById(android.R.id.content), "Please check your Network Connection", Snackbar.LENGTH_LONG).show();
                        }else{
                            try {
                                if(keys.hasNext()) {
                                    content = response.getJSONObject(keys.next());
                                }
                            }catch (JSONException exception){

                            }

                                SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor = app_preferences.edit();
                                editor.putString("content",content.toString());
                                editor.apply();

                            }

                        }

                    @Override
                    public void onError(ANError anError) {
                        Snackbar.make(findViewById(android.R.id.content), "Please check your Network Connection", Snackbar.LENGTH_LONG).show();
                    }

                });

        bottomNavigationView.setSelectedItemId(bottomNavigationView.getSelectedItemId());


    }
    public  void loadData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {

                JSONObject contentJson = new JSONObject(content);
                navEstName = contentJson.getString("Name of the establishment");
                navEmail = contentJson.getString("Email");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvNavEstName.setText(navEstName);
        tvNavEmail.setText(navEmail);

    }



    public void openDrawer(){
        drawer.openDrawer(Gravity.START);
    }


    public void loadEnergyView(){

      /*  new FinestWebView.Builder(this).theme(R.style.RedTheme)
                .titleDefault("Bless This Stuff")
                .webViewBuiltInZoomControls(true)
                .webViewDisplayZoomControls(true)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .injectJavaScript("javascript: document.getElementById('msg').innerHTML='Hello "
                        + "TheFinestArtist"
                        + "!';")
                .show("http://www.blessthisstuff.com");

                */

        new FinestWebView.Builder(this).theme(R.style.FinestWebViewTheme)
                .titleDefault("Things Board")
                .showUrl(false)
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
                .show("https://www.google.com");


      /*  new FinestWebView.Builder(this)
                .webViewJavaScriptEnabled(true)
                .injectJavaScript(null)
                .show("http://54.210.0.223:8080/login"); */


    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(id == R.id.nav_energy_view) {
            Intent intent = new Intent(MainActivity.this,EnergyViewActivity.class);
            openActivity(intent);
        }else if(id == R.id.nav_tickets){
            Intent intent = new Intent(MainActivity.this,TicketsActivity.class);
            openActivity(intent);
        }else if(id == R.id.nav_profile){
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            openActivity(intent);
        }else if(id == R.id.nav_logout){

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(MainActivity.this,LoginActivityNew.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this,"Successfully Logged out",Toast.LENGTH_LONG).show();
            finish();
        }else{
/*
        if (id == R.id.nav_dashboard) {
            DashBoard dashBoardFragment = new DashBoard();
            transaction.replace(R.id.fragment_container, dashBoardFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        } else if (id == R.id.nav_trends) {
            Trends trendsFragment = new Trends();
            transaction.replace(R.id.fragment_container, trendsFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();

        } else if (id == R.id.nav_reports) {
            Reports reportsFragment = new Reports();
            transaction.replace(R.id.fragment_container, reportsFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();

        }else if (id == R.id.nav_editProfile) {
            Intent intent = new Intent(MainActivity.this,EditProfileActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_manage_est) {
            Intent intent = new Intent(MainActivity.this,ManageEstActivity.class);
            startActivity(intent);
        }
        */

            drawer.closeDrawer(GravityCompat.START);

        }
        return true;
    }


    public void openActivity(final Intent intent){
        drawer.closeDrawer(Gravity.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        },100);


    }

    // Managing Clicks in Bottom Navigation Drawer
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {

                case  R.id.navigation_home :

                    transaction.replace(R.id.fragment_container, new HomeFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();

                    return true;


                case  R.id.navigation_assets :

                    transaction.replace(R.id.fragment_container, new AssetsFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();

                    return true;


                case  R.id.navigation_consumption :

                    transaction.replace(R.id.fragment_container, new ConsumptionFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();

                    return true;


                case  R.id.navigation_solar :

                    transaction.replace(R.id.fragment_container, new SolarFragment());
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();

                    return true;


             /*   case R.id.navigation_home:

                    if(fragmentManager.findFragmentByTag("3") != null) {
                        //if the fragment exists, show it.
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out).show(fragmentManager.findFragmentByTag("0")).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out).add(R.id.fragment_container, new HomeFragment(), "0").commit();
                    }
                    return true;


                case R.id.navigation_consumption:

                    if(fragmentManager.findFragmentByTag("2") != null) {
                        //if the fragment exists, show it.
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out).show(fragmentManager.findFragmentByTag("2")).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out).add(R.id.fragment_container, new ConsumptionFragment(), "2").commit();
                    }
                    return true;

                case R.id.navigation_solar:

                    if(fragmentManager.findFragmentByTag("3") != null) {
                        //if the fragment exists, show it.
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out).show(fragmentManager.findFragmentByTag("3")).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.fade_out).add(R.id.fragment_container, new SolarFragment(), "3").commit();
                    }
                    return true;
                    */





            }
            // TODO: 14-01-2018  Change it to return false
            return true;
        }
    };

    boolean doubleBackToExitPressedOnce = false;
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;

                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);


            } else {
                getSupportFragmentManager().popBackStack();
            }

        }

    }


}
