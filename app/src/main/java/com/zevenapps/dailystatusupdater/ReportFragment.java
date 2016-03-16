package com.zevenapps.dailystatusupdater;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utility.Constants;
import Utility.ResultListAdapter;
import Utility.StatusResponse;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ReportFragment extends DialogFragment
{
	private ListView statusList;
	Dialog dialog;
    AlertDialog.Builder builder;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		 builder = new AlertDialog.Builder(getActivity());
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 View view = getActivity().getLayoutInflater().inflate(R.layout.updateresult, null);
		 statusList = (ListView)view.findViewById(android.R.id.list);
		 
         builder.setMessage("Reports")
         .setView(view)
         .setCancelable(false)
         .setPositiveButton("OK", new DialogInterface.OnClickListener() 
         {
             public void onClick(DialogInterface dialog, int id) {
                 //Do something here
             }
         });
         new GetStatusTask().execute();
         dialog = builder.create();
		
		return dialog;
	}
	/*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
        View rootView = inflater.inflate(R.layout.updateresult, container,false);
        statusList = (ListView)rootView.findViewById(android.R.id.list);
        
        getDialog().setTitle("Status Report");  
        
       
        // Do something else
        return rootView;
    }*/
	private class GetStatusTask extends AsyncTask<Void, Void, List<StatusResponse>>
	{
		String AddedResult;
		JSONArray jsonArray;
		ProgressDialog pd = null;
		private ArrayList<StatusResponse> mResponseList;
		@Override
		protected List<StatusResponse> doInBackground(Void... params)
		{
			// TODO Auto-generated method stub
			// Execute HTTP Post Request
	        
	        mResponseList = new ArrayList<StatusResponse>();
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpGet httpget = new HttpGet(Constants.AddStatusEndpoint);
	        
			try 
			{
				
		        HttpResponse responsenext = httpclient.execute(httpget);
		        HttpEntity entitynext = responsenext.getEntity();
		        AddedResult= EntityUtils.toString(entitynext);
		        
		        jsonArray = new JSONArray(AddedResult);
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
				
			/*	Calendar currentDate = Calendar.getInstance(); //Get the current date
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd"); //format it as per your requirement
				String dateNow = formatter.format(currentDate.getTime());
				ArrayList<StatusResponse> filteredResultList = Lists.newArrayList(Collections2.filter(resultList, new ArticleFilter(dateNow)));*/
				
				
		        ResultListAdapter adapter = new ResultListAdapter(getActivity(), R.layout.listresultrows, resultList);
		        adapter.notifyDataSetChanged();
		        statusList.setAdapter(adapter);
		        
		       
		}
		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}
		
		
	}
}
