package com.zevenapps.dailystatusupdater;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import Utility.ConnectionDetector;
import Utility.Constants;
import Utility.StorageHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity 
{
	private WebView wvLogin;
	private WebViewClient mWebViewClient;
	private WebSettings mWebSettings;
	private String redirected;
	protected String redirectUri = "https://dailystatusdev.azurewebsites.net/Status";
	private String mAuthCode;
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
	public ProgressDialog myDialog;
	public String firstname;
	protected boolean canContinue =true;
	public boolean accessTokenExpired;
	public String mPortalId;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//Component initialization
		wvLogin = (WebView)findViewById(R.id.WVLogin);


		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		if(cd.isConnectingToInternet()== false)
		{
			new AlertDialog.Builder(this)
			.setTitle("Network error")
			.setMessage("Please check your internet connection")
			.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) 
				{ 
					canContinue  = false;
					finish();
				}
			})
			.show();
		}
		else
		{

			//WebView settings
			AppWebViewClient webViewClient = new AppWebViewClient(this);
			wvLogin.setWebViewClient(webViewClient);
			//Turn on javascript (required for AD web Auth)
			WebSettings settings = wvLogin.getSettings();
			settings.setJavaScriptEnabled(true);

			String loginUrl = Constants.LOGIN_URL + 
					Constants.TENANT +
					"/oauth2/authorize?response_type=code&resource=" +
					Constants.RESOURCE + "&client_id=" +
					Constants.CLIENT_ID + "&redirect_uri=" + 
					Constants.REDIRECT_URL;
			//Load our webview URL
			wvLogin.loadUrl(loginUrl);
		}


	}



	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}



	class AppWebViewClient extends WebViewClient
	{

		private Context mContext;

		public AppWebViewClient(Context context)
		{
			mContext = context;
		}


		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{		
			//Trigger this activity to finish when we hit the REDIRECT_URL
			if (url.startsWith(Constants.REDIRECT_URL)) 
			{
				LoginActivity activity = (LoginActivity) mContext;
				activity.completeWithCode(url);
				return true;
			}

			view.loadUrl(url);
			return false;
		}
	}

	public void completeWithCode(String url)
	{
		// TODO Auto-generated method stub
		//Callback for finishing this activity and returning the code
		/*Intent data = new Intent();
				data.putExtra(Constants.EXTRA_CODE_URL, url);			
				setResult(Constants.RESULT_CODE_AUTHENTICATE, data);
				finish();*/

		if(url.contains("code="))
		{
			Uri uri = Uri.parse(url);
			mAuthCode = uri.getQueryParameter("code");

			/*Intent mIntnet = new Intent(LoginActivity.this, StatusUpdate.class);
			mIntnet.putExtra("Code", mAuthCode);
			startActivity(mIntnet);
			finish();*/

			new FetchAuthTokenTask().execute();

		}
	}

	private class FetchAuthTokenTask extends AsyncTask<Void, Integer, String>
	{
		ProgressDialog dialog;
		String result;
		String resultnext;
		StorageHelper helper ;
		@Override
		protected String doInBackground(Void... params) 
		{
			// TODO Auto-generated method stub
			String mUrl = Constants.LOGIN_URL + Constants.TENANT +"/oauth2/token";


			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(mUrl);

			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("client_id", Constants.CLIENT_ID));
				nameValuePairs.add(new BasicNameValuePair("code", mAuthCode));
				nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
				nameValuePairs.add(new BasicNameValuePair("redirect_uri", Constants.REDIRECT_URL));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);

				//Deserialize the data into JSON
				JSONObject statusObject = new JSONObject(result);
				//Pull values out of the JSON
				mAccessToken = statusObject.getString("access_token");
				Log.i(TAG, "Access Token: " + mAccessToken);
				mExpiresIn = statusObject.getString("expires_in");
				mExpiresOn = statusObject.getString("expires_on");
				mIdToken = statusObject.getString("id_token");
				mRefreshToken = statusObject.getString("refresh_token");
				mResource = statusObject.getString("resource");
				mScope = statusObject.getString("scope");
				mTokenType = statusObject.getString("token_type");


				//Get claims out of access token (content between 2nd and 3rd periods)
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
				firstname = jObject.getString("given_name");


				//Save user session details

				helper = new StorageHelper(getApplicationContext());
				helper.createLoginSession(mAuthCode,mAccessToken, mExpiresIn, mExpiresOn, mIdToken, mRefreshToken, mResource, mScope, mTokenType, userName, firstname);


			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return mAccessToken;
		}
		@Override
		protected void onPostExecute(String result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(myDialog.isShowing() && dialog!=null)
			{
				myDialog.dismiss();

			}
			Intent mIntent = new Intent(LoginActivity.this, StatusUpdate.class);
			mIntent.putExtra("AccessToken", result);

			startActivity(mIntent);
			finish();

		}

		@Override
		protected void onPreExecute()

		{
			// TODO Auto-generated method stub

			super.onPreExecute();

			myDialog = new ProgressDialog(LoginActivity.this).show(LoginActivity.this, "Fetching news..", "Just a moment");

			myDialog.getWindow().setContentView(R.layout.waitdialog);
			myDialog.getWindow().setTitle("Loading..");
			myDialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			myDialog.show();

		}



	}



}
