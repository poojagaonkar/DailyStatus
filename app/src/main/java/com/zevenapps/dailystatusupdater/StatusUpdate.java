package com.zevenapps.dailystatusupdater;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Utility.ConnectionDetector;
import Utility.Constants;
import Utility.StorageHelper;
import Utility.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StatusUpdate extends FragmentActivity  implements ActionBar.TabListener 
{
	private String accesstoken, refreshToken;
	
	private StorageHelper helper;

	private ViewPager viewPager;

	private ActionBar actionBar;

	private TabsPagerAdapter mAdapter;

	private String[] tabs = { "Running\nLate", "Work\nFrom", "Reports"};
	private int[] icons ={R.drawable.running,R.drawable.color_icons_green_home, R.drawable.report};

	protected boolean canContinue = true;

	protected ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statusupdate);
		
		
		viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);     
		 
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
		else if(canContinue)
		{	
        //Get  user details
        helper = new StorageHelper(getApplicationContext());
		 HashMap<String, String> userDetailMap = helper.getUserDetails();
		 accesstoken = userDetailMap.get("accesstoken");
		 refreshToken = userDetailMap.get("refresh_token");
		 
		
		  // Adding Tabs
       for (int i= 0; i< tabs.length && i< icons.length; i++) 
       {
    	 
    	  
           actionBar.addTab(actionBar.newTab().setText(tabs[i]).setIcon(icons[i])
                   .setTabListener(this));

		  
       }
       
      
       //Change ActionBar color
       actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009788")));
       //Change TabColor
       actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00796A")));

       /**
        * on swiping the viewpager make respective tab selected
        * */
       viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

           @Override
           public void onPageSelected(int position)
           {
               // on changing the page
               // make respected tab selected
               actionBar.setSelectedNavigationItem(position);
            
           }

           @Override
           public void onPageScrolled(int arg0, float arg1, int arg2) {
           }

           @Override
           public void onPageScrollStateChanged(int arg0) {
           }
       });

		}
		
		//new AddStatusTask().execute();
	
	}
	
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	
	
	private class AddStatusTask extends AsyncTask<Void, Void, String>
	{
		String AddedResult;
		@Override
		protected String doInBackground(Void... params)
		{
			// TODO Auto-generated method stub
			// Execute HTTP Post Request
	        
	        
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(Constants.AddStatusEndpoint);
	        
			try 
			{
				httppost.setHeader("Authorization", accesstoken);
		        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		        
		        nvps.add(new BasicNameValuePair("Comment", "AndroidTest"));
		        nvps.add(new BasicNameValuePair("TimeSlot", "FullDay"));
		        nvps.add(new BasicNameValuePair("StartTime", "2014-08-29T06:47:17"));
		        nvps.add(new BasicNameValuePair("EndTime", "2014-08-29T10:47:17"));
		        nvps.add(new BasicNameValuePair("WorkLocation","Office"));
		        AbstractHttpEntity entity;
				entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
				entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
		        entity.setContentEncoding("UTF-8");
		        httppost.setEntity(entity);
		        HttpResponse responsenext = httpclient.execute(httppost);
		        HttpEntity entitynext = responsenext.getEntity();
		        AddedResult= EntityUtils.toString(entitynext);
		
		
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        		return AddedResult;
		}
		
		
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
  {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbaroptions, menu);
 
        return super.onCreateOptionsMenu(menu);
    }
  
  /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Take appropriate action for each action item click
    	StorageHelper helper = new StorageHelper(getApplicationContext());
        switch (item.getItemId()) {
       
      //Edit user details
        case R.id.action_editInfo:
        	
        	helper.logoutUser();
        	
        	break;
        // Call Admin/HR
        case R.id.txtLoading:
        	String phoneNumber = "+919011464842";
        	Intent callIntent = new Intent(Intent.ACTION_CALL);
        	callIntent.setData(Uri.parse("tel:" + phoneNumber));
        	startActivity(callIntent);
        	break;
        	
           
        default:
            return super.onOptionsItemSelected(item);
        }
		return true;
    }
}
