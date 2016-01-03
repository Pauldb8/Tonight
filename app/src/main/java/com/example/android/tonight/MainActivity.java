package com.example.android.tonight;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ListViewLoader myView;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    static final int GET_SIGNIN_INFO = 8020;  // The request code
    private User userInfo;
    private SignIn googleConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = new ListViewLoader();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.mainLayout, myView)
                    .commit();
        }
        //Set the toolbar from compat in order tog et material design toolbar on older android versions
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        //Getting the drawer nav
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        //This checks the if the user is connected and upate the drawer info accordingly
        updateDrawerUserInfo(mNavigationView);
        //Setting up a listener for the item in the drawer
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            //startActivity(new Intent(this, SignInActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is a listener to the item button on the drawer
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        //menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_drawer_signin:
                startActivityForResult(new Intent(getApplicationContext(), SignInActivity.class), GET_SIGNIN_INFO);
                updateDrawerUserInfo(mNavigationView);
                break;
            case R.id.nav_drawer_signoff:
                startActivityForResult(new Intent(getApplicationContext(), SignInActivity.class), GET_SIGNIN_INFO);
                updateDrawerUserInfo(mNavigationView);
                // TODO - Handle other items
        }
        //menuItem.setChecked(false);
        return true;
    }

    /**
     * This method receive the result from the intent SignIn or SignOff
     * and the calls updateDrawerUserInfo to update the view reflecting the changes
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GET_SIGNIN_INFO) {
            // Make sure the request was successful
            //if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                updateDrawerUserInfo(mNavigationView);
                // Do something with the contact here (bigger example below)
            //}
        }
    }

    /**
     * This method checks if the user is signed in and update the drawer info accordingly
     * If it is not the case then we show it as Anonymous
     * @param mNavigationView
     */
    public void updateDrawerUserInfo(NavigationView mNavigationView) {
        googleConnection = new SignIn(this);
        //Setting up the user's info from Google if silent sign in worked
        Log.i("Google Sign In", String.valueOf(googleConnection.isAuthSuccessfull()));
        if(googleConnection.isAuthSuccessfull()){ //User is signed in
            //Let's change the menu layout for the online version
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawer_menu_online);

            userInfo = new User(googleConnection.getUser().getName(), googleConnection.getUser().getEmail()
                    , googleConnection.getUser().getPictureUrl());
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_name)).setText(userInfo.getName());
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_email)).setText(userInfo.getEmail());
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_email)).setVisibility(View.VISIBLE);
            new Tools.DownloadImageTask(((de.hdodenhof.circleimageview.CircleImageView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_circle_view)), this)
                    .execute(String.valueOf(userInfo.getPictureUrl()));
        }
        else { //User is not signed in
            //Let's change the menu layout for the online version
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawer_menu);
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_name)).setText("Anonymous");
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_email)).setVisibility(View.INVISIBLE);
            ((de.hdodenhof.circleimageview.CircleImageView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_circle_view)).setImageDrawable(
                    getDrawable(R.drawable.ic_account_circle));

        }
    }

}
