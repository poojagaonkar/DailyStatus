package com.zevenapps.dailystatusupdater;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utility.ArticleFilter;
import Utility.Constants;
import Utility.ReportExpandableListAdapter;
import Utility.StatusResponse;
import Utility.StorageHelper;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class Reports extends Fragment 
{
	public static List<StatusResponse> mResponseList = new ArrayList<StatusResponse>();
	private ListView statusList;
	private RelativeLayout rlLoading;
	public ArrayList<StatusResponse> rowItems;
	private ExpandableListView expListView;
	private ExpandableListAdapter listAdapter;
	List<String> listDataHeader;
    HashMap<String, List<StatusResponse>> listDataChild;
	public StorageHelper helper;
	public HashMap<String, String> mMap;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.reportlayout, null);
		statusList = (ListView)v.findViewById(android.R.id.list);
		rlLoading = (RelativeLayout)v.findViewById(R.id.loadingPanel);
		
		expListView = (ExpandableListView)v.findViewById(R.id.lvExpStatus);
		/*expListView.expandGroup(0);
		expListView.expandGroup(1);
		expListView.expandGroup(2);
		expListView.expandGroup(3);*/
		
		
	
		
		
		return v;
	}
	private class GetStatusTask extends AsyncTask<Void, Void, List<StatusResponse>>
	{
		String AddedResult;
		JSONArray jsonArray;
		ProgressDialog pd = null;
		private ArrayList<StatusResponse> mResponseList;
		String runLate, wfhFull, wfhFirst, wfhSecond;
		Date strtDate, enddDate;
		@Override
		protected List<StatusResponse> doInBackground(Void... params)
		{
			// TODO Auto-generated method stub
			// Execute HTTP Post Request
	        
	        mResponseList = new ArrayList<StatusResponse>();
	        HttpClient httpclient = new DefaultHttpClient();
	        
	       
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date date = new Date();
	        String sDate = dateFormat.format(date); //2014/08/06 15:59:48
	        String eDate =  sDate + "T23:59:59";
	        sDate = sDate + "T00:00:00";
	        
	        helper = new StorageHelper(getActivity());
	        mMap = helper.getUserDetails();
	        String accessToken =  mMap.get("accesstoken");
	       /*
	        * Url Query to access OData services.
	        */
	      
	       //https://dailystatusdev.azurewebsites.net/odata/OStatus?$filter=StartTime%20gt%20datetime%272014-09-25T00:00:00.000%27%20and%20StartTime%20lt%20datetime%272014-09-25T23:59:59.000%27
	       String url = Constants.StatusQueryendpoint+"?$filter=StartTime%20gt%20datetime%27"+sDate+".000%27%20and%20StartTime%20lt%20datetime%27"+eDate+".000%27";
	    
	        HttpGet httpget =  new HttpGet(url);
	        httpget.setHeader("Authorization", "Bearer"+" "+accessToken);
	        httpget.setHeader("Accept", "application/json");
	        httpget.setHeader("Content-type","application/json; charset=UTF-8");
			try 
			{
				
		        HttpResponse responsenext = httpclient.execute(httpget);
		        HttpEntity entitynext = responsenext.getEntity();
		        AddedResult= EntityUtils.toString(entitynext);
		        
		        
		        JSONObject jsonObject = new JSONObject(AddedResult);
                jsonArray = jsonObject.getJSONArray("value");
		   
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject menuObject = jsonArray.getJSONObject(i);
					
					String createdBy =  menuObject.getString("CreatedBy");
					String comment =  menuObject.getString("Comment");
					String location =  menuObject.getString("Location");
					String slot =   menuObject.getString("Slot");
					String reachingAt =   menuObject.getString("StartTime");
					String lunch =  menuObject.getString("Lunch");
					
					mResponseList.add(new StatusResponse(createdBy, comment, location, slot, reachingAt, lunch));
				}
		
		
			} catch (UnsupportedEncodingException e) {
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
	        
			
			
	        		return mResponseList;
		}
		@Override
		protected void onPostExecute(List<StatusResponse> resultList) 
		{
			// TODO Auto-generated method stub
				Collections.reverse(resultList);
		
				ArrayList<StatusResponse> runLateList = Lists.newArrayList(Collections2.filter(resultList, new ArticleFilter("Running late")));
				ArrayList<StatusResponse> wfhFullList = Lists.newArrayList(Collections2.filter(resultList, new ArticleFilter("FullDay")));
				ArrayList<StatusResponse> wfhFirstList = Lists.newArrayList(Collections2.filter(resultList, new ArticleFilter("FirstHalf")));
				ArrayList<StatusResponse> wfhSecondList = Lists.newArrayList(Collections2.filter(resultList, new ArticleFilter("Second Half")));
				
				 listDataHeader = new ArrayList<String>();
			        listDataChild = new HashMap<String, List<StatusResponse>>();
			 
			        // Adding child data
			        listDataHeader.add("Running Late");
			        listDataHeader.add("WFH Full Day");
			        listDataHeader.add("WFH First Half");
			        listDataHeader.add("WFH Second Half");
			        
			        listDataChild.put(listDataHeader.get(0), runLateList); // Header, Child data
			        listDataChild.put(listDataHeader.get(1), wfhFullList);
			        listDataChild.put(listDataHeader.get(2), wfhFirstList);
			        listDataChild.put(listDataHeader.get(3), wfhSecondList);
			        
			        
			        listAdapter = new ReportExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
					 
			        // setting list adapter
			        expListView.setAdapter(listAdapter);
				
		     /*   ResultListAdapter adapter = new ResultListAdapter(getActivity(), R.layout.listresultrows, resultList);
		        adapter.notifyDataSetChanged();
		        statusList.setAdapter(adapter);*/
				
				
		        
		        
		       
		        rlLoading.setVisibility(View.GONE);
		        expListView.setVisibility(View.VISIBLE);
		        
		        
		        expListView.expandGroup(0);
				expListView.expandGroup(1);
				expListView.expandGroup(2);
				expListView.expandGroup(3);
		        	 
		         
		        
		       
		}
		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}
		
		
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
{
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser)
	    { 
	    	new GetStatusTask().execute();
	    }


	}
	
	
}
