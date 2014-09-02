package com.fatfractal.ScriptAuthSample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 2/17/13
 * Time: 10:42 PM
 */
public class WebViewActivity extends Activity
{
    public static String CLASS_NAME = WebViewActivity.class.getName();

    public static String SCRIPT_AUTH_SERVICE_EXTRAS_TAG = "scriptAuthService";
    public static String AUTH_URI_EXTRAS_TAG = "authUri";
    public static String CALLBACK_URI_EXTRAS_TAG = "callbackUri";
    public static String CALLBACK_URI_WITH_CODE_EXTRAS_TAG = "callbackUriWithCode";
    public static int CALLBACK_URI_WITH_CODE_REQUEST_CODE = 1;

    private WebView m_webView;

    private String m_scriptAuthService;
    private String m_authUri;
    private String m_callbackUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_webView = new WebView(this);
        m_webView.getSettings().setJavaScriptEnabled(true);
        m_webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(CLASS_NAME, "Trying to load URL " + url + " callback URL " + m_callbackUri);
                if (url.startsWith(m_callbackUri)) {
                    Log.i(CLASS_NAME, "We're trying to finish");
                    Intent intent = getIntent();
                    intent.putExtra(CALLBACK_URI_WITH_CODE_EXTRAS_TAG, url);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        setContentView(m_webView);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    public static Intent getWebViewActivityIntent(Context context, String scriptAuthService, String authUri, String callbackUri) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(SCRIPT_AUTH_SERVICE_EXTRAS_TAG, scriptAuthService);
        intent.putExtra(AUTH_URI_EXTRAS_TAG, authUri);
        intent.putExtra(CALLBACK_URI_EXTRAS_TAG, callbackUri);
        return intent;
    }

    public static String getCallbackUriWithCodeFromIntent(Intent intent) {
        return intent.getStringExtra(CALLBACK_URI_WITH_CODE_EXTRAS_TAG);
    }

    public static String getScriptAuthServiceFromIntent(Intent intent) {
        return intent.getStringExtra(SCRIPT_AUTH_SERVICE_EXTRAS_TAG);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        m_scriptAuthService = intent.getStringExtra(SCRIPT_AUTH_SERVICE_EXTRAS_TAG);
        m_authUri = intent.getStringExtra(AUTH_URI_EXTRAS_TAG);
        m_callbackUri = intent.getStringExtra(CALLBACK_URI_EXTRAS_TAG);

        Log.i(CLASS_NAME, "Loading URL " + m_authUri);
        m_webView.loadUrl(m_authUri);
    }
}