package com.example.android.tonight;

/**
 * Created by onetec on 15-12-15.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Async task to get all food categories
 * */
public class getEvents extends AsyncTask<Object, Void, Void> {

    private String URL_EVENTS = "http://192.168.1.18/tonight/getEvents.php";
    //This will contain the list of all the events
    private ArrayList<Event> eventsList;
    // This is the Adapter being used to display the list's data.
    private EventCustomAdapter mAdapter;
    private Activity mContext;
    private ListViewLoader myView;

    public getEvents(Activity context, ListViewLoader myView) {
        this.mContext = context;
        this.myView = myView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Object[] parameters) {
        ServiceHandler jsonParser = new ServiceHandler();
        //String json = jsonParser.makeServiceCall(URL_EVENTS, ServiceHandler.GET);

        String json = "{\"events\":[{\"id\":\"2\",\"name\":\"Anniversaire Julie\",\"start_date\":" +
                "\"2015-12-10 20:00:00\",\"end_date\":\"2015-12-10 23:59:59\",\"max_people\":\"10" +
                "\",\"price\":\"0\",\"picture\":\"http:\\/\\/i.imgur.com\\/md0oUSx.jpg\",\"" +
                "description\":\"Anniversaire de Julie, pr\\u00e9 OKLM\",\"city\":\"" +
                "Louvain-La-Neuve\"},{\"id\":\"3\",\"name\":\"Marche de noel\",\"start_date\":" +
                "\"2015-12-04 11:00:00\",\"end_date\":\"2015-12-23 23:59:59\",\"max_people\":" +
                "\"0\",\"price\":\"0\",\"picture\":\"http:\\/\\/i.imgur.com\\/wMupDnj.png\"," +
                "\"description\":\"Comme chaque ann\\u00e9e Louvain-La-Neuve accueille son" +
                " march\\u00e9 de No\\u00ebl sur la grand place.\",\"city\":\"Louvain-La-Neuve\"}" +
                ",{\"id\":\"4\",\"name\":\"Soir\\u00e9e nouvel an\",\"start_date\":\"" +
                "2015-12-31 23:00:00\",\"end_date\":\"2015-12-31 23:59:59\",\"max_people\":\"0\"" +
                ",\"price\":\"0\",\"picture\":\"http:\\/\\/i.imgur.com\\/lP4A2jl.jpg\"," +
                "\"description\":\"Le Becket's Irish Bar organise comme chaque nouvelle " +
                "ann\\u00e9e une soir\\u00e9e sp\\u00e9ciale pour les \\u00e9tudiants. " +
                "Happy hour pour tous les cocktails: 2 pour le prix d'1 jusque 2h du matin. " +
                "Aucune raison n'est bonne pour rater cette soir\\u00e9e et bien commencer " +
                "l'ann\\u00e9e 2016 !\",\"city\":\"Louvain-La-Neuve\"},{\"id\":\"5\",\"name\":" +
                "\"Delirium Caf\\u00e9\",\"start_date\":\"2015-01-01 00:00:00\",\"end_date\":" +
                "\"2015-12-31 23:59:59\",\"max_people\":\"0\",\"price\":\"0\",\"picture\":\"" +
                "http:\\/\\/i.imgur.com\\/RCPosEY.jpg\",\"description\":\"Venez visiter le bar " +
                "mythique D\\u00e9lirium Caf\\u00e9, ouvert tous les jours. Go\\u00fbtez \\u00e0 " +
                "plus de 1000 vari\\u00e9t\\u00e9s de bi\\u00e8re de ce bar \\u00e0 l'ambiance " +
                "d\\u00e9contract\\u00e9e. Situ\\u00e9 pr\\u00e8s de la grand place de Bruxelles." +
                "\",\"city\":\"Brussels\"}]}";

        eventsList = new ArrayList<Event>();
        //mContext = (Context) parameters[0];
        //myView = (ListViewLoader) parameters[1];

        //Log.e("Response: ", "> " + json);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray events = jsonObj
                            .getJSONArray("events");

                    for (int i = 0; i < events.length(); i++) {
                        JSONObject evObj = (JSONObject) events.get(i);
                        Event ev = new Event(evObj.getInt("id"),
                                evObj.getString("name"),
                                parseDateTime(evObj.getString("start_date")),
                                parseDateTime(evObj.getString("end_date")),
                                evObj.getInt("max_people"),
                                evObj.getDouble("price"),
                                evObj.getString("picture"),
                                evObj.getString("description"),
                                evObj.getString("city"));
                        //Log.i("INFO", "Event: " + ev.toString());
                        eventsList.add(ev);
                    }
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
        mAdapter = new EventCustomAdapter(mContext, eventsList);
        myView.setListAdapter(mAdapter);
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