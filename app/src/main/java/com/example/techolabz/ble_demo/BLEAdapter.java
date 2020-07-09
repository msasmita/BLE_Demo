package com.example.techolabz.ble_demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Techolabz on 13-11-2016.
 */
public class BLEAdapter extends ArrayAdapter<BLE> {
    Context context;
    ArrayList<BLE> bleArrayList;
    static final  JSONObject postData = new JSONObject();
    public BLEAdapter(Context context, ArrayList<BLE> bleArrayList) {
        super(context, R.layout.row_structure, bleArrayList);
        this.context = context;
        this.bleArrayList = bleArrayList;
        new PostDistance().execute();
    }

    public static class DataHolder {
        public TextView txtAdddress;
        public TextView txtName;
        public TextView txtuuid;
        public TextView txtmajor;
        public TextView txtminor;
        public TextView txtrssi;
        public TextView txtnamespaceid;
        public TextView txtinstanceid;
        public TextView txturl;
        public TextView txtrawdata;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_structure, null);

            holder = new DataHolder();

            holder.txtAdddress = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtuuid = (TextView) convertView.findViewById(R.id.txtuuid);
            holder.txtmajor = (TextView) convertView.findViewById(R.id.txtmajor);
            holder.txtminor = (TextView) convertView.findViewById(R.id.txtminor);
            holder.txtrssi = (TextView) convertView.findViewById(R.id.txtrssi);
            holder.txtnamespaceid = (TextView) convertView.findViewById(R.id.txtnamespaceid);
            holder.txtinstanceid = (TextView) convertView.findViewById(R.id.txtinstanceid);
            holder.txturl = (TextView) convertView.findViewById(R.id.txturl);
            holder.txtrawdata = (TextView) convertView.findViewById(R.id.txtrawdata);

            convertView.setTag(holder);
        } else {

            holder = (DataHolder) convertView.getTag();
        }

        BLE ble = bleArrayList.get(position);


        String deviceAdd = ble.getDeviceAddress();
        if (deviceAdd.contains("00:A0:50:5E:E2:1A") || deviceAdd.contains ("00:A0:50:4D:37:4D") || deviceAdd.contains("00:A0:50:4D:36:5B")) {

            holder.txtAdddress.setText("Address : " + ble.getDeviceAddress());
            holder.txtuuid.setText("Uuid : " + ble.getUuid());
            holder.txtmajor.setText("Major : " + ble.getMajor());
            holder.txtminor.setText("Minor : " + ble.getMinor());
            holder.txtrssi.setText("Distannce in Meters: " + calculateDistance(ble.getRssi()));
            holder.txtnamespaceid.setText("NamespaceId : " + ble.getNamespaceid());
            holder.txtinstanceid.setText("InstanceId : " + ble.getInstanceid());
            holder.txturl.setText("Url : " + ble.getUrl());

            if (ble.getDeviceName() == null) {
                holder.txtName.setText("Name : " + "N/A");
            } else {
                holder.txtName.setText("Name : " + ble.getDeviceName());
            }

            if (ble.getNamespaceid() == null) {
                holder.txtnamespaceid.setVisibility(View.GONE);
                holder.txtinstanceid.setVisibility(View.GONE);

            } else {
                holder.txtnamespaceid.setVisibility(View.VISIBLE);
                holder.txtinstanceid.setVisibility(View.VISIBLE);
                holder.txtName.setText("Name : " + ble.getDeviceName() + " (Eddystone UID)");

            }

            if (ble.getUuid() == null || ble.getUuid() == "") {
                holder.txtuuid.setVisibility(View.GONE);
                holder.txtmajor.setVisibility(View.GONE);
                holder.txtminor.setVisibility(View.GONE);


            } else {
                holder.txtuuid.setVisibility(View.VISIBLE);
                holder.txtmajor.setVisibility(View.VISIBLE);
                holder.txtminor.setVisibility(View.VISIBLE);
                holder.txtName.setText("Name : " + ble.getDeviceName() + " (iBeacon)");

            }
            if (ble.getUrl() == null) {
                holder.txturl.setVisibility(View.GONE);
            } else {
                holder.txturl.setVisibility(View.VISIBLE);
                holder.txtName.setText("Name : " + ble.getDeviceName() + " (Eddystone Url)");
            }


            if (ble.getRawData() == null) {
                // holder.txtrawdata.setVisibility(View.GONE);


            } else {
                //  holder.txtrawdata.setVisibility(View.VISIBLE);
                holder.txtName.setText("Name : " + ble.getDeviceName());
            }


                    try {
                        if (ble.getDeviceAddress() == "00:A0:50:5E:E2:1A") {
                            postData.put("1A", calculateDistance(ble.getRssi()));
                        } else if (ble.getDeviceAddress() == "00:A0:50:4D:37:4D") {
                            postData.put("4D", calculateDistance(ble.getRssi()));
                        } else if (ble.getDeviceAddress() == "00:A0:50:4D:36:5B") {
                            postData.put("5B", calculateDistance(ble.getRssi()));
                        }
//                        postData.put("2A", "10");
//                        postData.put("3B", "10");
//                        postData.put("4A", "10");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

        return convertView;

        }else{
               return convertView;
           }
    }



    public Double calculateDistance(String rssi) {
        int txPower = -59 ;//hard coded power value. Usually ranges between -59 to -65
        Double inum = Double.parseDouble(rssi);
        if (inum == 0) {
            return -1.0;
        }
        Double ratio = (inum * 1.0) / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            Double distance =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return distance;
        }
    }

//    public void sendDistance() {
//        try {
//        JSONObject o =new JSONObject();
//        o.put("11B", "30");
//        o.put("3B", "30");
//        o.put("5B", "30");
//        URL urlForGetRequest = null;
//        urlForGetRequest = new URL("http://www.requestwall.com/beacons/save-user-location.php");
//        HttpURLConnection postConnection = (HttpURLConnection) urlForGetRequest.openConnection();
//        postConnection.setRequestMethod("POST");
//        postConnection.setRequestProperty("Content-Type", "application/json");
//        postConnection.setRequestProperty("Accept", "raw");
//        postConnection.setDoOutput(true);
//
//        OutputStream os = postConnection.getOutputStream();
//        byte[] input = o.toString().getBytes("utf-8");
//        os.write(input, 0, input.length);
//        BufferedReader br = new BufferedReader(new InputStreamReader(postConnection.getInputStream(), "utf-8"));
//        StringBuilder response = new StringBuilder();
//        String responseLine = null;
//        while ((responseLine = br.readLine()) != null) {
//            response.append(responseLine.trim());
//        }
//
//        System.out.println("DDDDDDDDDDD");
////            OkHttpClient client = new OkHttpClient();
////            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
////            RequestBody body = RequestBody.create(mediaType, "{'1A': \"220\", '5B': 0.7, '4d': 0.8}");
////            Request request = new Request.Builder()
////                    .url("http://www.requestwall.com/beacons/save-user-location.php")
////                    .method("POST", body)
////                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
////                    .build();
////            Response response = client.newCall(request).execute();
//
////            System.out.println(response.toString());
//    }catch(Exception e){
//        e.printStackTrace();
//
//    }
//    }

}
