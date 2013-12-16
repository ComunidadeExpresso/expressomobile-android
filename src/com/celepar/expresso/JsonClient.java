package com.celepar.expresso;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.util.Date;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import java.net.SocketTimeoutException;


public class JsonClient {
    private static JsonClient ourInstance = new JsonClient();
    private static Context context;
    private String url = "";
    private int timeout = 10000;
    private String auth;
    private JSONObject response;
    private Date lastCheckDate;
    
    public void setLastCheckDate(Date date){
        this.lastCheckDate = date;
    }
    
    public Date getLastCheckDate(){
        return this.lastCheckDate;
    }
    
    public void setURL(String url){
        this.url = url;
    }
    
    public String getURL(){
        return this.url;
    }
    
    public void setAuth(String auth){
        this.auth = auth;
    }
    
    public String getAuth(){
        return auth;
    }
    public static JsonClient getInstance() {        
        return ourInstance;
    }

    private void setContext(Context context){
        JsonClient.context = context;
    }

    private Context getContext(){
        return JsonClient.context;
    }

    private JsonClient() {

    }

    public JSONObject getResponse(){
        return response;
    }

    public void setResponse(JSONObject response){
        this.response = response;
    }
    
    public void call(Context context, String method, JSONObject params, int id) throws JSONException {
        JSONObject response = new JSONObject();
        //JSONObject request = new JSONObject();        
        
        setContext(context);
        //request.put("params", params.toString());
        //request.put("method", method);
        //request.put("format", "json-rpc");
        //request.put("id", id);
        
        //Log.v(org.json.JSONObject.quote(params.toString()),"JSON");
   

        if(isNetworkAvailable() != false){
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
            HttpConnectionParams.setSoTimeout(httpParams, timeout);
            HttpClient client = new DefaultHttpClient(httpParams);
    
            HttpPost httpPost = new HttpPost(url + method);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Accept", "application/json");
    
            try {
                //StringEntity entity = new StringEntity(request.toString());
                
                StringEntity entity = new StringEntity("id=" + id + "&params=" + params.toString());
                
                httpPost.setEntity(entity);
                ResponseHandler<String> handler = new BasicResponseHandler();
                response = new JSONObject(client.execute(httpPost, handler));

            } catch (SocketTimeoutException e) {
                response.put("error",context.getString(R.string.timeout_network));
                
            } catch (HttpResponseException e) {
                switch (e.getStatusCode()){                
                    case 503:
                        response.put("error",context.getString(R.string.unavailable_service));
                        break;
                    case 404:
                        response.put("error",context.getString(R.string.invalid_service));
                    case 403:
                        response.put("error",context.getString(R.string.forbidden_access_service));
                        break;
                    default:
                        response.put("error","HTTPResponseException:"+e.getStatusCode());
                        break;
                }
            }
            catch (Throwable e) {
                response.put("error", "Exception:"+e.getLocalizedMessage());
            }    
        }
        else {
            response.put("error", context.getString(R.string.unavailable_network));
        }
        setResponse(response);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }    
}