package com.celepar.expresso;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.webkit.CookieSyncManager;
import android.webkit.CookieManager;
import android.view.Menu;
import org.apache.cordova.*;


public class MainActivity extends DroidGap {
	
//	@Override
//	public void init() {
//	    super.init();       
//	    this.appView.getSettings().setNavDump(false);
//	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        CookieManager.setAcceptFileSchemeCookies(false);
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieManager.setAcceptCookie(false);
        CookieSyncManager.getInstance().sync();
        
        
        
//        String cookieString = sessionCookie.getName() + "=" + sessionCookie.getValue() + "; domain=" + sessionCookie.getDomain();
//        cookieManager.setCookie(myapp.domain, cookieString);
        
        // 
        
        super.loadUrl("file:///android_asset/www/index_android.html");
//        CookieManager.setAcceptFileSchemeCookies(false);
    }
    
    public void clearCookies() {
    	
    	CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        CookieManager.setAcceptFileSchemeCookies(false);
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieManager.setAcceptCookie(false);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override 
    public void onConfigurationChanged(Configuration newConfig) { 
    	super.onConfigurationChanged(newConfig); 
    }
    
}
