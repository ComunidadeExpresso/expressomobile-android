package org.apache.cordova;

/**
* A phonegap plugin that converts a Base64 String to a PNG file.
*
* @author mcaesar
* @lincese MIT.
*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;

import android.os.Environment;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public class Base64ToPNG extends CordovaPlugin {
	
	private CallbackContext callbackContext = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
//    public PluginResult execute(String action, JSONArray args, String callbackId) {
    	
    	this.callbackContext = callbackContext;

        if (!action.equals("saveImage")) {
        	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
        	return false;
        }

        try {

            String b64String = "";
            if (b64String.startsWith("data:image")) {
                b64String = args.getString(0).substring(21);
            } else {
                b64String = args.getString(0);
            }
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

            return this.saveImage(b64String, filename, folder, overwrite, callbackContext);

        } catch (JSONException e) {

            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage()));
            return false;

        } catch (InterruptedException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage()));
            return false;
        }

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
            int flags = Base64.NO_WRAP | Base64.URL_SAFE;
            
            byte[] decodedBytes = Base64.decode(b64String.getBytes(),flags);

            //Save Binary file to phone
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(decodedBytes);
            fOut.close();
            
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Saved successfully!"));
            
            return true;

//            return new PluginResult(PluginResult.Status.OK, "Saved successfully!");

        } catch (FileNotFoundException e) {
        	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "File not Found!"));
            return false;
        } catch (IOException e) {
        	callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
            return false;
        }
    }
}