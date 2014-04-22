package com.celepar.expresso;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;

import org.apache.cordova.*;

public class ComposeMessageActivity extends DroidGap {
	
	final static String EXTRA_URL = "URL";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
        
        Uri data = getIntent().getData();
        
//        System.out.println("DATA:");
//        System.out.println(data);
        
        String urlString;
        String authString;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            urlString= null;
            authString = null;
        } else {
        	urlString= extras.getString("URL");
        	authString= extras.getString("AUTH");
        }

//        
        System.out.println("URL: " + urlString);
        System.out.println("AUTH: " + authString);
//
//        
//        System.out.println("ACTION: " + action);
//        System.out.println("TYPE: " + type);
        
        super.loadUrl("file:///android_asset/www/index_android.html");
        super.loadUrl("javascript:var intent_auth='" + authString + "';");
        super.loadUrl("javascript:var intent_route='" + urlString + "';");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
        
	
}
