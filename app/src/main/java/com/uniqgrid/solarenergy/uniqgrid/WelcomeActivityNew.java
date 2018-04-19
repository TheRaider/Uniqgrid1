package com.uniqgrid.solarenergy.uniqgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class WelcomeActivityNew extends AppCompatActivity {

    TextView tvStep1,tvStep2,tvStep3,tvStep4,tvStep5,tvStep6;
    ImageView ivNext;
    String estName ="-";
    TextView tvEstName;
    ProgressBar pgWelcomeScreen;
    int progress=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_new);


        tvEstName = (TextView) findViewById(R.id.tvEstName);

        tvStep1 = (TextView) findViewById(R.id.tvStep1);
        tvStep2 = (TextView) findViewById(R.id.tvStep2);
        tvStep3 = (TextView) findViewById(R.id.tvStep3);
        tvStep4 = (TextView) findViewById(R.id.tvStep4);
        tvStep5 = (TextView) findViewById(R.id.tvStep5);
        tvStep6 = (TextView) findViewById(R.id.tvStep6);
        ivNext = (ImageView) findViewById(R.id.ivNext);

        pgWelcomeScreen = (ProgressBar) findViewById(R.id.pgWelcomeScreen);
        pgWelcomeScreen.setProgress(0);



        tvStep1.setTextColor(Color.parseColor("#55000000"));
        tvStep2.setTextColor(Color.parseColor("#55000000"));
        tvStep3.setTextColor(Color.parseColor("#55000000"));
        tvStep4.setTextColor(Color.parseColor("#55000000"));
        tvStep5.setTextColor(Color.parseColor("#55000000"));
        tvStep6.setTextColor(Color.parseColor("#55000000"));






        prepareData();

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivityNew.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void prepareData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivityNew.this);
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {
                JSONObject contentJson = new JSONObject(content);
                estName = contentJson.getString("Name of the establishment");
                tvEstName.setText(estName);
                JSONObject stepsJson = contentJson.getJSONObject("_subtable_1000582");
                Iterator<String> keys = stepsJson.keys();
                if(keys.hasNext()) {
                    JSONObject stepJson = stepsJson.getJSONObject(keys.next());
                    if ((stepJson.getString("Energy Assessment")).equals("Yes")) {
                        pgWelcomeScreen.setProgress(1);
                        tvStep1.setTextColor(Color.parseColor("#000000"));
                    }
                    if ((stepJson.getString("Device Installation")).equals("Yes")) {
                        pgWelcomeScreen.setProgress(2);
                        tvStep2.setTextColor(Color.parseColor("#000000"));
                    }

                    if ((stepJson.getString("EnergyView Activation")).equals("Yes")) {
                        pgWelcomeScreen.setProgress(3);
                        tvStep3.setTextColor(Color.parseColor("#000000"));
                    }

                    if ((stepJson.getString("Consumption Management")).equals("Yes")) {
                        pgWelcomeScreen.setProgress(4);
                        tvStep4.setTextColor(Color.parseColor("#000000"));
                    }

                    if ((stepJson.getString("Solar Installation")).equals("Yes")) {
                        pgWelcomeScreen.setProgress(5);
                        tvStep5.setTextColor(Color.parseColor("#000000"));
                    }

                    if ((stepJson.getString("Generation Management")).equals("Yes")) {
                        pgWelcomeScreen.setProgress(6);
                        tvStep6.setTextColor(Color.parseColor("#000000"));
                    }


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
