package com.example.android.tonight;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by onetec on 15-12-15.
 */
public class EventCustomAdapter extends ArrayAdapter<Event> {

    Activity mContext;
    public EventCustomAdapter(Activity context, ArrayList<Event> events) {
        super(context, 0, events);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = (Event) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.event_excerpt_list, parent, false);
        }

        // Lookup view for data population
        TextView evName = (TextView) convertView.findViewById(R.id.eventName);
        TextView evDate = (TextView) convertView.findViewById(R.id.eventDate);
        TextView evMaxPeople = (TextView) convertView.findViewById(R.id.eventMaxPerson);
        TextView evPrice = (TextView) convertView.findViewById(R.id.eventPrice);
        TextView evHour = (TextView) convertView.findViewById(R.id.eventHour);
        //Background picture for the current line
        ImageView evPicture = (ImageView) convertView.findViewById(R.id.eventPicture);

        // Populate the data into the template view using the data object
        evName.setText(event.getName());
        evDate.setText(event.getStartDateFormatted());
        evMaxPeople.setText(String.valueOf(event.getMax_people()));
        //evPicture.setImageURI(Uri.parse(event.getPicture_url()));
        evPrice.setText(event.getPriceFormatted());
        evHour.setText(event.getStartHour()+ " - " + event.getEndHour());

        //We put a default picture
        //evLine.setBackground(getContext().getResources().getDrawable(R.drawable.brussels));

        new Tools.DownloadImageTask(evPicture, mContext)
                .execute(event.getPicture_url());
        // Return the completed view to render on screen
        return convertView;
    }


}