package com.yousuf.shawon.ribbit.ribbit;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by user on 1/19/2016.
 */
public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // [Optional] Power your app with Local Datastore. For more info, go to
       // https://parse.com/docs/android/guide#local-datastore
      //  Parse.enableLocalDatastore(this);
        Parse.initialize(this);

//
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
//

    }
}
