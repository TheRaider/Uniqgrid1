package com.uniqgrid.solarenergy.uniqgrid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail,etNewPassword,etConfirmNewPassword;
    Button btnChangePassword;
    String emailid,password,confirmPassword;

    ProgressDialog pd;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);


        etEmail = (EditText) findViewById(R.id.etEmail);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmNewPassword = (EditText) findViewById(R.id.etConfirmNewPassword);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        pd = new ProgressDialog(ForgotPasswordActivity.this);
        pd.setMessage("Please wait......");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!etConfirmNewPassword.getText().toString().equalsIgnoreCase("") &&
                        etConfirmNewPassword.getText().toString().equalsIgnoreCase(etNewPassword.getText().toString())){
                }else{
                    etConfirmNewPassword.setError("Passwords doesn't match");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!etConfirmNewPassword.getText().toString().equalsIgnoreCase("") &&
                        etConfirmNewPassword.getText().toString().equalsIgnoreCase(etNewPassword.getText().toString())){
                }else{
                    etConfirmNewPassword.setError("Passwords doesn't match");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailid = etEmail.getText().toString();
                password = etNewPassword.getText().toString();
                confirmPassword = etConfirmNewPassword.getText().toString();


                if (!isValidEmailAddress(emailid)) {
                    etEmail.setError("Please enter a valid Email-Id");
                }else if(!isValidPassword(password)) {
                    etNewPassword.setError("Password is too short");
                }else if(!password.equalsIgnoreCase(confirmPassword)){
                    etConfirmNewPassword.setError("Passwords doesn't match");
                }else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    pd.show();
                    changePassword(emailid,password);
                }
            }
        });

    }

    public void changePassword(final String email, final String passwo){

        AndroidNetworking.get("https://www.ragic.com/uniqgrid/crm3/1?where=1000507,eq,"+email+"&api")
                .addHeaders("Authorization","Basic ajZNMW9hMFQrV2lqT2NxdURuTzJGbS8yRnhrY0crQmpUdGt0R1RPNFhHSldKK2lTL3dBWVhKOG1ScHpEQXVNOQ==") // posting header
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        int ragicId = -1;
                        String crctPassword = "";
                        String successMsg="abcd";
                        JSONObject content = new JSONObject();
                        Iterator<String> keys = response.keys();

                        if(response.length() == 0 || !(keys.hasNext())){
                            Log.d("result","length is 0");
                            Snackbar.make(findViewById(android.R.id.content), "Email is not yet registered", Snackbar.LENGTH_LONG).show();
                            ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                }
                            });
                        }else{
                            try {
                                if(keys.hasNext()) {
                                    content = response.getJSONObject(keys.next());
                                    ragicId = Integer.parseInt(content.getString("_ragicId"));
                                }
                            }catch (Exception exception){

                            }
                            Log.d("result","success");
                            successMsg = "success";

                            ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                }
                            });

                            sendPostRequest(ragicId,email,passwo);

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Snackbar.make(findViewById(android.R.id.content), "Please check your Network Connection", Snackbar.LENGTH_LONG).show();

                        ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                    }

                });


    }

    public void sendPostRequest(int ragicId ,final String email, final String passwo){

        AndroidNetworking.post("https://www.ragic.com/uniqgrid/crm3/1/"+ ragicId + "?1000508=" +passwo+"&api")
                .addHeaders("Authorization","Basic ajZNMW9hMFQrV2lqT2NxdURuTzJGbS8yRnhrY0crQmpUdGt0R1RPNFhHSldKK2lTL3dBWVhKOG1ScHpEQXVNOQ==") // posting header
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        etEmail.setText("");
                        etNewPassword.setText("");
                        etConfirmNewPassword.setText("");
                        Toast.makeText(ForgotPasswordActivity.this,"Password changed successfully",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Snackbar.make(findViewById(android.R.id.content), "Please check your Network Connection", Snackbar.LENGTH_LONG).show();

                        ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                pd.dismiss();
                            }
                        });
                    }

                });


    }


    public boolean isValidEmailAddress(String email) {

        return  (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    public boolean isValidPassword(String password){
        if(password.length()>=6){
            return  true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        if(pd!=null && pd.isShowing()){
            pd.dismiss();
            AndroidNetworking.forceCancelAll();
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        }

    }
}
