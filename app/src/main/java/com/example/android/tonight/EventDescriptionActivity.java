package com.example.android.tonight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class EventDescriptionActivity extends AppCompatActivity {

    private Event specificEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting the intent
        specificEvent = (Event)getIntent().getSerializableExtra(ListViewLoader.EVENT_ID_INTENT);

        //Setting the view
        setContentView(R.layout.activity_event_description);
/*        FadingActionBarHelper helper = new FadingActionBarHelper()
                .headerLayout(R.layout.activity_event_description_part2)
                .contentLayout(R.layout.activity_event_description_part1);
        setContentView(helper.createView(this));
        helper.initActionBar(this);*/

        //Getting the views in order to fill them with the information from Event
        TextView evTitle = (TextView)findViewById(R.id.evTitle);
        TextView evStartDate = (TextView)findViewById(R.id.evStartDate);
        TextView evPrice = (TextView)findViewById(R.id.evPrice);
        TextView evTime = (TextView)findViewById(R.id.evStartTime);
        TextView evPosition = (TextView)findViewById(R.id.evPosition);
        TextView evDescription = (TextView)findViewById(R.id.evDescription);
        ImageView evPicture = (ImageView)findViewById(R.id.evDescPicture);

        //Filling views with the corresponding information
        evTitle.setText(specificEvent.getName());
        evStartDate.setText(specificEvent.getStartDateFormatted());
        evPrice.setText(String.valueOf(specificEvent.getPriceFormatted()));
        evTime.setText(String.valueOf(specificEvent.getStartHour()) + " - " +
            specificEvent.getEndHour());
        evPosition.setText(specificEvent.getCity());
        evDescription.setText(specificEvent.getDescription());

        //Set the toolbar from compat in order tog et material design toolbar on older android versions
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.descToolbar);

        //Asynchronously download the picture
        //new DownloadImageTask(evPicture).execute(specificEvent.getPicture_url());
        new Tools.DownloadImageTask(evPicture, this, mainToolbar)
                .execute(specificEvent.getPicture_url());

        //Making default toolbar
        setSupportActionBar(mainToolbar);
        //To provide the back button to go the the main activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
