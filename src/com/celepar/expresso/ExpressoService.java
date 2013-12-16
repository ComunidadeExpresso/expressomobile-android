package com.celepar.expresso;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import java.text.ParseException;

import com.celepar.expresso.R;

import java.util.ArrayList;
import org.json.JSONArray;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class ExpressoService extends BackgroundService {
	
	private final static String TAG = ExpressoService.class.getSimpleName();
	
	private String mAuth = "null";
	
	private String mApiURL = "http://api.expresso.pr.gov.br/";
	
	private String mUsername = "";
	
	private String mPassword = "";
	
	private Date lastCheckDate;
	
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();
		
//		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 
			
			this.messagesList(this);
			
			this.lastCheckDate = new Date(System.currentTimeMillis());
			
			//Log.d(TAG, msg);
//		} catch (JSONException e) {
//		}
		
		return result;	
	}
	
	public void showNotification( String contentTitle, String contentText , String routeURL) {
		int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, contentTitle, when);
		
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent notificationIntent = new Intent(this, ViewMessageActivity.class);
		notificationIntent.putExtra ("url",routeURL);
		
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
        
        NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, notification);
	}

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("auth", this.mAuth);
			result.put("apiURL", this.mApiURL);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {
//			if (config.has("apiURL"))
//				this.mApiURL = config.getString("apiURL");
			
			if (config.has("auth"))
				this.mAuth = config.getString("auth");
			
			if (config.has("username"))
				this.mUsername = config.getString("username");
			
			if (config.has("password"))
				this.mPassword = config.getString("password");
		} catch (JSONException e) {
		}
		
	}     

	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTimerEnabled() {
		// TODO Auto-generated method stub
		
		this.lastCheckDate = new Date(System.currentTimeMillis());
		
	}

	@Override
	protected void onTimerDisabled() {
		// TODO Auto-generated method stub
		
	}
	
	private ArrayList messagesList(final Context context){ 	
//        dialog = ProgressDialog.show(context,"", getString(R.string.waiting_for_server), true, true);
        
        final ArrayList messages = new ArrayList();

                try {
                	 JsonClient jsonClient = JsonClient.getInstance();
                	 JSONObject params = new JSONObject();
                	 jsonClient.setAuth(this.mAuth);
                	 jsonClient.setURL(this.mApiURL);
                	 jsonClient.setLastCheckDate(this.lastCheckDate);
                     if(jsonClient.getAuth() != null){
                         params.put("auth", jsonClient.getAuth());
                     }
                     params.put("folderID","INBOX");
                     JsonClient.getInstance().call(context, "Mail/Messages", params, 2);
                } catch (JSONException e) {
                }
                handler.post(
                    new Runnable(){
                        public void run() {
//                            dialog.dismiss();
                            JSONObject response = JsonClient.getInstance().getResponse();
                            if( response != null) {
                                try {
                                    String error = response.isNull("error") ? null : response.getString("error");
                                    if(error != null) {
                                    	System.out.println("ERRO!!!!");
                                    	System.out.println(error);
//                                        CustomToast.show(getApplicationContext(), error, CustomToast.ERROR);
                                    }

                                    JSONObject result = response.getJSONObject("result");
                                    //System.out.println("PASSOU AQUI!!!!");
                                    
                                   
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                    String now = formatter.format(JsonClient.getInstance().getLastCheckDate()); 
                                    System.out.println("LAST CHECK DATE!");
                                    System.out.println(now);
                                    //System.out.println(result);
                                    if(result != null) {
                                    	
                                    	JSONArray folderArray = result.getJSONArray("messages");
                                    	
                                    	for(int i=0;i<folderArray.length();i++){
                                    		
                                    		JSONObject message_object = folderArray.getJSONObject(i);

                                    		try {
                                    			
                                    			Date messageDate = formatter.parse(message_object.getString("msgDate"));
                                    			
                                    			String currentDate = formatter.format(messageDate); 
                                    			//System.out.println("DATA:" + currentDate);
                                    			
                                    			if (messageDate.compareTo((Date)JsonClient.getInstance().getLastCheckDate()) > 0) {
                                    				showNotification(message_object.getString("msgSubject"),message_object.getString("msgBodyResume"),"notify");
                                    			} 
                                    			
                                    		} catch (java.text.ParseException e) {
                                                e.printStackTrace();
                                            }

                                    	}
                                    
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
    			);   
        
        return messages;
        
    }


}
