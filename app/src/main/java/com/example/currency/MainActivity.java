package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
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
    }


    private class GetValute extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Handler sh = new Handler();
            JsonParser parser = new JsonParser();
            Valute[] valute;
            JsonObject [] obj;

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JsonObject jsonObject = parser.parse(String.valueOf(new JSONObject(jsonStr).getJSONObject("Valute"))).getAsJsonObject();
                    String []keys =  jsonObject.keySet().toArray(new String[jsonObject.keySet().size()]);

                    valute = new Valute[keys.length];
                    obj = new JsonObject[keys.length];

                    for (int i =0; i<keys.length; i++){
                        obj[i] = jsonObject.getAsJsonObject(keys[i]);
                        valute[i] = new Valute(
                                obj[i].get("ID"),
                                obj[i].get("NumCode"),
                                obj[i].get("CharCode"),
                                obj[i].get("Nominal"),
                                obj[i].get("Name"),
                                obj[i].get("Value"),
                                obj[i].get("Previous"));
                        valute[i].valute = keys[i];

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }

         /*   ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, valuteList,
                    R.layout.list_item, new String[]{"charCode", "name",
                    "value"}, new int[]{R.id.CharCode,
                    R.id.name, R.id.value});

            lv.setAdapter(adapter);*/
            return null;
        }
    }
}