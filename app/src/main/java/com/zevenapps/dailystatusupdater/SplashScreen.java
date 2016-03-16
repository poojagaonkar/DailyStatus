package com.zevenapps.dailystatusupdater;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import Utility.Constants;
import Utility.StorageHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SplashScreen extends Activity 
{

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	private StorageHelper helper;


	private String mAccessToken;
	private String TAG;
	private String mExpiresIn;
	private String mExpiresOn;
	private String mIdToken;
	private String mRefreshToken;
	private String mResource;
	private String mScope;
	private String mTokenType;
	public String userName;
	public String firstName;
	public HashMap<String, String> mMap;

	public String result;

	public String mPortalId;

	protected String mAuthCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		helper =  new StorageHelper(getApplicationContext());



		  
	        ImageView imgLogo = (ImageView) findViewById(R.id.imageZevenTop);
	        TranslateAnimation anim = new TranslateAnimation(
	        	    TranslateAnimation.ABSOLUTE, 50, 
	        	    TranslateAnimation.ABSOLUTE, 0,
	        	    TranslateAnimation.ABSOLUTE, 0.0f,
	        	    TranslateAnimation.ABSOLUTE, 0.0f
	        	);
	        	anim.setFillAfter(true);
	        	anim.setDuration(1500);

	        	imgLogo.startAnimation(anim);

	        ImageView imgLogo1 = (ImageView) findViewById(R.id.imagezevenBottom);
	        TranslateAnimation anim1 = new TranslateAnimation(
	        	    TranslateAnimation.ABSOLUTE, -50, 
	        	    TranslateAnimation.ABSOLUTE, 0,
	        	    TranslateAnimation.ABSOLUTE, 0.0f,
	        	    TranslateAnimation.ABSOLUTE, 0.0f
	        	);
	        	anim1.setFillAfter(true);
	        	anim1.setDuration(1500);

	        	imgLogo1.startAnimation(anim1);




		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer.
			 */

			@Override
			public void run() 
			{

				// This method will be executed once the timer is over
				// Start your app main activity

				if(helper.isLoggedIn()==true)
				{
					mMap = helper.getUserDetails();

					mAccessToken =  mMap.get("accesstoken");
					mRefreshToken = mMap.get("refresh_token");
					mExpiresIn = mMap.get("expires_in");
					mExpiresOn = mMap.get("expires_on");
					mIdToken = mMap.get("id_token");
					mScope = mMap.get("scope");
					mTokenType = mMap.get("token_type");
					userName = mMap.get("username");
					firstName = mMap.get("name");
					mAuthCode  = mMap.get("auth_code");
					/*
					 * Check if AccessToken need to be refreshed
					 */
					if( mAccessToken.isEmpty())

                    {
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
					int expiryHour = Integer.parseInt(mExpiresIn);  
					expiryHour = (expiryHour/60)/60;


					String expiredOn =  getDate(Long.parseLong(mExpiresOn)*1000);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentDateandTime = sdf.format(new Date());

					new RefreshTokenTask().execute();
					
				}
				else
				{

					Intent i = new Intent(SplashScreen.this, LoginActivity.class);
					startActivity(i);
					finish();
					// close this activity
				}
			}
		}, SPLASH_TIME_OUT);
	}


	private String getDate(long milliSeconds)
	{
		// Create a DateFormatter object for displaying date in specified format.
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Create a calendar object that will convert the date and time value in milliseconds to date. 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}


	private class RefreshTokenTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
			/**
			 * Check if access token is expired
			 * Request new  access token  by passing refresh token 
			 */
			String mUrl = Constants.LOGIN_URL + Constants.TENANT +"/oauth2/token";

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(mUrl);
			int expiryHour = (Integer.parseInt(mExpiresIn)/60)/60;



			try 
			{
				List<NameValuePair> nvps = new ArrayList<NameValuePair>(2);
				
				nvps.add(new BasicNameValuePair("grant_type", "refresh_token"));
				nvps.add(new BasicNameValuePair("refresh_token", mRefreshToken));
				httppost.setEntity(new UrlEncodedFormEntity(nvps));

				// Execute HTTP Post Request
				HttpResponse refreshResponse = httpclient.execute(httppost);
				HttpEntity refreshEntity = refreshResponse.getEntity();
				result = EntityUtils.toString(refreshEntity);

				//Deserialize the data into JSON
				JSONObject refreshStatusObject = new JSONObject(result);
				//Pull values out of the JSON
				mAccessToken = refreshStatusObject.getString("access_token");
				
				mExpiresIn = refreshStatusObject.getString("expires_in");
				

				int firstIndex = mAccessToken.indexOf(".");
				int secondIndex = mAccessToken.indexOf(".", firstIndex+2);
				String claims = mAccessToken.substring(firstIndex + 1, secondIndex);
				//Decode base64 URL ended claims
				byte[] data = Base64.decode(claims, Base64.URL_SAFE);

				String text = new String(data, "ASCII");
				//Display claims on screen

				JSONObject jObject = new JSONObject(text);
				//Get and display the logged in user name
				userName = jObject.getString("unique_name");
				firstName = jObject.getString("given_name");

				helper.createLoginSession(mAuthCode,mAccessToken, mExpiresIn, mExpiresOn, mIdToken, mRefreshToken, mResource, mScope, mTokenType, userName, firstName);
			} 
			catch (UnsupportedEncodingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}




			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent i = new Intent(SplashScreen.this, StatusUpdate.class);
			startActivity(i);
			finish();
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}


	}


}
