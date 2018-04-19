package com.uniqgrid.solarenergy.uniqgrid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import models.Ticket;

public class TicketsActivity extends AppCompatActivity {

    RecyclerView rvTicketHistory;


    ArrayList<Ticket> ticketsArrayList = new ArrayList<>();
    TicketsAdapter ticketsAdapter;


    ImageView ivBack;

    FloatingActionButton fabLogTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.slide_in_up,R.anim.hold);


        fabLogTicket = (FloatingActionButton) findViewById(R.id.fabLogTicket);

        rvTicketHistory = (RecyclerView) findViewById(R.id.rvTicketHistory);
        ticketsAdapter = new TicketsAdapter();
        rvTicketHistory.setAdapter(ticketsAdapter);
        rvTicketHistory.setLayoutManager(new LinearLayoutManager(TicketsActivity.this));


        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Tickets");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fabLogTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TicketsActivity.this,LogTicketActivity.class);
                startActivity(intent);
            }
        });


        prepareData();


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }


    public  void prepareData(){

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(TicketsActivity.this);
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {
                JSONObject contentJson = new JSONObject(content);
                JSONObject ticketsJson = contentJson.getJSONObject("_subtable_1000538");
                Iterator<String> keys = ticketsJson.keys();
                int i=1;
                while(keys.hasNext()){
                    JSONObject ticketJson = ticketsJson.getJSONObject(keys.next());

                    float feedbackNum=0f;
                    try{
                        String feedback = ticketJson.getString("Feedback");
                        if(!feedback.equals("")){
                            feedbackNum = Float.parseFloat(feedback);
                        }
                    }catch (Exception e){

                    }

                    Ticket ticket = new Ticket(i+"",
                            ticketJson.getString("Comm. Value"),
                            ticketJson.getString("Request"),
                            ticketJson.getString("Created"),
                            ticketJson.getString("Confirmed"),"",
                            ticketJson.getString("Completed"),
                            feedbackNum);
                    ticketsArrayList.add(ticket);
                    i++;
                }

                ticketsAdapter.notifyDataSetChanged();



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>{


        class TicketViewHolder extends RecyclerView.ViewHolder{

            TextView tvTicketNo,tvTicketValue,tvRequest;
            TextView tvCreatedDate,tvConfirmedDate,tvInProgressDate,tvCompletedDate;
            ProgressBar pgCreated,pgConfirmed,pgInProgress,pgCompleted;
            RatingBar rbFeedback;
            Button bSubmitFeedBack;

            public TicketViewHolder(View itemView) {
                super(itemView);
                tvTicketNo = (TextView) itemView.findViewById(R.id.tvTicketNo);
                tvTicketValue = (TextView) itemView.findViewById(R.id.tvTicketValue);
                tvRequest = (TextView) itemView.findViewById(R.id.tvRequest);
                tvCreatedDate = (TextView) itemView.findViewById(R.id.tvCreatedDate);
                tvConfirmedDate = (TextView) itemView.findViewById(R.id.tvConfirmedDate);
                tvInProgressDate = (TextView) itemView.findViewById(R.id.tvInProgressDate);
                tvCompletedDate = (TextView) itemView.findViewById(R.id.tvCompletedDate);

                pgCreated = (ProgressBar) itemView.findViewById(R.id.pgCreated);
                pgConfirmed = (ProgressBar) itemView.findViewById(R.id.pgConfirmed);
                pgInProgress = (ProgressBar) itemView.findViewById(R.id.pgInProgress);
                pgCompleted = (ProgressBar) itemView.findViewById(R.id.pgCompleted);

                rbFeedback = (RatingBar)itemView.findViewById(R.id.rbFeedback);
                bSubmitFeedBack = (Button) itemView.findViewById(R.id.bSubmitFeedBack);



            }
        }

        @Override
        public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View customView = getLayoutInflater().inflate(R.layout.item_ticket,parent,false);
            return new TicketViewHolder(customView);
        }

        @Override
        public void onBindViewHolder(TicketViewHolder holder, int position) {
            Ticket ticket = ticketsArrayList.get(position);
            holder.tvTicketNo.setText("#"+ticket.getTicketNo());
            holder.tvTicketValue.setText(" INR "+ticket.getValue());
            holder.tvRequest.setText(ticket.getRequest());
            if(ticket.getCreatedDate().equals("")){
               holder.pgCreated.setProgress(0);
                holder.tvCreatedDate.setText("");
            }else{
                holder.pgCreated.setProgress(100);
                holder.tvCreatedDate.setText(ticket.getCreatedDate());
            }
            if(ticket.getConfirmedDate().equals("")){
                holder.pgConfirmed.setProgress(0);
                holder.tvConfirmedDate.setText("");
            }else{
                holder.pgConfirmed.setProgress(100);
                holder.tvConfirmedDate.setText(ticket.getConfirmedDate());
            }
            if(ticket.getInProgressDate().equals("")){
                holder.pgInProgress.setProgress(0);
                holder.tvInProgressDate.setText("");
            }else{
                holder.pgInProgress.setProgress(100);
                holder.tvInProgressDate.setText("");
            }
            if(ticket.getCompletedDate().equals("")){
                holder.pgCompleted.setProgress(0);
                holder.tvCompletedDate.setText("");
            }else{
                holder.pgInProgress.setProgress(100);
                holder.pgCompleted.setProgress(100);
                holder.tvCompletedDate.setText(ticket.getCompletedDate());
            }
            holder.rbFeedback.setRating(ticket.getFeedBack());
            holder.bSubmitFeedBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }

        @Override
        public int getItemCount() {
            return ticketsArrayList.size();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slide_down);
    }


}
