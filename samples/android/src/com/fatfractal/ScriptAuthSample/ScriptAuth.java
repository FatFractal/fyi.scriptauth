package com.fatfractal.ScriptAuthSample;

import android.app.Application;
import android.content.Context;
import com.fatfractal.ffef.FatFractal;
import com.fatfractal.ffef.impl.FFPrefsAndroid;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 2/17/13
 * Time: 10:23 PM
 */
public class ScriptAuth extends Application
{
    public static FatFractal ff = null;

    @Override
    public void onCreate() {
        super.onCreate();

        getFF();

        Context context = this.getApplicationContext();
        FFPrefsAndroid.setContext(context);
    }

    public static FatFractal getFF() {
        if (ff == null) {
            String baseUrl = "http://fyi.fatfractal.com/scriptauth";
            String sslUrl = "https://fyi.fatfractal.com/scriptauth";

            try {
                ff = FatFractal.getInstance(new URI(baseUrl), new URI(sslUrl));
                //ff.setDebug(true);

                FatFractal.setMain(ff);
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return ff;
    }
}
