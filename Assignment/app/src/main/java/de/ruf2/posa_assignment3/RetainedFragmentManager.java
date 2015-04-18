package de.ruf2.posa_assignment3;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Bernhard Ruf on 16.04.2015.
 */
public class RetainedFragmentManager {
    /**
     * debugging tag used by the android logger
     */
    protected  final String TAG = getClass().getSimpleName();

    /**
     * name used to identify the RetainedFragment
     */
    private final String mRetainedFragmentTag;

    /**
     * Reference to the FragmentManager
     */
    private final FragmentManager mFragmentManager;

    /**
     * Reference to the RetainedFragment
     */
    private RetainedFragment mRetainedFragment;

    /**
     * constructor to initializes fields
     * @param activity
     * @param retainedFragmentTag
     */
    public RetainedFragmentManager(Activity activity, String retainedFragmentTag){
        mFragmentManager = activity.getFragmentManager();
        mRetainedFragmentTag = retainedFragmentTag;
    }

    /**
     * initializes the RetainedFragment the first time it's called
     *
     * @return true if this wast the first time it's called, else false
     */
    public boolean firstTimeIn(){
        mRetainedFragment = (RetainedFragment) mFragmentManager.findFragmentByTag(mRetainedFragmentTag);

        if (mRetainedFragment != null){
            return false;
        } else {
            mRetainedFragment = new RetainedFragment();
            mFragmentManager.beginTransaction().add(mRetainedFragment,mRetainedFragmentTag).commit();
            return true;
        }
    }

    /**
     * add the @a object with the @a key
     */
    public void put(String key, Object object){
        mRetainedFragment.put(key, object);
    }

    /**
     * add the @a object with its class name
     */
    public void put(Object object){
        mRetainedFragment.put(object.getClass().getName(), object);
    }

    /**
     * get the object with @a key
     */
    public <T> T get(String key){
        return (T) mRetainedFragment.get(key);
    }


    /**
     * Retain state information between configuration changes
     */
    public class RetainedFragment extends Fragment{
        /**
         * Maps keys to objects
         */
        private HashMap<String, Object> mData = new HashMap<>();

        /**
         * Hook method called when a new instance of Fragment is created
         * @param savedInstanceState
         */
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            Log.d(TAG, "onCreate called to setRetainInstace to true");

            //Ensure the data survices runtime cnofiruation changes
            setRetainInstance(true);
        }

        /**
         * add the @a object with the @a key
         */
        public void put(String key, Object object){
            mData.put(key, object);
        }

        /**
         * add the @a object with its class name
         */
        public void put(Object object){
            put(object.getClass().getName(), object);
        }

        /**
         * get the object with @a key
         */
        public <T> T get(String key){
            return (T) mData.get(key);
        }
    }

}
