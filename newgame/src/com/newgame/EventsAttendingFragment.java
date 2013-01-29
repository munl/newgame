package com.newgame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class EventsAttendingFragment extends ListFragment {

	
	Vector<String> attendingNames = new Vector<String>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(container == null)return null;
		
		return (LinearLayout)inflater.inflate(R.layout.eventattending, container, false);
	}

	
	// Called on Fragment creation.
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        attendingNames.clear();        
        JSONObject obj;
        
        try {
			String jsondata = Utility.fb.request( EventInformation.eventID + "/attending" );
			JSONObject jsonobject = (JSONObject) new JSONTokener(jsondata).nextValue();
			JSONArray attending = (JSONArray) new JSONTokener(jsonobject.getString("data").toString()).nextValue();
			
			for(int i = 0; i< attending.length(); i++){
				obj = attending.getJSONObject(i);
				attendingNames.add(obj.optString("name"));
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			        android.R.layout.simple_list_item_1, attendingNames);
			    setListAdapter(adapter);
			    
			
						
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    } 
}
