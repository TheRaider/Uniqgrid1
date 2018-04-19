package com.uniqgrid.solarenergy.uniqgrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import models.ConsumptionGI;
import models.LoadDistGI;


public class ConsumptionFragment extends Fragment {


    BarChart barChart;
    ArrayList<Integer> colors;
    ArrayList<ConsumptionGI> consumptionGIArrayList = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    NestedScrollView nsAddEst;

    TextView tvAnnualEnergy,tvMaxAnnualEnergy,tvAvgEnergy,tvStandardDeviation,tvAnnualEnergyCost;
    String annualEnergy="-",maxAnnualEnergy="-",avgEnergy="-",standardDeviation="-",annualEnergyCost="-";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View customView =  inflater.inflate(R.layout.fragment_consumption, container, false);


        // Adding colors
        colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);


        // Consumption Pattern Bar Chart

        barChart = (BarChart) customView.findViewById(R.id.barchart);
        barChart.getDescription().setEnabled(false);

//        populateConsumptionBarGraphData();
        prepareData();
        populateConsumptionBarGraph(consumptionGIArrayList,"Consumption");

        barChart.setVisibleXRangeMaximum(7); // set maximum bars visible
        barChart.setPinchZoom(false); // disabling zoom
        barChart.setScaleEnabled(false); // disabling zoom



        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new MyIndexAxisValueFormatter(labels));
        xAxis.setCenterAxisLabels(false);


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setEnabled(false);  // disable axes


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false); // disable gridlines
        rightAxis.setEnabled(false);  // disable axes


        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);


        IMarker marker = new YourMarkerView(getContext(),R.layout.custom_marker_view_layout);
        barChart.setMarker(marker);

        barChart.animateY(1000);

        tvAnnualEnergy = (TextView) customView.findViewById(R.id.tvAnnualEnergy);
        tvMaxAnnualEnergy = (TextView) customView.findViewById(R.id.tvMaxAnnualEnergy);
        tvAvgEnergy = (TextView) customView.findViewById(R.id.tvAvgEnergy);
        tvStandardDeviation = (TextView) customView.findViewById(R.id.tvStandardDeviation);
        tvAnnualEnergyCost = (TextView) customView.findViewById(R.id.tvAnnualEnergyCost);




        loadData();



        return  customView;
    }

    public  void populateConsumptionBarGraphData(){
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",100f));
        consumptionGIArrayList.add(new ConsumptionGI("January",90f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));
        consumptionGIArrayList.add(new ConsumptionGI("January",10f));


    }




    public void populateConsumptionBarGraph(ArrayList<ConsumptionGI> consumptionGIS , String Label){
        if(consumptionGIS.size()==0)
            return;
        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        int i=0;
        for(ConsumptionGI consumptionGI : consumptionGIS) {
              bargroup1.add(new BarEntry(i, consumptionGI.getConsumption())); // xvals,yvals
              i+=1;
            String label = consumptionGI.getMonth();
            if(label.length()>3)
                labels.add(label.substring(0,3));
            else
                labels.add(label);
        }


        // creating dataset for Bar Group1
        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Consumption Pattern");
        barDataSet1.setColors(Color.rgb(3,169,244));

        // initialize the Bardata with argument labels and dataSet
        BarData data = new BarData(barDataSet1);
        data.setDrawValues(false); // disable labels on top
        data.setBarWidth(0.5f);

        barChart.setData(data);


    }

    public  void prepareData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {

                float consumptionNum =0f;
                JSONObject contentJson = new JSONObject(content);
                JSONObject consumptionGIsJson = contentJson.getJSONObject("_subtable_1000500");
                Iterator<String> keys = consumptionGIsJson.keys();
                while(keys.hasNext()){
                    JSONObject consumptionGIJson = consumptionGIsJson.getJSONObject(keys.next());
                    try {
                        consumptionNum = Float.parseFloat(consumptionGIJson.getString("Consumption (kWhr)"));
                    }catch (Exception e){

                    }
                    ConsumptionGI consumptionGI = new ConsumptionGI(consumptionGIJson.getString("Month"),
                            consumptionNum);
                    consumptionGIArrayList.add(consumptionGI);
                }

                annualEnergy = contentJson.getString("Annual Energy");
                maxAnnualEnergy = contentJson.getString("Maximum Energy");
                avgEnergy = contentJson.getString("Average Energy");
                standardDeviation = contentJson.getString("Standard deviation");
                annualEnergyCost = contentJson.getString("Annual Energy cost");





            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadData(){
        tvAnnualEnergy.setText(": " +annualEnergy);
        tvMaxAnnualEnergy.setText(": "+maxAnnualEnergy);
        tvAvgEnergy.setText(": "+avgEnergy);
        tvStandardDeviation.setText(": "+standardDeviation);
        tvAnnualEnergyCost.setText(": INR "+annualEnergyCost);
    }





    public Double RoundOff(Double d){
        return Math.round(d * 100.0) / 100.0;
    }

    class YourMarkerView extends MarkerView {

        private TextView tvContent;

        public YourMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);

            // find your layout components
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            tvContent.setText("" + e.getY());

            // this will perform necessary layouting
            super.refreshContent(e, highlight);
        }

        private MPPointF mOffset;

        @Override
        public MPPointF getOffset() {

            if(mOffset == null) {
                // center the marker horizontally and vertically
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }

            return mOffset;
        }
    }

    public class MyIndexAxisValueFormatter implements IAxisValueFormatter
    {
        private String[] mValues = new String[] {};
        private int mValueCount = 0;

        /**
         * An empty constructor.
         * Use `setValues` to set the axis labels.
         */
        public MyIndexAxisValueFormatter() {
        }

        /**
         * Constructor that specifies axis labels.
         *
         * @param values The values string array
         */
        public MyIndexAxisValueFormatter(String[] values) {
            if (values != null)
                setValues(values);
        }

        /**
         * Constructor that specifies axis labels.
         *
         * @param values The values string array
         */
        public MyIndexAxisValueFormatter(Collection<String> values) {
            if (values != null)
                setValues(values.toArray(new String[values.size()]));
        }

        public String getFormattedValue(float value, AxisBase axis) {
            int index = Math.round(value);

            if (index < 0 || index >= mValueCount || index != (int)value)
                return "";

            return mValues[index];
        }

        public String[] getValues()
        {
            return mValues;
        }

        public void setValues(String[] values)
        {
            if (values == null)
                values = new String[] {};

            this.mValues = values;
            this.mValueCount = values.length;
        }
    }

}



