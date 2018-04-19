package com.uniqgrid.solarenergy.uniqgrid;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Utils.ColorTemplates;
import models.Asset;
import models.LoadDistGI;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssetsFragment extends Fragment {


    PieChart pieChart;
    ArrayList<LoadDistGI> loadDistGIArrayList = new ArrayList<>();
    ArrayList<Integer> colors;


    RecyclerView rvLighting,rvCoolingAndHeating,rvWater,rvAppliances;
    ArrayList<Asset> allAssetsArrayList = new ArrayList<>();
    
    ArrayList<Asset> lightingAssetsList = new ArrayList<>();
    ArrayList<Asset> coolingAndHeatingAssetsList = new ArrayList<>();
    ArrayList<Asset> waterAssetsList = new ArrayList<>();
    ArrayList<Asset> appliancesAssetsList = new ArrayList<>();

    // A map that maps name of asset with it's image
    HashMap<String,Integer> assetNameToImageMap = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View customView = inflater.inflate(R.layout.fragment_assets, container, false);

        // Adding colors
        colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        for (int c : ColorTemplates.COLOR_SET_3)
            colors.add(c);



        // Load Distribution PieChart
        pieChart = (PieChart) customView.findViewById(R.id.pieChart);

        // Setting Legend(actually removing the legend)
        //pieChart.getLegend().setEnabled(false);
        pieChart.getLegend().setWordWrapEnabled(true);
        //  pieChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        //  pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setRotationEnabled(false);
        pieChart.setDrawSliceText(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);

        //  pieChart.setCenterText("Total  \n " + RoundOff(100));

        prepareData();
        loadData();
        populatePieGraph(loadDistGIArrayList,"Load Distribution");

        setAssetNameToImageMapping();
        seperateAssetsBasedOnCategory();

        rvLighting = (RecyclerView) customView.findViewById(R.id.rvLighting);
        rvLighting.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvLighting.setAdapter(new AssetsAdapter(lightingAssetsList));


        rvCoolingAndHeating = (RecyclerView) customView.findViewById(R.id.rvCoolingAndHeating);
        rvCoolingAndHeating.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvCoolingAndHeating.setAdapter(new AssetsAdapter(coolingAndHeatingAssetsList));

        rvWater = (RecyclerView) customView.findViewById(R.id.rvWater);
        rvWater.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvWater.setAdapter(new AssetsAdapter(waterAssetsList));

        rvAppliances = (RecyclerView) customView.findViewById(R.id.rvAppliances);
        rvAppliances.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvAppliances.setAdapter(new AssetsAdapter(appliancesAssetsList));
        
        




        return  customView;
    }


    void setAssetNameToImageMapping(){
        assetNameToImageMap.put("Airconditioner",R.drawable.appliance_air_conditioner);
        assetNameToImageMap.put("AirCooler",R.drawable.appliance_air_cooler);
        assetNameToImageMap.put("Audio and Speaker",R.drawable.appliance_audio_and_speaker);
        assetNameToImageMap.put("Camera and Security",R.drawable.appliance_camera_security);
        assetNameToImageMap.put("CFL",R.drawable.appliance_cfl);
        assetNameToImageMap.put("Computer",R.drawable.appliance_computer);
        assetNameToImageMap.put("Diesel Generator",R.drawable.appliance_diesel_generator);
        assetNameToImageMap.put("DVD Player",R.drawable.appliance_dvd_player);
        assetNameToImageMap.put("Exhaust",R.drawable.appliance_exhaust_fan);
        assetNameToImageMap.put("Fan",R.drawable.appliance_fan);
        assetNameToImageMap.put("//Geyser",R.drawable.appliance_geyser); // geyser is not present in database
        assetNameToImageMap.put("Incandescent bulb",R.drawable.appliance_incandescent_bulb);
        assetNameToImageMap.put("LED light bulb",R.drawable.appliance_led_bulb);
        assetNameToImageMap.put("Photocopier",R.drawable.appliance_photocopier);
        assetNameToImageMap.put("Projector",R.drawable.appliance_projector);
        assetNameToImageMap.put("Refrigerator",R.drawable.appliance_refrigerator);
        assetNameToImageMap.put("Room-heater",R.drawable.appliance_room_heater);
        assetNameToImageMap.put("Portable Scanner",R.drawable.appliance_scanner);
        assetNameToImageMap.put("Small Printer",R.drawable.appliance_small_printer);
        assetNameToImageMap.put("Smart Board",R.drawable.appliance_smart_board);
        assetNameToImageMap.put("Solar PV Battery",R.drawable.appliance_solar_battery);
        assetNameToImageMap.put("Solar PV",R.drawable.appliance_solar_pv);
        assetNameToImageMap.put("Television",R.drawable.appliance_television);
        assetNameToImageMap.put("Tubelight",R.drawable.appliance_tubelight);
        assetNameToImageMap.put("UPS Battery",R.drawable.appliance_ups_battery);
        assetNameToImageMap.put("Water dispenser",R.drawable.appliance_water_dispenser);
        assetNameToImageMap.put("Water pump",R.drawable.appliance_water_pump);
        assetNameToImageMap.put("Water Purifier",R.drawable.appliance_water_purifier);




    }
    public  void prepareData(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String content = app_preferences.getString("content","abcd");
        if(!content.equals("abcd")){
            try {

                JSONObject contentJson = new JSONObject(content);
                JSONObject appliancesJson = contentJson.getJSONObject("_subtable_1000506");
                Iterator<String> keys = appliancesJson.keys();
                while(keys.hasNext()){
                    JSONObject assetJson = appliancesJson.getJSONObject(keys.next());
                    Asset asset = new Asset(assetJson.getString("Category"),
                            assetJson.getString("Asset Type"),
                            assetJson.getString("Wattage"),
                            assetJson.getString("Quantity"),
                            assetJson.getString("Total Wattage") );
                    allAssetsArrayList.add(asset);
                }


                JSONObject pieGraphJson = contentJson.getJSONObject("_subtable_1000555");
                keys = pieGraphJson.keys();
                while(keys.hasNext()){
                    JSONObject pieGraphDataJson = pieGraphJson.getJSONObject(keys.next());
                    String category = pieGraphDataJson.getString("Asset Category");
                    String kW = pieGraphDataJson.getString("kW");
                    String percent = pieGraphDataJson.getString("%");
                    double kWNum = 0,percentNum=0;
                    try {
                         kWNum = Double.parseDouble(kW);
                         percentNum = Double.parseDouble(percent);
                    }catch (NumberFormatException e){

                        Log.d("Unable to parse",kW + " "+percent);
                    }

                    LoadDistGI loadDistGI = new LoadDistGI(category,kWNum ,percentNum);
                    loadDistGIArrayList.add(loadDistGI);
                }






            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadData(){










    }




    public  void loadAssets(){
//        allAssetsArrayList.add(new Asset("Lighting","CFL",10,4,40));
//        allAssetsArrayList.add(new Asset("Lighting","Tubelight",10,4,40));
//        allAssetsArrayList.add(new Asset("Lighting","LED light bulb",10,4,40));
//        allAssetsArrayList.add(new Asset("Lighting","Incandescent bulb",10,4,40));
//
//        allAssetsArrayList.add(new Asset("Cooling and Heating","Airconditioner",10,4,40));
//        allAssetsArrayList.add(new Asset("Cooling and Heating","AirCooler",10,4,40));
//        allAssetsArrayList.add(new Asset("Cooling and Heating","//Geyser",10,4,40));
//        allAssetsArrayList.add(new Asset("Cooling and Heating","Exhaust",10,4,40));
//        allAssetsArrayList.add(new Asset("Cooling and Heating","Fan",10,4,40));
//        allAssetsArrayList.add(new Asset("Cooling and Heating","Room-heater",10,4,40));
//        allAssetsArrayList.add(new Asset("Cooling and Heating","Refrigerator",10,4,40));
//
//
//        allAssetsArrayList.add(new Asset("Water","Water dispenser",10,4,40));
//        allAssetsArrayList.add(new Asset("Water","Water pump",10,4,40));
//        allAssetsArrayList.add(new Asset("Water","Water Purifier",10,4,40));
//
//        allAssetsArrayList.add(new Asset("Appliances","Audio and Speaker",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Camera and Security",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Computer",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","DVD Player",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Diesel Generator",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Photocopier",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Projector",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Portable Scanner",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Room-heater",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Small Printer",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Smart Board",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Solar PV Battery",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Solar PV",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","UPS Battery",10,4,40));
//        allAssetsArrayList.add(new Asset("Appliances","Television",10,4,40));
        
        
        
        
        
        
        

    }

    public  void seperateAssetsBasedOnCategory(){

        if(allAssetsArrayList.size() == 0) {
            return;
        }
        for(Asset asset : allAssetsArrayList){
            switch (asset.getCategory()){
                case "Lighting":
                    lightingAssetsList.add(asset);
                    break;
                case "Cooling and Heating":
                    coolingAndHeatingAssetsList.add(asset);
                    break;
                case "Water":
                    waterAssetsList.add(asset);
                    break;
                case "Appliance":
                    appliancesAssetsList.add(asset);
                    break;
                default:
                    break;
                
            }
        }
        
    }


    public void populatePieGraphData(){

        loadDistGIArrayList.add(new LoadDistGI("Lighting",4392 ,40));
        loadDistGIArrayList.add(new LoadDistGI("Cooling and Heating",4392 ,20));
        loadDistGIArrayList.add(new LoadDistGI("Water",4392 ,29));
        loadDistGIArrayList.add(new LoadDistGI("Appliances",4392 ,1));




    }
    public void populatePieGraph(ArrayList<LoadDistGI> graphItems , String Label){
        ArrayList<PieEntry> yValues = new ArrayList<>();
        for(LoadDistGI graphItem : graphItems) {

            if(graphItem.getkW() > 0){
                yValues.add(new PieEntry((float) graphItem.getPercent(),graphItem.getCategory()));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(yValues,"");
        pieDataSet.setColors(colors);
        //  pieDataSet.setLabel("Load Distribution");
        pieDataSet.setSliceSpace(2f);

        IValueFormatter formatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value+"";
            }
        };


        IValueFormatter MyValueFormatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if(value < 5) return "";
                else return value + " %";
            }
        };


        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(11f);
        //  pieData.setValueFormatter(formatter);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.animateY(1400);


    }

    class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.AssetViewHolder>{


        ArrayList<Asset> assetArrayList = new ArrayList<>();

        public AssetsAdapter(ArrayList<Asset> assetArrayList) {
            this.assetArrayList = assetArrayList;
        }

        public AssetsAdapter() {
        }

        public  class AssetViewHolder extends  RecyclerView.ViewHolder{

            ImageView ivAssetImage;
            TextView tvAssetName,tvAssetQuantity,tvAssetWattage;
            public AssetViewHolder(View itemView) {
                super(itemView);
                
                ivAssetImage = (ImageView) itemView.findViewById(R.id.ivAssetImage);
                tvAssetName = (TextView) itemView.findViewById(R.id.tvAssetName);
                tvAssetQuantity = (TextView) itemView.findViewById(R.id.tvAssetQuantity);
                tvAssetWattage = (TextView) itemView.findViewById(R.id.tvAssetWattage);
                
                
            }
        }


        @Override
        public AssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View customView = getLayoutInflater().inflate(R.layout.list_item_asset,parent,false);
            return new AssetViewHolder(customView);
        }

        @Override
        public void onBindViewHolder(AssetViewHolder holder, int position) {

            Asset asset = assetArrayList.get(position);
            int imageId = -1;
            if(assetNameToImageMap.get(asset.getAssetType()) != null){
                imageId = assetNameToImageMap.get(asset.getAssetType());
            }

            if(imageId != -1)
            holder.ivAssetImage.setImageDrawable(getResources().getDrawable(imageId));

            holder.tvAssetName.setText(asset.getAssetType());
            holder.tvAssetQuantity.setText(asset.getQuantity()+"");
            holder.tvAssetQuantity.setText(asset.getTotalWattage()+"");

            
        }

        @Override
        public int getItemCount() {
            return assetArrayList.size();
        }

    }

}
