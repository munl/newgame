package com.newgame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventsGeneralFragment extends Fragment {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	ImageView pic;
	TextView eventName, startTime, description;
	Button setNotif;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if(container == null)return null;
		
		
		return (LinearLayout)inflater.inflate(R.layout.eventgeninfo, container, false);
	}
	
	// Called on Fragment creation.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        pic = (ImageView)getActivity().findViewById(R.id.eventpic);
        eventName = (TextView)getActivity().findViewById(R.id.eventname);
        startTime = (TextView)getActivity().findViewById(R.id.starttime);
        description = (TextView)getActivity().findViewById(R.id.description);
        setNotif = (Button)getActivity().findViewById(R.id.setnotification);
        URL img_url = null;
        
        eventName.setText(EventInformation.eventName); 
        //Log.v("what", EventInformation.description );
        
        
       
        try {
        	 startTime.setText(sdf.parse(EventInformation.startTime).toString());
			img_url = new URL("http://graph.facebook.com/" + EventInformation.eventID + "/picture?type=normal");
			Bitmap bmp = BitmapFactory.decodeStream(img_url.openConnection().getInputStream());
			pic.setImageBitmap(bmp);			
			String jsondata = Utility.fb.request( EventInformation.eventID );
			JSONObject obj = Util.parseJson(jsondata);
			description.setText(obj.optString("description"));
			description.setMovementMethod(new ScrollingMovementMethod());
						
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        setNotif.setOnClickListener(new OnClickListener() {
        	  public void onClick(View view) {
        		  
        		  try {
        			  //change eventinformation times into dates readable by android
        			  Date dateStartTime = sdf.parse(EventInformation.startTime);
        			  Date dateEndTime = sdf.parse(EventInformation.endTime);
        			  
        			  //putting information into calendar
        			  Calendar cal = Calendar.getInstance();              
        			  Intent intent = new Intent(Intent.ACTION_EDIT);
        			  intent.setType("vnd.android.cursor.item/event");
        			  intent.putExtra("title", EventInformation.eventName);
        			  intent.putExtra("beginTime", dateStartTime.getTime());
        			  intent.putExtra("endTime", dateEndTime.getTime());
        			  
        			  startActivity(intent);
	        		 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		  
        		  
        		  
        	  }
        	});
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
