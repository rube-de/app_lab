package vandy.mooc.aidl;

import vandy.mooc.aidl.WeatherData;
import java.util.List;

/**
 * Interface defining the method that the AcronymServiceSync will
 * implement to provide synchronous access to the Acronym Web service.
 */
interface WeatherCall {
   /**
    * A two-way (blocking) call to the AcronymServiceSync that
    * retrieves information about an acronym from the Acronym Web
    * service and returns a list of AcronymData containing the results
    * from the Web service back to the AcronymActivity.
    */
    List<WeatherData> getCurrentWeather(in String Weather);
}
