package com.newgame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class NewgameActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	
	
	ImageView pic, button; 
	SharedPreferences sharedPreferences;
	TextView welcome;
	Button post, eventsbutton;
	
	JSONArray events[] = null;
	
	public void showInfo(String errormessage){
        new AlertDialog.Builder(this)
        .setTitle("error")
        .setMessage(errormessage)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .show();
        
         
    }
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      //needed to properly use online settings. NOTE: STILL NOT SURE WHY THIS IS NEEDED....
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
        
        //Declarations
        String APP_ID = getString(R.string.APP_ID); 
        Utility.fb = new Facebook(APP_ID);
        welcome = (TextView)findViewById(R.id.welcome);
        post = (Button)findViewById(R.id.postbutton);
        eventsbutton = (Button)findViewById(R.id.events);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NewgameActivity.this);
        String access_token = sharedPreferences.getString("access_token", null);
        Utility.access_token = access_token;
        long expires = sharedPreferences.getLong("access_expires", 0);
        Utility.asyncRunner = new AsyncFacebookRunner(Utility.fb);
        
        if (access_token != null){
        	Utility.fb.setAccessToken(access_token);
        }
        if (expires != 0)
        {          
            Utility.fb.setAccessExpires(expires);
        }
        
        button = (ImageView)findViewById(R.id.login);
        pic = (ImageView)findViewById(R.id.profilepic);
        button.setOnClickListener(this);
        updateButtonImage();      
        
        
    }

	private void updateButtonImage() {
		// TODO Auto-generated method stub
		
		
		if(Utility.fb.isSessionValid()){
			button.setImageResource(R.drawable.logout_button);
			pic.setVisibility(ImageView.VISIBLE);
			post.setVisibility(Button.VISIBLE);
			eventsbutton.setVisibility(Button.VISIBLE);
			
			JSONObject obj = null;
			URL img_url = null;
			
			
			try {
				String jsonUser = Utility.fb.request("me");
				Log.v("hello", jsonUser);
				obj = Util.parseJson(jsonUser);
				
				String id = obj.optString("id");
				String name = obj.optString("name");		
				welcome.setText("welcome, " + name);
				img_url = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
				Bitmap bmp = BitmapFactory.decodeStream(img_url.openConnection().getInputStream());
				pic.setImageBitmap(bmp);
				
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}else{
			button.setImageResource(R.drawable.login_button);
			pic.setVisibility(ImageView.INVISIBLE);
			eventsbutton.setVisibility(Button.INVISIBLE);
			post.setVisibility(Button.INVISIBLE);	
			//what if i add a comment like jian was here
		}
	}
	
	public void buttonClicks(View v){
		switch(v.getId()){
		case R.id.postbutton:
			//post
			
			Utility.fb.dialog(NewgameActivity.this, "feed", new DialogListener() {
				
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					
				}
				
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					
				}
				
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					
				}
				
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
			break;
			
		case R.id.events:
			
			Intent myIntent = new Intent(NewgameActivity.this, EventsListActivity.class);
			NewgameActivity.this.startActivity(myIntent);
			
			break;
		}
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(Utility.fb.isSessionValid()){
			//close our session -- logout of facebook
			try {
				Log.v("testing", "the session is valid");
				Utility.fb.logout(getApplicationContext());
				updateButtonImage();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//login to facebook
			Toast.makeText(NewgameActivity.this, "Authorizing", Toast.LENGTH_SHORT).show();
			Utility.fb.authorize(NewgameActivity.this, new String[] {"email", "user_events", "friends_events"}, Utility.fb.FORCE_DIALOG_AUTH , new DialogListener() {
				
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					Toast.makeText(NewgameActivity.this, "fbError", Toast.LENGTH_SHORT);
					
				}
				
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					Toast.makeText(NewgameActivity.this, "onError", Toast.LENGTH_SHORT);
				}
				
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					Editor editor = sharedPreferences.edit();
					editor.putString("access_token", Utility.fb.getAccessToken());
					editor.putLong("access_expires", Utility.fb.getAccessExpires());
					editor.commit();
					updateButtonImage();
				}
				
				public void onCancel() {
					// TODO Auto-generated method stub
					Toast.makeText(NewgameActivity.this, "onCancel", Toast.LENGTH_SHORT);
				}
			});
		}
			
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Utility.fb.authorizeCallback(requestCode, resultCode, data);
	}
}