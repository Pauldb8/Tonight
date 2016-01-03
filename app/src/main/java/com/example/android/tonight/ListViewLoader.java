package com.example.android.tonight;

import android.app.ActivityOptions;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

public class ListViewLoader extends ListFragment {

    //Unique key for the intent
    public final static String EVENT_ID_INTENT = "com.example.android.tonight.EVENT_ID";

    // The SearchView for doing filtering.
    SearchView mSearchView;

    //L'adapter pour notre listVieuw
    public EventCustomAdapter mAdapterView;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        setEmptyText("No events");

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        //Get all the events from DB asynchronously and update the ListView accordingly
        getEvents ev = new getEvents(getActivity(), this);
        ev.execute();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        //Log.i("FragmentComplexList", "Item clicked: " + id);

        //Commons views used in both ListViewLoader and EventDescriptionActivity
        //This is necessary for having a smooth transition and visually appealing
        ImageView transitionPicture = (ImageView)v.findViewById(R.id.eventPicture);

        //Getting the id of the selected event from the adapter
        Event selEv = (Event) this.getListAdapter().getItem(position);
        //Log.i("Click Event : ", selEv.toString());

        //Open the description view thanks to an intent with the id of the event
        Intent openDetail = new Intent(v.getContext(), EventDescriptionActivity.class);
        Event evId = selEv;
        openDetail.putExtra(EVENT_ID_INTENT, evId);
        //Compat API 19
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    transitionPicture, "evTransitionPic");

            //Let start the activity
            startActivity(openDetail, options.toBundle());
        }
        else{
            startActivity((openDetail));
        }
    }
}