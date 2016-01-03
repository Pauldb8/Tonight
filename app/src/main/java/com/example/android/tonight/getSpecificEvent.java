package com.example.android.tonight;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Async task to get the information about the specific event
 * */
public class getSpecificEvent extends AsyncTask<Object, Void, Void> {

    private String URL_EVENTS = "http://192.168.1.18/tonight/getSpecificEvent.php";
    //This will contain the event
    private Event specificEvent;
    private ArrayList<Event> eventsList;
    // This is the Adapter being used to display the list's data.
    //private SpecificEventCustomAdapter mAdapter;
    //This is the id of the event
    private int evId;
    private Context mContext;
    private GridView myView;

    public getSpecificEvent(Context context, GridView myView, int id) {
        this.mContext = context;
        this.myView = myView;
        this.evId = id;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Object[] parameters) {
        ServiceHandler jsonParser = new ServiceHandler();
        //Adapt the url to add the id paramater
        URL_EVENTS = URL_EVENTS + "?id=" + evId;
        String json = jsonParser.makeServiceCall(URL_EVENTS, ServiceHandler.GET);
        //mContext = (Context) parameters[0];
        //myView = (ListViewLoader) parameters[1];

        Log.e("Response: ", "> " + json);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray events = jsonObj
                            .getJSONArray("events");

                    //for (int i = 0; i < events.length(); i++) {
                        //JSONObject evObj = (JSONObject) events.get(i);
                        JSONObject evObj = (JSONObject) events.get(0);
                        specificEvent = new Event(evObj.getInt("id"),
                                evObj.getString("name"),
                                parseDateTime(evObj.getString("start_date")),
                                parseDateTime(evObj.getString("end_date")),
                                evObj.getInt("max_people"),
                                evObj.getDouble("price"),
                                evObj.getString("picture"),
                                evObj.getString("description"),
                                evObj.getString("city"));

                    eventsList = new ArrayList<Event>();
                    eventsList.add(specificEvent);

                        Log.i("INFO", "Event: " + specificEvent.toString());
                    //}
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "Didn't receive any data from server!");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //mAdapter = new SpecificEventCustomAdapter(myView.getContext(), eventsList);
        //myView.setAdapter(mAdapter);
    }

    /**
     * This fonction returns a Date var from a String formatted as "2015-12-25 13:15:17"
     * @param dateString
     * @return Date variable with the date from the string
     */
    public static Date parseDateTime(String dateString) {
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return fmt.parse(dateString);
        }
        catch (ParseException e) {
            Log.e("ERROR", "Could not parse datetime: " + dateString);
            return null;
        }
    }

}