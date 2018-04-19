package com.uniqgrid.solarenergy.uniqgrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import models.SolarGI;
import models.SolarGI;


public class SolarFragment extends Fragment {


    LineChart lineChart;
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<SolarGI> solarGIArrayList = new ArrayList<>();

    TextView tvCumulativeArea,tvRoofTopBlocks,tvFeasibleSolarPv;
    TextView tvOptimalSolarPv,tvNoOfSolarPanels,tvLifetimeSolarGeneration,tvFirstYearSolarGeneration;

    String cumulativeArea="-",roofTopBlocks="-",feasibleSolarPv="-";
    String optimalSolarPv="-",noOfSolarPanels="-",lifetimeSolarGeneration="-",firstYearSolarGeneration="-";

    private String roofTopImageUrlPrefix = "https://www.ragic.com/sims/file.jsp?a=uniqgrid&f=";

    private  String roofTopImageUrl = roofTopImageUrlPrefix;

    ImageView ivSolarRoofTop;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View customView = inflater.inflate(R.layout.fragment_solar, container, false);

        lineChart = (LineChart)customView.findViewById(R.id.lineChart);
        tvCumulativeArea = (TextView) customView.findViewById(R.id.tvCumulativeArea);
        tvRoofTopBlocks = (TextView) customView.findViewById(R.id.tvRoofTopBlocks);
        tvFeasibleSolarPv = (TextView) customView.findViewById(R.id.tvFeasibleSolarPv);
        tvOptimalSolarPv = (TextView) customView.findViewById(R.id.tvOptimalSolarPv);
        tvNoOfSolarPanels = (TextView) customView.findViewById(R.id.tvNoOfSolarPanels);
        tvLifetimeSolarGeneration = (TextView) customView.findViewById(R.id.tvLifetimeSolarGeneration);
        tvFirstYearSolarGeneration = (TextView) customView.findViewById(R.id.tvFirstYearSolarGeneration);

        ivSolarRoofTop = (ImageView) customView.findViewById(R.id.ivSolarRoofTop);

        refreshData();

        return customView;
    }

    public  void refreshData(){
        lineChart.getDescription().setEnabled(false);

//        populateConsumptionBarGraphData();
        prepareData();

        populateSolarLineGraph(solarGIArrayList,"Solar");

        lineChart.setVisibleXRangeMaximum(7);
        lineChart.setPinchZoom(false); // disabling zoom
        lineChart.setScaleEnabled(false); // disabling zoom



        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setEnabled(false);  // disable axes

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false); // disable gridlines
        rightAxis.setEnabled(false);  // disable axes


        Legend l = lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        IMarker marker = new YourMarkerView(getContext(),R.layout.custom_marker_view_layout);
        lineChart.setMarker(marker);


        //      lineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


        loadData();

    }

    public  void prepareData(){

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {
                float pastEnergyNum =0f,targetEnergyNum=0f;
                JSONObject contentJson = new JSONObject(content);
                // Solar rooftop image url
                String imageUrl = contentJson.getString("Solar Image (Rooftop)");
                roofTopImageUrl = roofTopImageUrlPrefix + imageUrl;
                Log.d("Vinnu",roofTopImageUrl);


                Picasso.with(getContext()).
                        load(roofTopImageUrl)
                        .fit()
                        .into(ivSolarRoofTop);

                JSONObject solarGISJson = contentJson.getJSONObject("_subtable_1000586");
                Iterator<String> keys = solarGISJson.keys();
                while(keys.hasNext()){
                    JSONObject solarGIJson = solarGISJson.getJSONObject(keys.next());
                    try {
                        pastEnergyNum = Float.parseFloat(solarGIJson.getString("Past Energy Bill"));
                        targetEnergyNum = Float.parseFloat(solarGIJson.getString("Target Energy Bill"));
                    }catch (Exception e){

                    }
                    SolarGI solarGI = new SolarGI(solarGIJson.getString("Month"),
                            pastEnergyNum,targetEnergyNum);
                    solarGIArrayList.add(solarGI);
                }

                cumulativeArea = contentJson.getString("Cumulative Area in Sq. ft.");
                roofTopBlocks = contentJson.getString("Rooftop Blocks available");
                feasibleSolarPv = contentJson.getString("Feasible Solar PV (kWp)");
                optimalSolarPv = contentJson.getString("Optimal Solar PV (kWp)");
                noOfSolarPanels = contentJson.getString("No. of Solar Panels");
                lifetimeSolarGeneration = contentJson.getString("Lifetime Solar Generation");
                firstYearSolarGeneration = contentJson.getString("First year Solar Generation");





            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public  void loadData(){

//        tvCumulativeArea.setText(": "+cumulativeArea +" Sq. ft.");
//        tvRoofTopBlocks.setText(": "+roofTopBlocks );
//        tvFeasibleSolarPv.setText(": "+feasibleSolarPv +" kWp");
//        tvOptimalSolarPv.setText(": "+optimalSolarPv +" kWp");
//        tvNoOfSolarPanels.setText(": "+noOfSolarPanels);
//        tvLifetimeSolarGeneration.setText(": "+lifetimeSolarGeneration +" kWhr");
//        tvFirstYearSolarGeneration.setText(": "+firstYearSolarGeneration+" kWhr");

        tvCumulativeArea.setText(": "+cumulativeArea);
        tvRoofTopBlocks.setText(": "+roofTopBlocks );
        tvFeasibleSolarPv.setText(": "+feasibleSolarPv);
        tvOptimalSolarPv.setText(": "+optimalSolarPv);
        tvNoOfSolarPanels.setText(": "+noOfSolarPanels);
        tvLifetimeSolarGeneration.setText(": "+lifetimeSolarGeneration);
        tvFirstYearSolarGeneration.setText(": "+firstYearSolarGeneration);

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

    public void populateSolarLineGraph(ArrayList<SolarGI> SolarGIS , String Label){

        if(SolarGIS.size() == 0)
            return;
        ArrayList<Entry> linegroup1 = new ArrayList<>();
        ArrayList<Entry> linegroup2 = new ArrayList<>();
        int i=0;
        for(SolarGI SolarGI : SolarGIS) {

            linegroup1.add(new Entry(i, SolarGI.getPastEnergy())); // xvals,yvals
            linegroup2.add(new Entry(i, SolarGI.getTargetEnergy())); // xvals,yvals
            
            i++;
            String label = SolarGI.getMonth();
            if(label.length()>3)
                labels.add(label.substring(0,3));
            else
                labels.add(label);
        }
   

        // creating dataset for Line Group1
        LineDataSet lineDataSet1 = new LineDataSet(linegroup1, "Previous Cost");
        int color1 = Color.rgb(3,169,244);
        lineDataSet1.setColor(color1);
    //     lineDataSet1.setDrawCircles(false);
        lineDataSet1.setDrawFilled(true);
        lineDataSet1.setCircleColor(color1);
        lineDataSet1.setLineWidth(1f);
        lineDataSet1.setCircleRadius(3f);
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setValueTextSize(9f);
        lineDataSet1.setFillColor(color1);
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);
    //    lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        // creating dataset for Line Group2
        LineDataSet lineDataSet2 = new LineDataSet(linegroup2, "New Cost");
        int color2 = Color.rgb(245,182,129);
        lineDataSet2.setColor(color2);
        lineDataSet2.setDrawFilled(true);
    //    lineDataSet2.setDrawCircles(false);
        lineDataSet2.setCircleColor(color2);
        lineDataSet2.setLineWidth(1f);
        lineDataSet2.setCircleRadius(3f);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setValueTextSize(9f);
        lineDataSet2.setFillColor(color2);
       // lineDataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        ArrayList<ILineDataSet> iDataSets = new ArrayList<>();
        iDataSets.add(lineDataSet1);
        iDataSets.add(lineDataSet2);

        Log.d("vinnnu ","1 :" +linegroup1.size() + " 2 : " + linegroup2.size());

        // initialize the Bardata with argument labels and dataSet
        LineData data = new LineData(iDataSets);
        data.setDrawValues(false); // disable labels on top
        

        lineChart.setData(data);


    }

    public  void populateConsumptionBarGraphData(){
        solarGIArrayList.add(new SolarGI("January",100f , 25f));
        solarGIArrayList.add(new SolarGI("January",90f , 10f));
        solarGIArrayList.add(new SolarGI("January",80f , 15f));
        solarGIArrayList.add(new SolarGI("January",50f , 35f));
        solarGIArrayList.add(new SolarGI("January",70f , 18f));
        solarGIArrayList.add(new SolarGI("January",80f , 26f));
        solarGIArrayList.add(new SolarGI("January",90f , 8f));
        solarGIArrayList.add(new SolarGI("January",60f , 19f));
        solarGIArrayList.add(new SolarGI("January",90f , 21f));
        solarGIArrayList.add(new SolarGI("January",100f , 23f));
        solarGIArrayList.add(new SolarGI("January",50f , 26f));
        solarGIArrayList.add(new SolarGI("January",70f , 22f));






    }



}
