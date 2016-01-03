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


public class MainActivity extends AppCompatActivity {

    public static ListViewLoader myView;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    static final int GET_SIGNIN_INFO = 8020;  // The request code
    private User userInfo;

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        //Setting up the user's info from Google if silent sign in worked
        SignIn authToGoogle = new SignIn(this);
        Log.i("Google Sign In", String.valueOf(authToGoogle.isAuthSuccessfull()));
        if(authToGoogle.isAuthSuccessfull()){
            userInfo = new User(authToGoogle.getUser().getName(), authToGoogle.getUser().getEmail()
            , authToGoogle.getUser().getPictureUrl());
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_name)).setText(userInfo.getName());
            ((TextView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_email)).setText(userInfo.getEmail());
            ((de.hdodenhof.circleimageview.CircleImageView)((View)mNavigationView.getHeaderView(0))
                    .findViewById(R.id.drawer_circle_view)).setImageURI(userInfo.getPictureUrl());
        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        startActivityForResult(new Intent(getApplicationContext(), SignInActivity.class), GET_SIGNIN_INFO);
                        break;
                    // TODO - Handle other items
                }
                menuItem.setChecked(false);
                return true;
            }
        });
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
            startActivity(new Intent(this, SignInActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == GET_SIGNIN_INFO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

}
