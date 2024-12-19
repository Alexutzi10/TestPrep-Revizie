package com.example.revizie.network;

import android.util.Log;

import com.example.revizie.data.DateConverter;
import com.example.revizie.data.Revizie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
    public static List<Revizie> getFromJson(String json){
        try {
            JSONObject root = new JSONObject(json);
            JSONObject details = root.getJSONObject("details");
            List<Revizie> revizii = new ArrayList<>();

            JSONArray datasets = details.getJSONArray("datasets");
            for (int i = 0; i < datasets.length(); i++) {
                JSONObject obj = datasets.getJSONObject(i);
                JSONObject revizie = obj.getJSONObject("revizie");

                int  nr = revizie.getInt("price");
                String due = revizie.getString("dueDate");
                String name = revizie.getString("serviceName");

                Date date;
                try {
                    date = DateConverter.toDate(due);
                } catch (Exception ex) {
                    Log.e("parse", "error parsing date");
                    continue;
                }

                Revizie revizieItem = new Revizie(nr, date, name);
                revizii.add(revizieItem);
            }
            return revizii;
        } catch (JSONException e) {
            Log.e("parse", "error parsing json");
        }
        return new ArrayList<>();
    }
}
