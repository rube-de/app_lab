package vandy.mooc.aidl;

import vandy.mooc.aidl.WeatherData;
import java.util.List;

/**
 * Interface defining the method that receives callbacks from the
 * AcronymServiceAsync.
 */
interface WeatherResults {
    /**
     * This one-way (non-blocking) method allows AcyronymServiceAsync
     * to return the List of AcronymData results associated with a
     * one-way AcronymRequest.callAcronymRequest() call.
     */
    oneway void sendResults(in List<WeatherData> results);

    /**
     * This one-way (non-blocking) method allows AcyronymServiceAsync
     * to return an error String if the Service fails for some reason.
     */
    oneway void sendError(in String reason);
}
