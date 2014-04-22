package com.celepar.expresso;

import static com.udinic.accounts_authenticator_example.authentication.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.res.Configuration;
import android.webkit.CookieSyncManager;
import android.webkit.CookieManager;
import android.view.Menu;
import org.apache.cordova.*;
import android.net.Uri;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.udinic.accounts_authenticator_example.authentication.AccountGeneral;


public class MainActivity extends DroidGap {
	

	private int requestCode = 100;
	
	@Override
	public void init() {
	    super.init();
//	    this.appView.getSettings().setNavDump(false);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        super.init();
        this.registerForContextMenu(this.appView);
        
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        CookieManager.setAcceptFileSchemeCookies(false);
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieManager.setAcceptCookie(false);
        CookieSyncManager.getInstance().sync();
                
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
        
//        System.out.println("URL ROUTE: " + urlString);
//        System.out.println("AUTH: " + authString);
        
        super.loadUrl("file:///android_asset/www/index_android.html");
        super.loadUrl("javascript:var intent_auth='" + authString + "';");
        super.loadUrl("javascript:var intent_route='" + urlString + "';");
        
    }
    
    public void showNotification( String contentTitle, String contentText, String subText, String msgID) {

		
        String routeViewMessage = "/Mail/Messages/0/" + msgID + "/INBOX";
        String routeReplyMessage = "/Mail/Message/ReplyMessage/" + msgID + "/INBOX";
        String routeDeleteMessage = "/Mail/Message/DelMessage/" + msgID + "/INBOX";
        
        int requestCodeView = Integer.parseInt(msgID + "0001");
        int requestCodeReply = Integer.parseInt(msgID + "0002");
        int requestCodeDel = Integer.parseInt(msgID + "0003");
        
        Intent notificationIntent = new Intent(this,MainActivity.class);
        notificationIntent.setAction("com.celepar.expresso.NOTIFICATION");
        notificationIntent.putExtra("ROUTE",routeViewMessage);
        PendingIntent contentIntent = PendingIntent.getActivity(this,requestCodeView,notificationIntent, 0);
        
        Intent notificationReplyIntent = new Intent(this,MainActivity.class);
        notificationReplyIntent.setAction("com.celepar.expresso.NOTIFICATION");
        notificationReplyIntent.putExtra("ROUTE",routeReplyMessage);
        PendingIntent contentReplyIntent = PendingIntent.getActivity(this, requestCodeReply,notificationReplyIntent, 0);
        
        
        Intent notificationDeleteIntent = new Intent(this,MainActivity.class);
        notificationDeleteIntent.setAction("com.celepar.expresso.NOTIFICATION");
        notificationDeleteIntent.putExtra("ROUTE",routeDeleteMessage);
        PendingIntent contentDeleteIntent = PendingIntent.getActivity(this,requestCodeDel,notificationDeleteIntent, 0);
        
        Notification notif = new Notification.Builder(this)
        .setContentTitle(contentTitle)
        .setContentText(contentText).setSmallIcon(R.drawable.ic_launcher)
        .setContentIntent(contentIntent)
        .setSubText(subText)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_ler_mensagem, "Visualizar", contentIntent)
        .addAction(R.drawable.ic_responder_mensagem, "Responder", contentReplyIntent)
        .addAction(R.drawable.ic_excluir_mensagem, "Apagar", contentDeleteIntent).build();
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(msgID), notif);
        
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
