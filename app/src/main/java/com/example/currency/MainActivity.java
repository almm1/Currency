package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private String time;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GetValute getValute = new GetValute();
    // URL to get contacts JSON
    private static String url = "https://www.cbr-xml-daily.ru/daily_json.js";
    ArrayList<HashMap<String, String>> valuteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valuteList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);

        getValute.execute();

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });
        Timer timer = new Timer();
        timer.schedule(new UpdateTimeTask(), 300000, 300000); //тикаем каждую секунду без задержки
    }

    private class GetValute extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Handler sh = new Handler();
            JsonParser parser = new JsonParser();
            JSONObject [] obj;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr).getJSONObject("Valute");
                    time = new JSONObject(jsonStr).getString("Timestamp");
                    JsonObject jsonObject = parser.parse(String.valueOf(jsonObj)).getAsJsonObject();
                    String []keys =  jsonObject.keySet().toArray(new String[jsonObject.keySet().size()]);

                    obj = new JSONObject[keys.length];

                    for (int i =0; i<keys.length; i++){
                        obj[i] = jsonObj.getJSONObject(keys[i]);

                        HashMap<String, String> val = new HashMap<>();

                        // adding each child node to HashMap key => value
                        val.put("CharCode", obj[i].getString("CharCode"));
                        val.put("Name", obj[i].getString("Name"));
                        val.put("Value", obj[i].getString("Value"));
                        val.put("Nominal", obj[i].getString("Nominal"));
                        float cur = (float) obj[i].getDouble("Value");
                        float prev = (float) obj[i].getDouble("Previous");
                        float difference = cur-prev;
                        difference = (float) (Math.round(difference * 100) / 100.0);
                        String string="";
                        if (difference > 0){
                            string =  "+"+Float.toString(difference);
                        }
                        else if (difference < 0){
                            string =  Float.toString(difference);
                        }
                        else
                            string = "0";
                        val.put("Difference", string);

                        // adding contact to contact list
                        valuteList.add(val);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, valuteList,
                    R.layout.list_item, new String[]{"CharCode", "Name",
                    "Value", "Nominal", "Difference"}, new int[]{R.id.CharCode,
                    R.id.name, R.id.value, R.id.nominal, R.id.difference}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView = (TextView) view.findViewById(R.id.difference);
                    String s = (String) textView.getText();

                    if (s.charAt(0) == '+')
                        textView.setTextColor(Color.parseColor("#00FF85"));
                    else if (s.charAt(0) == '-')
                        textView.setTextColor(Color.parseColor("#DC5A5A"));
                    else if (s.charAt(0) == '0')
                        textView.setTextColor(Color.parseColor("#727272"));
                    return view;
                }
            };
            lv.setAdapter(adapter);
        }
    }

    public void toConvert(View view){
        TextView textView = (TextView) view.findViewById(R.id.CharCode);
        String s = (String) textView.getText();

        TextView text = (TextView) view.findViewById(R.id.value);
        String v = (String) text.getText();
        float f = Float.parseFloat(v);

        TextView nom = (TextView) view.findViewById(R.id.nominal);
        String nn = (String) nom.getText();
        int n = Integer.parseInt(nn);

        Intent intent = new Intent(this, Convert.class);
        intent.putExtra("CharCode", s);
        intent.putExtra("value", f);
        intent.putExtra("nominal", n);
        startActivity(intent);
    }

    private class UpdateTimeTask extends TimerTask {
        @Override
        public void run() {
           update();
        }
    }

    private void update(){
        getValute = null;
        valuteList.clear();
        System.gc();
        getValute = new GetValute();
        getValute.execute();
        time = "На момент: "+ time;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();

                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }
}