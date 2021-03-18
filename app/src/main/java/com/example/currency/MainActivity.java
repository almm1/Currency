package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ListView lv;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    // URL to get contacts JSON
    private static String url = "https://www.cbr-xml-daily.ru/daily_json.js";

    ArrayList<HashMap<String, String>> valuteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valuteList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);

        new GetValute().execute();

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                 new GetValute().execute();

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
                    JsonObject jsonObject = parser.parse(String.valueOf(new JSONObject(jsonStr).getJSONObject("Valute"))).getAsJsonObject();
                    String []keys =  jsonObject.keySet().toArray(new String[jsonObject.keySet().size()]);

                    obj = new JSONObject[keys.length];

                    for (int i =0; i<keys.length; i++){
                       // obj[i] = jsonObject.getAsJsonObject(keys[i]);
                        obj[i] = jsonObj.getJSONObject(keys[i]);

                        HashMap<String, String> val = new HashMap<>();

                        // adding each child node to HashMap key => value
                        val.put("CharCode", obj[i].getString("CharCode"));
                        val.put("Name", obj[i].getString("Name"));
                        val.put("Value", obj[i].getString("Value"));

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
                    "Value"}, new int[]{R.id.CharCode,
                    R.id.name, R.id.value});
            lv.setAdapter(adapter);
        }
    }

    public void toConvert(View view){
        Intent intent = new Intent(this, Convert.class);
        startActivity(intent);
    }
}