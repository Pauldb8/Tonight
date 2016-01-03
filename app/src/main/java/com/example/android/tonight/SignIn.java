package com.example.android.tonight;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
//Todo transform to static class
public class SignIn implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView nameDrawer;
    private TextView emailDrawer;
    private de.hdodenhof.circleimageview.CircleImageView pictureDrawer;
    private ProgressDialog mProgressDialog;
    private Activity mContext;
    private User userInfo;
    private boolean authSuccessfull = false;

    /**
     * Public contructor for connecting to Google Sign In
     * @param context
     */
    public SignIn(Activity context){
        this.mContext = context;
        //Setting up the views
        nameDrawer = (TextView) mContext.findViewById(R.id.drawer_name);
        emailDrawer = (TextView) mContext.findViewById(R.id.drawer_email);
        pictureDrawer = (de.hdodenhof.circleimageview.CircleImageView)
                mContext.findViewById(R.id.drawer_circle_view);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        //Testing connection with cached user information
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

            //Todo return user information
        }
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //Here we have access to the user's data so we will update his/her information
            //accordingly
            this.authSuccessfull = true; //We have user information
            userInfo = new User();
            userInfo.setName(acct.getDisplayName());
            userInfo.setEmail(acct.getEmail());
            userInfo.setPictureUrl(acct.getPhotoUrl());
        }
    }
    // [END handleSignInResult]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    public boolean isAuthSuccessfull(){
        return this.authSuccessfull;
    }

    public User getUser(){
        return this.userInfo;
    }
}
