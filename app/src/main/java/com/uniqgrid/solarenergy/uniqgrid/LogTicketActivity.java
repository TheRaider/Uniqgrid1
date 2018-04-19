package com.uniqgrid.solarenergy.uniqgrid;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogTicketActivity extends AppCompatActivity {

    ImageView ivBack;
    TextView tvDate,tvRequestDate;
    int mYear,mMonth,mDay;
    String ticketDate;
    Long date_value;
    DatePickerDialog.OnDateSetListener dateSetListener;
    LinearLayout llDate;
    EditText etRequest;

    Button bSubmitRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_ticket);


        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvRequestDate = (TextView)findViewById(R.id.tvRequestDate);
        llDate = (LinearLayout) findViewById(R.id.llDate);
        bSubmitRequest = (Button) findViewById(R.id.bSubmitRequest);
        etRequest = findViewById(R.id.etRequest);

        setDate();

        bSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requestDate = tvRequestDate.getText().toString();
                String targetDate = tvDate.getText().toString();
                if(etRequest.getText().toString().equalsIgnoreCase("")){
                    Snackbar.make(findViewById(android.R.id.content),"Please enter your request",Snackbar.LENGTH_SHORT).show();
                }else{
                    String request = etRequest.getText().toString();
                    String msg = "Request Date : " + requestDate + "\n"
                            + "Request : " + request + "\n"
                            + "Target Date : " +targetDate ;
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                    openWhatsApp1(msg);
                }

            }
        });


    }

    private void openWhatsApp(String message) {
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.setType("text/plain");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
//        sendIntent.setPackage("com.whatsapp");

        String smsNumber = "7799122202"; // E164 format without '+' sign
        Uri uri = Uri.parse("smsto:" + smsNumber);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
       // i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        i.putExtra(Intent.EXTRA_TEXT, "I'm the body.");
      //  i.putExtra("sms_to",message);

        if (i.resolveActivity(this.getPackageManager()) == null) {
            Toast.makeText(this, "Please Install Whatsapp to submit a request" , Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(i);
        // startActivity(sendIntent);
    }

    public  void openWhatsApp1(String message){

        // Reference : http://howdygeeks.com/send-whatsapp-message-unsaved-number-android/
        String smsNumber = "917799122202";
        try {
            PackageManager packageManager = this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ smsNumber +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openWhatsApp(String message , String extra) {

        String smsNumber = "7799122202";
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
        sendIntent.putExtra(message,
                PhoneNumberUtils.stripSeparators(smsNumber)+"@s.whatsapp.net");//phone number without "+" prefix


        startActivity(sendIntent);
        if (sendIntent.resolveActivity(this.getPackageManager()) == null) {
            Toast.makeText(this, "Please Install Whatsapp to submit a request" , Toast.LENGTH_SHORT).show();
            return;
        }

        // startActivity(sendIntent);
    }







    public  void  setDate(){


        // Getting Current Date and Time
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        date_value = c.getTimeInMillis();

        // Setting Default Time and Date
        ticketDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
        tvDate.setText(ticketDate);

        SimpleDateFormat f1 = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = null;
        try {
            d1 = f1.parse(ticketDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final long time_to_add = date_value - d1.getTime();

        // Date Picker
        dateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                ticketDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
                tvDate.setText(ticketDate);

                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date d = f.parse(ticketDate);

                    date_value = d.getTime() + time_to_add;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LogTicketActivity.this,dateSetListener,mYear,mMonth,mDay);
//                datePickerDialog.getWindow().setWindowAnimations(R.style.DialogAnimationUpDown);
                datePickerDialog.show();
            }
        });
    }
}
