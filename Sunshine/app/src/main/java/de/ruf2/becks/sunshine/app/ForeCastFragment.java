package de.ruf2.becks.sunshine.app;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by becks on 25.10.2014.
 */
public class ForeCastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

    public ForeCastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        setHasOptionsMenu(true);

        List<String> fakeData = new ArrayList<String>();
        fakeData.add("Today - Sunny - 25/15");
        fakeData.add("Tomorrow - Cloudy - 21/16");
        fakeData.add("Friday - Sunny - 22/15");
        fakeData.add("Saturday - Cloudy 18/14");
        fakeData.add("Sunday - Rainy - 17/12");
        fakeData.add("Monday - Sunny - 23/15");

        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_texview,
                fakeData);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);


        return rootView;
    }


    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        private String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            if(params.length == 0){
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            //retrun stringarray
            String[] foreCast = null;

            //params
            String zipcode = params[0];
            String mode = "json";
            String units = "metric";
            int days = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
//                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                final String QUERY_PARAM = "q";
                final String FORM_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q", zipcode)
                        .appendQueryParameter("mode", mode)
                        .appendQueryParameter("units", units)
                        .appendQueryParameter("cnt", Integer.toString(days));

                URL url = new URL(builder.build().toString());


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return  null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return  getWeatherDataFromJson(forecastJsonStr, days);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error", e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if(strings != null) {
                mForecastAdapter.clear();
                mForecastAdapter.addAll(strings);
            }
        }

        /* The date/time conversion code is going to be moved outside the asynctask later,
        * so for convenience we're breaking it out into its own method now.
        */
       private String getReadableDateString(long time){
           // Because the API returns a unix timestamp (measured in seconds),
           // it must be converted to milliseconds in order to be converted to valid date.
           Date date = new Date(time * 1000);
           SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
           return format.format(date).toString();
       }

       /**
        * Prepare the weather high/lows for presentation.
        */
       private String formatHighLows(double high, double low) {
           // For presentation, assume the user doesn't care about tenths of a degree.
           long roundedHigh = Math.round(high);
           long roundedLow = Math.round(low);

           String highLowStr = roundedHigh + "/" + roundedLow;
           return highLowStr;
       }

       /**
        * Take the String representing the complete forecast in JSON Format and
        * pull out the data we need to construct the Strings needed for the wireframes.
        *
        * Fortunately parsing is easy:  constructor takes the JSON string and converts it
        * into an Object hierarchy for us.
        */
       private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
               throws JSONException {

           // These are the names of the JSON objects that need to be extracted.
           final String OWM_LIST = "list";
           final String OWM_WEATHER = "weather";
           final String OWM_TEMPERATURE = "temp";
           final String OWM_MAX = "max";
           final String OWM_MIN = "min";
           final String OWM_DATETIME = "dt";
           final String OWM_DESCRIPTION = "main";

           JSONObject forecastJson = new JSONObject(forecastJsonStr);
           JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

           String[] resultStrs = new String[numDays];
           for(int i = 0; i < weatherArray.length(); i++) {
               // For now, using the format "Day, description, hi/low"
               String day;
               String description;
               String highAndLow;

               // Get the JSON object representing the day
               JSONObject dayForecast = weatherArray.getJSONObject(i);

               // The date/time is returned as a long.  We need to convert that
               // into something human-readable, since most people won't read "1400356800" as
               // "this saturday".
               long dateTime = dayForecast.getLong(OWM_DATETIME);
               day = getReadableDateString(dateTime);

               // description is in a child array called "weather", which is 1 element long.
               JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
               description = weatherObject.getString(OWM_DESCRIPTION);

               // Temperatures are in a child object called "temp".  Try not to name variables
               // "temp" when working with temperature.  It confuses everybody.
               JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
               double high = temperatureObject.getDouble(OWM_MAX);
               double low = temperatureObject.getDouble(OWM_MIN);

               highAndLow = formatHighLows(high, low);
               resultStrs[i] = day + " - " + description + " - " + highAndLow;
           }
           return resultStrs;
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
       // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                FetchWeatherTask weatherTask = new FetchWeatherTask();
                weatherTask.execute("94043");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}


