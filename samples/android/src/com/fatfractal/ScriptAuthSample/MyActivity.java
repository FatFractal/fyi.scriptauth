package com.fatfractal.ScriptAuthSample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.fatfractal.ffef.FFUser;
import com.fatfractal.ffef.FatFractal;

import java.net.URI;

public class MyActivity extends Activity
{
    public static String CLASS_NAME = MyActivity.class.getName();

    private Button facebookButton, twitterButton, logOutButton;
    private View loggedOutView, loggedInView;
    private TextView userNameView, serviceView, errorView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        facebookButton = (Button)findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                loginWithScriptAuthService(FatFractal.SCRIPT_AUTH_SERVICE_FACEBOOK);
            }
        });

        twitterButton = (Button)findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                loginWithScriptAuthService(FatFractal.SCRIPT_AUTH_SERVICE_TWITTER);
            }
        });

        userNameView = (TextView)findViewById(R.id.userName);
        serviceView = (TextView)findViewById(R.id.service);
        logOutButton = (Button)findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        loggedOutView = findViewById(R.id.loggedOut);
        loggedInView = findViewById(R.id.loggedIn);
        errorView = (TextView)findViewById(R.id.error);
    }

    public void loginWithScriptAuthService(String scriptAuthService) {
        try {
            FatFractal ff = FatFractal.getMain();
            String callbackUri = "x-" + scriptAuthService.toLowerCase() + "-ff://authorize";
            ff.setCallbackUriForScriptAuthService(scriptAuthService, new URI(callbackUri));
            String authUri = ff.authUriForScriptAuthService(scriptAuthService).toString();
            Intent intent = WebViewActivity.getWebViewActivityIntent(this, scriptAuthService, authUri, callbackUri);
            startActivityForResult(intent, WebViewActivity.CALLBACK_URI_WITH_CODE_REQUEST_CODE);
        }
        catch (Exception e) {
            e.printStackTrace();
            showError(e.getLocalizedMessage());
        }
    }

    public void logout() {
        try {
            FatFractal.getMain().logout();
            loggedOutView.setVisibility(View.VISIBLE);
            loggedInView.setVisibility(View.GONE);
            showError(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            showError(e.getLocalizedMessage());
        }
    }

    public void showError(String err) {
        if (err != null && err.length() > 0) {
            errorView.setText(err);
            errorView.setVisibility(View.VISIBLE);
        } else {
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WebViewActivity.CALLBACK_URI_WITH_CODE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String scriptAuthService = WebViewActivity.getScriptAuthServiceFromIntent(data);
                String callbackUriWithCode = WebViewActivity.getCallbackUriWithCodeFromIntent(data);

                try {
                    FatFractal ff = FatFractal.getMain();
                    ff.retrieveAccessTokenForScriptAuthService(scriptAuthService, new URI(callbackUriWithCode));
                    FFUser user = ff.loginWithScriptAuthService(scriptAuthService);
                    if (user != null) {
                        Log.i(CLASS_NAME, "SUCCESS! user = " + user);
                        userNameView.setText(user.getUserName());
                        serviceView.setText(scriptAuthService);
                        loggedInView.setVisibility(View.VISIBLE);
                        loggedOutView.setVisibility(View.GONE);
                        showError(null);
                    } else {
                        Log.e(CLASS_NAME, "FAILURE");
                        showError("Login failed");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    showError(e.getLocalizedMessage());
                }
            } else {
                String msg = "Something went wrong, bad result code received: " + resultCode;
                Log.e(CLASS_NAME, msg);
                showError(msg);
            }
        }
    }
}
