/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.tonight.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class EventContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.tonight";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_EVENT = "events";
    public static final String PATH_SPECIFIC_EVENT = "event";
    public static final String PATH_EVLOCK = "evloc";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "event";

        //Principal columns about the events
        public static final String COLUMN_EVENT_ID = "id";
        public static final String COLUMN_EVENT_NAME = "name";
        public static final String COLUMN_EVENT_START_DATE = "start_date";
        public static final String COLUMN_EVENT_END_DATE = "end_date";
        public static final String COLUMN_EVENT_MAX_PEOPLE = "max_people";
        public static final String COLUMN_EVENT_PRICE = "price";
        //This is the ID of the foreign id key from `event_location`
        public static final String COLUMN_EVENT_EVENT_LOCATION_ID = "event_location_id";
        public static final String COLUMN_EVENT_PICTURE = "picture";
        public static final String COLUMN_EVENT_DESCRIPTION = "description";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPECIFIC_EVENT;

        public static Uri buildEventUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the weather table */
    public static final class EventLocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "event_location";

        // Columns from `event_location`
        public static final String COLUMN_EVLOC_KEY = "id";
        public static final String COLUMN_EVLOC_NAME = "name";
        public static final String COLUMN_EVLOC_LAT = "latitude";
        public static final String COLUMN_EVLOC_LONG = "longitude";
        public static final String COLUMN_EVLOC_ADDR = "address";
        //This is the foreign id key from `cities` which identifies the unique city
        //in which the the event is taking place
        public static final String COLUMN_EVLOC_CITIES_ID = "cities_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVLOCK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVLOCK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVLOCK;


        public static Uri buildEventLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildEventLocation(String idEvLoc) {
            return CONTENT_URI.buildUpon().appendPath(idEvLoc).build();
        }

        public static String getEventIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
