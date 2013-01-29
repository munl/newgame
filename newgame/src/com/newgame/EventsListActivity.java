package com.newgame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.facebook.android.FacebookError;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EventsListActivity extends ListActivity {

	Vector<String> eventVector = new Vector<String>();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	Calendar today = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist);
		
		JSONObject obj;
		
		//String[] eventArray = new String[3];
		
		try {
			
			String jsondata = Utility.fb.request( "me/events");
			JSONObject jsonobject = (JSONObject) new JSONTokener(jsondata).nextValue();
			
			final JSONArray events = (JSONArray) new JSONTokener(jsonobject.getString("data").toString()).nextValue();
			for(int i = 0; i < events.length(); i++){
				
				obj = events.getJSONObject(i);
				Date startTime = sdf.parse(obj.optString("start_time"));
				if(startTime.compareTo(today.getTime())>0){
					eventVector.add(obj.optString("name"));
				}
						
			}
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,eventVector));
			ListView listView = getListView();
			listView.setTextFilterEnabled(true);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				JSONObject eventobject;
				

				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					//Log.v("hello", ((TextView)view.findViewById(android.R.id.text1)).getText().toString());
					for(int i = 0; i < events.length(); i++){
						try {
							eventobject = events.getJSONObject(i);
							if(eventobject.optString("name") == ((TextView)view.findViewById(android.R.id.text1)).getText().toString()){
								EventInformation.eventID = eventobject.optString("id");
								EventInformation.eventName = eventobject.optString("name");
								EventInformation.startTime = eventobject.optString("start_time");
								EventInformation.endTime = eventobject.optString("end_time");
								EventInformation.description = eventobject.optString("description");
								Intent myIntent = new Intent(EventsListActivity.this, EventDetailsActivity.class);
								EventsListActivity.this.startActivity(myIntent);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		eventVector.clear();
		super.onDestroy();
	}

}
