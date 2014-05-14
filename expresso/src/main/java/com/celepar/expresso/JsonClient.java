package com.celepar.expresso;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.security.KeyStore;
import java.util.Date;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.net.SocketTimeoutException;

import com.celepar.expresso.MySSLSocketFactory;


public class JsonClient {
    private static JsonClient ourInstance = new JsonClient();
    private static Context context;
    private String url = "";
    private int timeout = 10000;
    private String auth;
    private JSONObject response;
    private Date lastCheckDate;
    private boolean canMakeRequest = true;
    
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
    
    public void setCanMakeRequest(boolean value){
        this.canMakeRequest = value;
    }
    
    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
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
        
        if (this.canMakeRequest == true) {

	        if(isNetworkAvailable() != false){
	            HttpParams httpParams = new BasicHttpParams();
	            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	            HttpConnectionParams.setSoTimeout(httpParams, timeout);
	            
	//            HttpClient client = new DefaultHttpClient(httpParams);
	            HttpClient client = this.getNewHttpClient();
	    
	            HttpPost httpPost = new HttpPost(url + method);
	            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
	            httpPost.setHeader("Accept", "application/json");
	    
	            try {
	            	
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
        
        } else {
        	System.out.println("CAN'T MAKE REQUESTS");
        }
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