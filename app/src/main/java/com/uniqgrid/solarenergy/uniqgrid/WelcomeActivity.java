package com.uniqgrid.solarenergy.uniqgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class WelcomeActivity extends AppCompatActivity {

    TextView tvStep1,tvStep2,tvStep3,tvStep4,tvStep5,tvStep6;
    ImageView ivStep1,ivStep2,ivStep3,ivStep4,ivStep5,ivStep6;
    ImageView ivNext;
    String estName ="-";
    TextView tvEstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvEstName = (TextView) findViewById(R.id.tvEstName);

        tvStep1 = (TextView) findViewById(R.id.tvStep1);
        tvStep2 = (TextView) findViewById(R.id.tvStep2);
        tvStep3 = (TextView) findViewById(R.id.tvStep3);
        tvStep4 = (TextView) findViewById(R.id.tvStep4);
        tvStep5 = (TextView) findViewById(R.id.tvStep5);
        tvStep6 = (TextView) findViewById(R.id.tvStep6);
        ivNext = (ImageView) findViewById(R.id.ivNext);

    /*    ivStep1 = (ImageView) findViewById(R.id.ivStep1);
        ivStep2 = (ImageView) findViewById(R.id.ivStep2);
        ivStep3 = (ImageView) findViewById(R.id.ivStep3);
        ivStep4 = (ImageView) findViewById(R.id.ivStep4);
        ivStep5 = (ImageView) findViewById(R.id.ivStep5);
        ivStep6 = (ImageView) findViewById(R.id.ivStep6);



        TextDrawable step1 = TextDrawable.builder()
                .beginConfig().bold().endConfig()
                .buildRound("1", Color.parseColor("#22000000"));
        ivStep1.setImageDrawable(step1);
        TextDrawable step2 = TextDrawable.builder()
                .beginConfig().bold().endConfig()
                .buildRound("2", Color.parseColor("#22000000"));
        ivStep2.setImageDrawable(step2);
        TextDrawable step3 = TextDrawable.builder()
                .beginConfig().bold().endConfig()
                .buildRound("3", Color.parseColor("#22000000"));
        ivStep3.setImageDrawable(step3);
        TextDrawable step4 = TextDrawable.builder()
                .beginConfig().bold().endConfig()
                .buildRound("4", Color.parseColor("#22000000"));
        ivStep4.setImageDrawable(step4);
        TextDrawable step5 = TextDrawable.builder()
                .beginConfig().bold().endConfig()
                .buildRound("5", Color.parseColor("#22000000"));
        ivStep5.setImageDrawable(step5);
        TextDrawable step6 = TextDrawable.builder()
                .beginConfig().bold().endConfig()
                .buildRound("6", Color.parseColor("#22000000"));
        ivStep6.setImageDrawable(step6);

        */

        tvStep1.setBackgroundColor(Color.parseColor("#55000000"));


        tvStep2.setBackgroundColor(Color.parseColor("#22000000"));
        tvStep2.setTextColor(Color.parseColor("#FFFFFF"));

        tvStep3.setBackgroundColor(Color.parseColor("#22000000"));
        tvStep3.setTextColor(Color.parseColor("#FFFFFF"));

        tvStep4.setBackgroundColor(Color.parseColor("#22000000"));
        tvStep4.setTextColor(Color.parseColor("#FFFFFF"));

        tvStep5.setBackgroundColor(Color.parseColor("#22000000"));
        tvStep5.setTextColor(Color.parseColor("#FFFFFF"));

        tvStep6.setBackgroundColor(Color.parseColor("#22000000"));
        tvStep6.setTextColor(Color.parseColor("#FFFFFF"));




        prepareData();

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void prepareData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
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
                        tvStep1.setBackgroundColor(Color.parseColor("#02bf45"));
                        tvStep1.setTextColor(Color.parseColor("#FFFFFF"));
//                        TextDrawable step1 = TextDrawable.builder()
//                                .beginConfig().bold().endConfig()
//                                .buildRound("1", Color.parseColor("#02bf45"));
//                        ivStep1.setImageDrawable(step1);
                    }
                    if ((stepJson.getString("Device Installation")).equals("Yes")) {
                        tvStep2.setBackgroundColor(Color.parseColor("#02bf45"));
                        tvStep2.setTextColor(Color.parseColor("#FFFFFF"));
//                        TextDrawable step2 = TextDrawable.builder()
//                                .beginConfig().bold().endConfig()
//                                .buildRound("2", Color.parseColor("#02bf45"));
//                        ivStep2.setImageDrawable(step2);
                    }

                    if ((stepJson.getString("EnergyView Activation")).equals("Yes")) {
                        tvStep3.setBackgroundColor(Color.parseColor("#02bf45"));
                        tvStep3.setTextColor(Color.parseColor("#FFFFFF"));
//                        TextDrawable step3 = TextDrawable.builder()
//                                .beginConfig().bold().endConfig()
//                                .buildRound("3", Color.parseColor("#02bf45"));
//                        ivStep3.setImageDrawable(step3);
                    }

                    if ((stepJson.getString("Consumption Management")).equals("Yes")) {
                        tvStep4.setBackgroundColor(Color.parseColor("#02bf45"));
                        tvStep4.setTextColor(Color.parseColor("#FFFFFF"));
//                        TextDrawable step4 = TextDrawable.builder()
//                                .beginConfig().bold().endConfig()
//                                .buildRound("4", Color.parseColor("#02bf45"));
//                        ivStep4.setImageDrawable(step4);
                    }

                    if ((stepJson.getString("Solar Installation")).equals("Yes")) {
                        tvStep5.setBackgroundColor(Color.parseColor("#02bf45"));
                        tvStep5.setTextColor(Color.parseColor("#FFFFFF"));
//                        TextDrawable step5 = TextDrawable.builder()
//                                .beginConfig().bold().endConfig()
//                                .buildRound("5", Color.parseColor("#02bf45"));
//                        ivStep5.setImageDrawable(step5);
                    }

                    if ((stepJson.getString("Generation Management")).equals("Yes")) {
                        tvStep6.setBackgroundColor(Color.parseColor("#02bf45"));
                        tvStep6.setTextColor(Color.parseColor("#FFFFFF"));
//                        TextDrawable step6 = TextDrawable.builder()
//                                .beginConfig().bold().endConfig()
//                                .buildRound("6", Color.parseColor("#02bf45"));
//                        ivStep6.setImageDrawable(step6);
                    }


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
