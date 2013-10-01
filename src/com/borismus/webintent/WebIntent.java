package com.borismus.webintent;

import com.celepar.expresso.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.cordova.DroidGap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.text.Html;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;

/**
 * WebIntent is a PhoneGap plugin that bridges Android intents and web
 * applications:
 * 
 * 1. web apps can spawn intents that call native Android applications. 2.
 * (after setting up correct intent filters for PhoneGap applications), Android
 * intents can be handled by PhoneGap web applications.
 * 
 * @author boris@borismus.com
 * 
 */
public class WebIntent extends CordovaPlugin {

	//private String onNewIntentCallback = null;
	private CallbackContext callbackContext = null;

	//public boolean execute(String action, JSONArray args, String callbackId) {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
	    try {
	        this.callbackContext = callbackContext;

	        if (action.equals("startActivity")) {
	            if (args.length() != 1) {
	                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	                return false;
	            }

	            // Parse the arguments
	            JSONObject obj = args.getJSONObject(0);
	            String type = obj.has("type") ? obj.getString("type") : null;
	            Uri uri = obj.has("url") ? Uri.parse(obj.getString("url")) : null;
	            JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
	            Map<String, String> extrasMap = new HashMap<String, String>();

	            // Populate the extras if any exist
	            if (extras != null) {
	                JSONArray extraNames = extras.names();
	                for (int i = 0; i < extraNames.length(); i++) {
	                    String key = extraNames.getString(i);
	                    String value = extras.getString(key);
	                    extrasMap.put(key, value);
	                }
	            }

	            startActivity(obj.getString("action"), uri, type, extrasMap);
	            //return new PluginResult(PluginResult.Status.OK);
	            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
	            return true;
	        } else if (action.equals("saveImage")) {
	        	
	        	try {

	                String b64String = "";
//	                if (b64String.startsWith("data:image")) {
//	                    b64String = args.getString(0).substring(21);
//	                } else {
	                    b64String = args.getString(0);
//	                }
	                JSONObject params = args.getJSONObject(1);

	                //Optional parameter
	                String filename = params.has("filename")
	                        ? params.getString("filename")
	                        : "b64Image_" + System.currentTimeMillis() + ".png";

	                String folder = params.has("folder")
	                        ? params.getString("folder")
	                        : Environment.getExternalStorageDirectory() + "/Pictures";

	                Boolean overwrite = params.has("overwrite") 
	                        ? params.getBoolean("overwrite") 
	                        : false;

	                this.saveImage(b64String, filename, folder, overwrite, callbackContext);

	            } catch (JSONException e) {

	                e.printStackTrace();
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage()));
	                return false;

	            } catch (InterruptedException e) {
	                e.printStackTrace();
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage()));
	                return false;
	            }
	            
	            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
	            return true;
	        	
	        } else if (action.equals("hasExtra")) {
	            if (args.length() != 1) {
	                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	                return false;
	            }
	            Intent i = ((DroidGap)this.cordova.getActivity()).getIntent();
	            String extraName = args.getString(0);
	            //return new PluginResult(PluginResult.Status.OK, i.hasExtra(extraName));
	            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, i.hasExtra(extraName)));
	            return true;
	            
	        } else if (action.equals("clearCookies")) {
	        	
	        	Log.v("ClearCookies","Clear Cookies");
	        	
	        	MainActivity act = (MainActivity)this.cordova.getActivity();
	        	act.clearCookies();
	        	
//	        	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, ""));
	        	
	        } else if (action.equals("getExtra")) {
	            if (args.length() != 1) {
	                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	                return false;
	            }
	            Intent i = ((DroidGap)this.cordova.getActivity()).getIntent();
	            
	            Log.v("Intent", i.getAction());
	            
	            String extraName = args.getString(0);
	            
	            StringBuilder str = new StringBuilder();
	            Bundle bundle = i.getExtras();
	            if (bundle != null) {
	                Set<String> keys = bundle.keySet();
	                Iterator<String> it = keys.iterator();
	                while (it.hasNext()) {
	                    String key = it.next();
	                    
	                    if (key.equals(extraName)) {
	                    	str.append(bundle.get(key));
	                    	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, str.toString()));
	    	                return true;
	                    }
//	                    str.append(key);
//	                    str.append(":");
//	                    str.append(bundle.get(key));
//	                    str.append("\n\r");
	                }
	                //Log.v("Intent", str.toString());
	            }
	            	            
	            if ( i.hasExtra(extraName)) {
	            	
	            	
	                
	            } else {
	                //return new PluginResult(PluginResult.Status.ERROR);
	             //   callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
	              //  return false;
	            }
	        } else if (action.equals("getUri")) {
	            if (args.length() != 0) {
	                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	                return false;
	            }

	            Intent i = ((DroidGap)this.cordova.getActivity()).getIntent();
	            String uri = i.getDataString();
	            //return new PluginResult(PluginResult.Status.OK, uri);
	            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, uri));
	            return true;
	        } else if (action.equals("onNewIntent")) {
	            if (args.length() != 0) {
	                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	                return false;
	            }

	            //this.onNewIntentCallback = callbackId;
	            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
	            result.setKeepCallback(true);
	            callbackContext.sendPluginResult(result);
	            return true;
	            //return result;
	        } else if (action.equals("sendBroadcast")) 
	        {
	            if (args.length() != 1) {
	                //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	                return false;
	            }

	            // Parse the arguments
	            JSONObject obj = args.getJSONObject(0);

	            JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
	            Map<String, String> extrasMap = new HashMap<String, String>();

	            // Populate the extras if any exist
	            if (extras != null) {
	                JSONArray extraNames = extras.names();
	                for (int i = 0; i < extraNames.length(); i++) {
	                    String key = extraNames.getString(i);
	                    String value = extras.getString(key);
	                    extrasMap.put(key, value);
	                }
	            }

	            sendBroadcast(obj.getString("action"), extrasMap);
	            //return new PluginResult(PluginResult.Status.OK);
	            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
	            return true;
	        }
	        //return new PluginResult(PluginResult.Status.INVALID_ACTION);
	        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
	        return false;
	    } catch (JSONException e) {
	        e.printStackTrace();
	        //return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
	        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
	        return false;
	    }
	}

	@Override
	public void onNewIntent(Intent intent) {
	    if (this.callbackContext != null) {
	        this.callbackContext.success(intent.getDataString());
	    }
	}

    void startActivity(String action, Uri uri, String type, Map<String, String> extras) {
        Intent i = (uri != null ? new Intent(action, uri) : new Intent(action));
        
        if (type != null && uri != null) {
            i.setDataAndType(uri, type); //Fix the crash problem with android 2.3.6
        } else {
            if (type != null) {
                i.setType(type);
            }
        }
        
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            // If type is text html, the extra text must sent as HTML
            if (key.equals(Intent.EXTRA_TEXT) && type.equals("text/html")) {
                i.putExtra(key, Html.fromHtml(value));
            } else if (key.equals(Intent.EXTRA_STREAM)) {
                // allowes sharing of images as attachments.
                // value in this case should be a URI of a file
                i.putExtra(key, Uri.parse(value));
            } else if (key.equals(Intent.EXTRA_EMAIL)) {
                // allows to add the email address of the receiver
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { value });
            } else {
                i.putExtra(key, value);
            }
        }
        ((DroidGap)this.cordova.getActivity()).startActivity(i);
    }

    void sendBroadcast(String action, Map<String, String> extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            intent.putExtra(key, value);
        }

        ((DroidGap)this.cordova.getActivity()).sendBroadcast(intent);
    }
    
    private boolean saveImage(String b64String, String fileName, String dirName, Boolean overwrite, CallbackContext callbackContext) throws InterruptedException, JSONException {

        try {

            //Directory and File
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dirName, fileName);
            
            

            //Avoid overwriting a file
            if (!overwrite && file.exists()) {
                return false;
            }

            //Decode Base64 back to Binary format
            int flags = Base64.DEFAULT;
            
            byte[] decodedBytes = Base64.decode(b64String.getBytes(),flags);

            //Save Binary file to phone
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(decodedBytes);
            fOut.close();
            
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, dirName.concat("/").concat(fileName)));
            
            return true;

        } catch (FileNotFoundException e) {
        	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "File not Found!"));
            return false;
        } catch (IOException e) {
        	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
            return false;
        }
    }
}