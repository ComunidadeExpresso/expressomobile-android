package com.celepar.expresso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public class MessagesListActivity extends Activity {
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.messages);
//        
//        ListView listView = (ListView)findViewById(R.id.listMessages);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView name = (TextView) view.findViewById(R.id.txt_menuItem);
//                /*if(name.getText().toString() == getString(R.string.disconnect)){
//                	logout(FoldersListActivity.this);
//                }
//                else */
//                CustomToast.show(getApplicationContext(), name.getText().toString(), CustomToast.SUCCESS, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//
//            }
//        });
//        
//        ArrayList<CustomMenuItem> folders = messagesList(this);
//        
//        ListAdapter adapter = new MenuItemAdapter(this, folders);
//        listView.setAdapter(adapter);
//        CustomToast.show(this, getString(R.string.login_success), CustomToast.SUCCESS, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//    }

//    private ArrayList<CustomMenuItem> getMainApps(){
//        ArrayList<CustomMenuItem> mainApps = new ArrayList<CustomMenuItem>();
//
//
//        mainApps.add(new CustomMenuItem(getString(R.string.all_folders),R.drawable.ic_folder));
//
//        return mainApps;
//    }

    
    private ArrayList messagesList(final Context context){ 	
        dialog = ProgressDialog.show(context,"", getString(R.string.waiting_for_server), true, true);
        
        final ArrayList messages = new ArrayList();

                try {
                	 JsonClient jsonClient = JsonClient.getInstance();
                	 JSONObject params = new JSONObject();
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
                            dialog.dismiss();
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
                                    System.out.println("PASSOU AQUI!!!!");
                                    System.out.println(result);
                                    if(result != null) {
                                    	
                                    	JSONArray folderArray = result.getJSONArray("messages");
                                    	
                                    	for(int i=0;i<folderArray.length();i++){
                                    		
                                    		JSONObject folder_object = folderArray.getJSONObject(i);
//                                    		messages.add(new CustomMenuItem(folder_object.getString("msgSubject"),R.drawable.ic_email));
                                    		
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