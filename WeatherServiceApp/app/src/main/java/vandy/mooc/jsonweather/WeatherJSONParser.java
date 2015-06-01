package vandy.mooc.jsonweather;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
            this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
            throws IOException {

        List<JsonWeather> weathers = new ArrayList<JsonWeather>();
        try {
            JSONObject json = getJsonObject(inputStream);
            Log.d(TAG, "json: " + json.toString());

            JSONObject jWind = json.getJSONObject("wind");
            double speed = jWind.getDouble("speed");
            double deg = jWind.getDouble("deg");
            Wind wind = new Wind();
            wind.setDeg(deg);
            wind.setSpeed(speed);

            JSONObject jSys = json.getJSONObject("sys");
            String country = jSys.getString("country");
            Long sunrise = jSys.getLong("sunrise");
            Long sunset = jSys.getLong("sunset");
            double message = jSys.getDouble("message");
            Sys sys = new Sys();
            sys.setSunset(sunset);
            sys.setSunrise(sunrise);
            sys.setCountry(country);
            sys.setMessage(message);

            JSONObject jMain = json.getJSONObject("main");
            double temp = jMain.getDouble("temp");
            double temp_min = jMain.getDouble("temp_min");
            double temp_max = jMain.getDouble("temp_max");
            Long humidity = jMain.getLong("humidity");
            Long pressure = jMain.getLong("pressure");
            Main main = new Main();
            main.setTemp(temp);
            main.setHumidity(humidity);
            main.setTempMax(temp_max);
            main.setTempMin(temp_min);
            main.setPressure(pressure);

            JSONArray jArr = json.getJSONArray("weather");
            JSONObject JSONWeather = jArr.getJSONObject(0);
            Long id = JSONWeather.getLong("id");
            String description = JSONWeather.getString("description");
            String icon = JSONWeather.getString("icon");
            String mainW = JSONWeather.getString("main");
            Weather w = new Weather();
            w.setMain(mainW);
            w.setDescription(description);
            w.setIcon(icon);
            w.setId(id);

            String city = json.getString("name");

            JsonWeather weather = new JsonWeather();
            weather.setMain(main);
            weather.setSys(sys);
            weather.setWind(wind);
            weather.setName(city + ", " + sys.getCountry());


            weathers.add(weather);



        } catch (JSONException e){
            Log.d(TAG, "json parsing failed: " + e.getMessage());
        }


//        // Create a JsonReader for the inputStream.
//        try (JsonReader reader =
//                     new JsonReader(new InputStreamReader(inputStream,
//                             "UTF-8"))) {
//            Log.d(TAG, "Parsing the results returned as an array");
//
//            // Handle the array returned from the Acronym Service.
//            return parseJsonWeatherArray(reader);
//        }
        return weathers;
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonWeatherArray(JsonReader reader)
            throws IOException {

        reader.beginArray();
        try {
            // If the acronym wasn't expanded return null;
            if (reader.peek() == JsonToken.END_ARRAY)
                return null;
            List<JsonWeather> weathers = new ArrayList<JsonWeather>();
            if (reader.peek() == JsonToken.BEGIN_ARRAY) {

                weathers = parseWeatherLongFormArray(reader);
//                while (reader.hasNext())
//                    weathers.add(parseJsonWeather(reader));

            }
            return weathers;
        } finally {
            reader.endArray();
        }
    }

    public List<JsonWeather> parseWeatherLongFormArray(JsonReader reader)
            throws IOException {

        Log.d(TAG, "reading lfs elements");

        reader.beginArray();

        try {
            List<JsonWeather> weathers = new ArrayList<JsonWeather>();

            while (reader.hasNext())
                weathers.add(parseJsonWeather(reader));

            return weathers;
        } finally {
            reader.endArray();
        }
    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseJsonWeather(JsonReader reader)
            throws IOException {
        reader.beginObject();
        JsonWeather jsonWeather = new JsonWeather();
        try {
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case JsonWeather.wind_JSON:
                        jsonWeather.setWind(parseWind(reader));
                        Log.d(TAG, "reading wind");
                        break;
                    case JsonWeather.main_JSON:
                        jsonWeather.setMain(parseMain(reader));
                        Log.d(TAG, "reading main");
                        break;
                    case JsonWeather.sys_JSON:
                        jsonWeather.setSys(parseSys(reader));
                        Log.d(TAG, "reading sys");
                        break;
                    case JsonWeather.weather_JSON:
                        jsonWeather.setWeather(parseWeathers(reader));
                        Log.d(TAG, "reading weathers");
                        break;
                    default:
                        reader.skipValue();
                        Log.d(TAG, "ignoring " + name);
                        break;
                }
            }
        }finally {
            reader.endObject();
        }
        return jsonWeather;
    }

    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {

        reader.beginArray();

        try {
            List<Weather> weathers = new ArrayList<Weather>();

            while (reader.hasNext())
                weathers.add(parseWeather(reader));

            return weathers;
        } finally {
            reader.endArray();
        }
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        reader.beginObject();
        Weather weather = new Weather();
        try {
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case Weather.description_JSON:
                       weather.setDescription(reader.nextString());
                        Log.d(TAG, "reading description " + weather.getDescription());
                        break;
                    case Weather.id_JSON:
                        weather.setId(reader.nextLong());
                        Log.d(TAG, "reading id " + weather.getId());
                        break;
                    case Weather.icon_JSON:
                        weather.setIcon(reader.nextString());
                        Log.d(TAG, " reading icon " + weather.getIcon());
                        break;
                    case Weather.main_JSON:
                        weather.setMain(reader.nextString());
                        Log.d(TAG, "reading main " + weather.getMain());
                        break;
                    default:
                        reader.skipValue();
                        Log.d(TAG, "ignoring " + name);

                }
            }
        }finally {
            reader.endObject();
        }
        return weather;
    }

    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader)
            throws IOException {
        reader.beginObject();
        Main main = new Main();
        try {
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case Main.temp_JSON:
                        main.setTemp(reader.nextDouble());
                        Log.d(TAG, "reading temp " + main.getTemp());
                        break;
                    case Main.grndLevel_JSON:
                        main.setGrndLevel(reader.nextDouble());
                        Log.d(TAG, "reading grndLvl " + main.getGrndLevel());
                        break;
                    case Main.humidity_JSON:
                        main.setHumidity(reader.nextLong());
                        Log.d(TAG, "reading humidity " + main.getHumidity());
                        break;
                    case Main.pressure_JSON:
                        main.setPressure(reader.nextDouble());
                        Log.d(TAG, "reading pressure " + main.getPressure());
                        break;
                    case Main.seaLevel_JSON:
                        main.setSeaLevel(reader.nextDouble());
                        Log.d(TAG, "reading sealvl" + main.getSeaLevel());
                        break;
                    case Main.tempMin_JSON:
                        main.setTempMin(reader.nextDouble());
                        Log.d(TAG, "reading min temp " + main.getTempMin());
                        break;
                    case Main.tempMax_JSON:
                        main.setTempMax(reader.nextDouble());
                        Log.d(TAG, "reading max temp " + main.getTempMax());
                        break;
                    default:
                        reader.skipValue();
                        Log.d(TAG, "ignoring " + name);

                }
            }
        }finally {
            reader.endObject();
        }
        return main;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
        reader.beginObject();
        Wind wind = new Wind();
        try {
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case Wind.deg_JSON:
                        wind.setDeg(reader.nextDouble());
                        Log.d(TAG, "reading deg " + wind.getDeg());
                        break;
                    case Wind.speed_JSON:
                        wind.setSpeed(reader.nextDouble());
                        Log.d(TAG, "reading speed " + wind.getSpeed());
                        break;
                    default:
                        reader.skipValue();
                        Log.d(TAG, "ignoring " + name);

                }
            }
        }finally {
            reader.endObject();
        }
        return wind;
    }

    /**
     * Parse a Json stream and return a Sys Object.
     */
    public Sys parseSys(JsonReader reader) throws IOException{
        // TODO -- you fill in here.
        reader.beginObject();
        Sys jSys = new Sys();
        try {
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case Sys.country_JSON:
                        jSys.setCountry(reader.nextString());
                        Log.d(TAG, "reading country " + jSys.getCountry());
                        break;
                    case Sys.message_JSON:
                        jSys.setMessage(reader.nextDouble());
                        Log.d(TAG, "reading message " + jSys.getMessage());
                        break;
                    case Sys.sunrise_JSON:
                        jSys.setSunrise(reader.nextLong());
                        Log.d(TAG, "reading sunrise " + jSys.getSunrise());
                        break;
                    case Sys.sunset_JSON:
                        jSys.setSunset(reader.nextLong());
                        Log.d(TAG, "reading sunset " + jSys.getSunset());
                        break;
                    default:
                        reader.skipValue();
                        Log.d(TAG, "ignoring " + name);

                }
            }
        }finally {
            reader.endObject();
        }
        return jSys;
    }

    //Returns a json object from an input stream
    private JSONObject getJsonObject(InputStream inputStreamObject){


        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            //returns the json object
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if something went wrong, return null
        return null;
    }
}
