package com.example.android.tonight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class contains various class tools which can be used multiple times
 */
public class Tools {

    /**
     * This class allows to easily download the background image in another thread
     * and show it in its right place when it downloads
     */
    public static class DownloadImageTask extends AsyncTask<String, Void, BitmapDrawable> {
        private ImageView bmImage;
        private Activity mContext; //Needed for accessing the private cache folder of the app
        private Toolbar mToolbar = null; //Needed to change the background color of the toolbar

        /**
         * This will be used normally
         * @param bmImage
         * @param mContext
         */
        public DownloadImageTask(ImageView bmImage, Activity mContext) {
            this.bmImage = bmImage;
            this.mContext = mContext;
        }


        public DownloadImageTask(ImageView bmImage, Activity mContext, Toolbar myToolbar) {
            this.bmImage = bmImage;
            this.mContext = mContext;
            this.mToolbar = myToolbar;
        }

        /**
         * This is going to asynchronously download the file and cache it under its URL filename
         * If the file is already cached we used that version instead of downloading it again.
         * @param urls
         * @return The cached version of BitmapDrawable from the url. First downloading it if
         * necessary.
         */
        protected BitmapDrawable doInBackground(String... urls) {
            String urldisplay = urls[0];
            Drawable myBitmapDrawable;
            Bitmap myBitMap = null;
            Bitmap myBitMapFromCache = null;
            InputStream in;
            String fileName = Uri.parse(urldisplay).getLastPathSegment(); //Url filename
            final int extractedColor;
            final int extractedDarkColor;

            //Let's try opening the file from the cache
            try {
                myBitMapFromCache = BitmapFactory.decodeStream(mContext.openFileInput(fileName));
                //Log.i("Caching Service", "File " + fileName + "was in cache, using it");
            } catch (FileNotFoundException fne) {
                //In case the file was not previously cached, we download it and cache it
                Log.i("Caching service", "File " + fileName + " was not cached yet, downloading...");
                try {
                    //Let's try opening the URL and convert it to bitmap file
                    in = new java.net.URL(urldisplay).openStream();
                    myBitMap = BitmapFactory.decodeStream(in);

                    //myBitmapDrawable = new BitmapDrawable(mContext.getResources(), in);
                } catch (Exception e) {
                    Log.e("Error decoding url stream for Bitmap", e.getMessage());
                    e.printStackTrace();
                }

                try {
                    //Now we are going to cache the file for later convenient usage
                    File outputDir = mContext.getCacheDir();
                    File outputFile = File.createTempFile(fileName, null, outputDir);
                } catch (IOException e) {
                    Log.e("Error impossible to create the cache file", e.getMessage());
                    e.printStackTrace();
                }

                //Now we are going to cache the Bitmap into the previously created File
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    myBitMap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    FileOutputStream fo = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    Log.i("Caching Service", "File has been properly downloaded and cached.");
                } catch (FileNotFoundException e) {
                    Log.e("Error fileName not found", e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Converting the cached Bitmap to BitmapDrawable in order to set it as background
                try {
                    myBitMapFromCache = BitmapFactory.decodeStream(mContext.openFileInput(fileName));
                    myBitmapDrawable = new BitmapDrawable(mContext.getResources(), myBitMapFromCache);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            finally{
                //Here the file should be available in cache
                myBitmapDrawable = new BitmapDrawable(mContext.getResources(), myBitMapFromCache);

                //If we were given a Toolbar as argument in the constructor
                if(mToolbar != null) {
                    //Let's also extract the color to colorate the toolbar
                    Palette generate = Palette.generate(myBitMapFromCache);
                    //Extracting color from picture
                    extractedColor = generate.getVibrantColor(R.color.primary);
                    extractedDarkColor = generate.getDarkVibrantColor(R.color.primary);
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToolbar.setBackgroundColor(extractedColor);
                            //Compat API 19
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Window window = mContext.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(extractedDarkColor);
                            }
                        }
                    });

                    //mToolbar.set
                }

                return (BitmapDrawable)myBitmapDrawable;
            }
        }

        protected void onPostExecute(BitmapDrawable result) {
            bmImage.setImageDrawable(result);
        }
    }

}
