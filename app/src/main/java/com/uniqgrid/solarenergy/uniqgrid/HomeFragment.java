package com.uniqgrid.solarenergy.uniqgrid;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import Utils.CircleDisplay;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    TextView tvPlan,tvActiveTickets,tvTotalTickets;
    TextView tvSanctionedLoad;


    String plan = "-",totalTickets = "-",activeTickets = "-";
    CircleDisplay cdConnLoad,cdDg,cdSolarPv;
    String sanctionedLoad="0" , connLoad="0" , dgLoad="0", solarPvLoad = "0";
    float sanctionedLoadNum=0f ,connLoadNum=0f , dgLoadNum=0f, solarPvLoadNum = 0f;

    ImageView ivErrorConnLoad,ivErrorDg,ivErrorSolarPv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View customView = inflater.inflate(R.layout.fragment_home, container, false);

        tvPlan = (TextView) customView.findViewById(R.id.tvPlan);
        tvActiveTickets = (TextView) customView.findViewById(R.id.tvActiveTickets);
        tvTotalTickets = (TextView) customView.findViewById(R.id.tvTotalTickets);

        tvSanctionedLoad = (TextView) customView.findViewById(R.id.tvSanctionedLoad);


        cdConnLoad = (CircleDisplay) customView.findViewById(R.id.cdConnLoad);
        cdDg = (CircleDisplay) customView.findViewById(R.id.cdDg);
        cdSolarPv = (CircleDisplay) customView.findViewById(R.id.cdSolarPv);

        ivErrorConnLoad = (ImageView) customView.findViewById(R.id.ivErrorConnLoad);
        ivErrorDg = (ImageView) customView.findViewById(R.id.ivErrorDg);
        ivErrorSolarPv = (ImageView) customView.findViewById(R.id.ivErrorSolarPv);



        prepareData();
        loadData();


         return  customView;
    }

    public  void prepareData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {

                JSONObject contentJson = new JSONObject(content);
                plan = contentJson.getString("Plan");
                activeTickets = contentJson.getString("Active Tickets");
                totalTickets = contentJson.getString("Total Tickets");
                sanctionedLoad = contentJson.getString("Sanctioned load in kW");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadData(){

        try{
            sanctionedLoadNum = Float.parseFloat(sanctionedLoad);
            connLoadNum = Float.parseFloat(connLoad);
            dgLoadNum = Float.parseFloat(dgLoad);
            solarPvLoadNum = Float.parseFloat(solarPvLoad);
        }catch (Exception e){

        }

        tvPlan.setText(plan);
        tvActiveTickets.setText(activeTickets);
        tvTotalTickets.setText("/ "+ totalTickets);
        tvSanctionedLoad.setText(""+(int)sanctionedLoadNum);


        cdConnLoad.setAnimDuration(2000);
        cdConnLoad.setValueWidthPercent(20f);
        cdConnLoad.setTextSize(15f);
        cdConnLoad.setTextColor(Color.parseColor("#ef6762"));
        cdConnLoad.setColor(Color.parseColor("#ef6762"));
        cdConnLoad.setDrawText(true);
        cdConnLoad.setFormatDigits(0);
        cdConnLoad.setUnit("");
        cdConnLoad.setStepSize(0.5f);
        if(connLoadNum>sanctionedLoadNum)
            ivErrorConnLoad.setVisibility(View.VISIBLE);
        cdConnLoad.showValue(connLoadNum, sanctionedLoadNum, true);
        cdConnLoad.setTouchEnabled(false);

        ivErrorConnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Excess Power");
                builder.setMessage("Connected Load is more than Sanctioned Load");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        cdDg.setAnimDuration(2000);
        cdDg.setValueWidthPercent(20f);
        cdDg.setTextSize(15f);
        cdDg.setTextColor(Color.parseColor("#8cc2dc"));
        cdDg.setColor(Color.parseColor("#8cc2dc"));
        cdDg.setDrawText(true);
        cdDg.setFormatDigits(0);
        cdDg.setUnit("");
        cdDg.setStepSize(0.5f);
        if(dgLoadNum>sanctionedLoadNum)
            ivErrorDg.setVisibility(View.VISIBLE);
        cdDg.showValue(dgLoadNum, sanctionedLoadNum, true);
        cdDg.setTouchEnabled(false);

        ivErrorDg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Excess Power");
                builder.setMessage("Diesel Generator Load is more than Sanctioned Load");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        cdSolarPv.setAnimDuration(2000);
        cdSolarPv.setValueWidthPercent(20f);
        cdSolarPv.setTextSize(15f);
        cdSolarPv.setTextColor(Color.parseColor("#6dd260"));
        cdSolarPv.setColor(Color.parseColor("#6dd260"));
        cdSolarPv.setDrawText(true);
        cdSolarPv.setFormatDigits(0);
        cdSolarPv.setUnit("");
        cdSolarPv.setStepSize(0.5f);
        if(solarPvLoadNum>sanctionedLoadNum)
            ivErrorSolarPv.setVisibility(View.VISIBLE);
        cdSolarPv.showValue(solarPvLoadNum, sanctionedLoadNum, true);
        cdSolarPv.setTouchEnabled(false);



        ivErrorSolarPv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Excess Power");
                builder.setMessage("Solar PV Load is more than Sanctioned Load");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        // A8C9D9 -- gray type color
        // 24c2fd -- blue color





    }



}
