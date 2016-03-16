package com.zevenapps.dailystatusupdater;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Utility.AccelerometerListener;
import Utility.AccelerometerManager;
import Utility.Constants;
import Utility.StatusResponse;
import Utility.StorageHelper;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class RunningLateStatus extends Fragment implements AccelerometerListener
{
	private TextView txtReachingBy;
	private RelativeLayout rlSaveRunningLate;
	private SeekBar seekLateBy, seekBarMins;
	private TextView lblLateBy, txtUpdate, txtWelcome, txtHours, txtMinutes;
	private Calendar calender;

	private SimpleDateFormat sdf;
	private String accessToken;
	private StorageHelper helper;
	private HashMap<String, String> mMap;
	private String status, comment, timeString, startTime;
	private Typeface roboto;
	private EditText editComment;
	public Time today;
	public int month;
	public String username, name;
	private AlertDialog myDialog;
	private int progressChangedMins = 0,  progressChangedHours = 0;
	private int hour =00, mins=00;
	private int[] minValue ={0,15,30,45};
	int currentMins;
	long hourLimit = 10;
	long minLimit =30;
	private ImageButton ibReport;
	String initialTime = "10:30";
	Calendar cal ;
	public List<StatusResponse> resultList;
	AlertDialog.Builder builder;
	AlertDialog alert;

	int previousval=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.runninglate, null);

		//Component initialization
		txtReachingBy = (TextView)mView.findViewById(R.id.txtReachingBy);
		seekLateBy = (SeekBar)mView.findViewById(R.id.seekBarLate);
		rlSaveRunningLate = (RelativeLayout)mView.findViewById(R.id.RLUpdateRunningLate);
		lblLateBy = (TextView)mView.findViewById(R.id.LblLateBy);
		editComment = (EditText)mView.findViewById(R.id.ETStatusComment);
		txtUpdate = (TextView)mView.findViewById(R.id.txtUpdate);
		txtWelcome = (TextView)mView.findViewById(R.id.txtWelcome);
		txtHours = (TextView)mView.findViewById(R.id.txtHours);
		txtMinutes = (TextView)mView.findViewById(R.id.txtMinutes);
		seekBarMins = (SeekBar)mView.findViewById(R.id.seekBarMins);





		// Calender instance to get current time
		calender = Calendar.getInstance();


		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date date;
		try 
		{
			date = sdf.parse(initialTime);
			cal= Calendar.getInstance();
			cal.setTime(date);

			txtReachingBy.setText("Reaching By:"+" "+ cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//SetFont
		roboto = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Thin.ttf");
		lblLateBy.setTypeface(roboto,Typeface.BOLD);
		txtReachingBy.setTypeface(roboto, Typeface.BOLD);
		editComment.setTypeface(roboto,Typeface.BOLD);
		txtUpdate.setTypeface(roboto, Typeface.BOLD);
		txtHours.setTypeface(roboto, Typeface.BOLD);
		txtMinutes.setTypeface(roboto, Typeface.BOLD);
		txtWelcome.setTypeface(roboto,Typeface.BOLD);



		today= new Time(Time.getCurrentTimezone());
		today.setToNow();

		helper = new StorageHelper(getActivity());
		mMap = helper.getUserDetails();
		name = mMap.get("name");

		//Set initial Time to 10.30
		// txtReachingBy.setText("Reaching by:"+" "+String.valueOf(hourLimit)+":"+String.valueOf(minLimit)+":"+today.second);
		txtWelcome.setText("Hi"+" "+ name);

		/*
		 * Change the Late by and Reaching at values on SeekBar change
		 */


		seekLateBy.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{


			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				progressChangedHours = progress;

				if(progressChangedHours==0 && progressChangedMins!=0)
				{
					lblLateBy.setText("Late By (Hrs):"+" " + String.valueOf(progressChangedHours) +":"+String.valueOf(progressChangedMins));
				}
				else
					lblLateBy.setText("Late By (Hrs):"+" " + String.valueOf(progressChangedHours) +":"+String.valueOf(progressChangedMins));


			}



			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub
				hourLimit = 10;
				
			}

			public void onStopTrackingTouch(SeekBar seekBar) 
			{
				hourLimit =10;
				if(progressChangedHours==0)
				{
					//timeString = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+today.second;
				
					
					if(minLimit ==0)
					{
						timeString = (hourLimit)+":"+"00";
					}
					else
					timeString = (hourLimit)+":"+minLimit;
				}
				else
				{

					hourLimit = hourLimit+progressChangedHours;
					if(minLimit ==0)
					{
						timeString = (hourLimit)+":"+"00";
					}
					else
					timeString = (hourLimit)+":"+minLimit;

				}
				
				txtReachingBy.setText("Reaching by:"+" "+timeString);
			}





		}); 

		seekBarMins.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{
			boolean isDragging = false;

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				progressChangedMins = progress;
				int stepSize = 15;

				progressChangedMins = (progressChangedMins/stepSize)*stepSize;
				seekBar.setProgress(progressChangedMins);
				if(progressChangedHours ==0 && progressChangedMins!=0)
				{
					lblLateBy.setText("Late By (Hrs):"+" " + String.valueOf(progressChangedHours) +":"+String.valueOf(progressChangedMins));
				}

				else
					lblLateBy.setText("Late By (Hrs):"+" " + String.valueOf(progressChangedHours) +":"+String.valueOf(progressChangedMins));



			}



			public void onStartTrackingTouch(SeekBar seekBar) 
			{
				// TODO Auto-generated method stub
				
				minLimit = 30;

			}

			public void onStopTrackingTouch(SeekBar seekBar) 
			{

				minLimit =  30;
				if(progressChangedMins==0)
				{
					timeString = (hourLimit)+":"+minLimit;
				}

				switch (progressChangedMins)
				{
				case 0:
					if(hourLimit>=10)
						minLimit = 30;

					break;
				case 15:

					minLimit = minLimit+15;
					break;

				case 30:
					minLimit= 0;
					if(hourLimit==10)
					{
						hourLimit = 11;	
					}
					else

						hourLimit = hourLimit+1;

					break;
				case 45 :
					minLimit =15;
					if(progressChangedHours==0)
					{
						hourLimit = 11;	
					}
					else

						hourLimit = hourLimit+1;
					break;
					
		
				}	 

				if(minLimit==0)
				{
					timeString = (hourLimit)+":"+"00";
				}
				else
				{
					timeString = (hourLimit)+":"+minLimit;
				}
				txtReachingBy.setText("Reaching by:"+" "+timeString);

			}




		}); 

		// Execute AddStatusTask to update the running late status
		rlSaveRunningLate.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(timeString == null || hourLimit ==10 && minLimit ==30)
				{
					timeString = hour+":"+mins+":"+today.second;
					Toast.makeText(getActivity(), "Please select the number of hours you are running late.", Toast.LENGTH_LONG).show();
				}
				else
				{
					showDialog(timeString, "Confirm");
				}

			}
		});


		return mView;

	}




	private class AddStatusTask extends AsyncTask<Void, Void, String>
	{
		String AddedResult;
		ProgressDialog pd;
		//Get Value of accessToken

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();

			pd = new ProgressDialog(getActivity());
			pd.setMessage("Updating..");
			pd.show();
		}
		@Override
		protected String doInBackground(Void... params)
		{
			// TODO Auto-generated method stub
			// Execute HTTP Post Request


			accessToken = mMap.get("accesstoken");
			username = mMap.get("username");

			//Values

			if(editComment.getText().toString().length()==0)
			{
				comment = "-";

			}
			else
			{
				comment = editComment.getText().toString();
			}
			status = "Running late";


			today= new Time(Time.getCurrentTimezone());
			today.setToNow();
			month = today.month+1;
			startTime = today.year+"-"+month+"-"+today.monthDay+"T"+timeString;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.AddStatusEndpoint);

			// Set header with AccessToken
			httppost.setHeader("Authorization", "Bearer"+" "+accessToken);

			//Add parameters as Form encoded

			List <NameValuePair> nvps = new ArrayList <NameValuePair>(6);
			nvps.add(new BasicNameValuePair("CreatedBy",username));
			nvps.add(new BasicNameValuePair("Comment", comment));
			nvps.add(new BasicNameValuePair("Slot", status));
			nvps.add(new BasicNameValuePair("StartTime", startTime));
			nvps.add(new BasicNameValuePair("Location","Office"));
			nvps.add(new BasicNameValuePair("Lunch","true"));


			try
			{
				AbstractHttpEntity entity;
				entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);

				entity.setContentType("application/x-www-form-urlencoded");
				entity.setContentEncoding("UTF-8");
				httppost.setEntity(entity);

				//Fetch the response
				HttpResponse responsenext;

				responsenext = httpclient.execute(httppost);

				if(responsenext.getStatusLine().getStatusCode() == 201)
				{
					HttpEntity entitynext = responsenext.getEntity();
					AddedResult= EntityUtils.toString(entitynext);
				}
				else
				{
					AddedResult = "Sorry";
				}

			} 
			catch (ClientProtocolException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return AddedResult;
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result.equalsIgnoreCase("Sorry"))
			{
				
				pd.dismiss();
				
				Toast.makeText(getActivity(), "Could not update. Please try again.", Toast.LENGTH_LONG).show();
			}
					
			
			if(pd!=null &&pd.isShowing())
			{
				pd.setMessage("Updated");
				new Handler().postDelayed(new Runnable() 
				{
					@Override
					public void run() 
					{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 2000);

				pd.dismiss();
				Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();
			}
			timeString =null;
			//cal.set(Calendar.HOUR_OF_DAY, 10);
		//	cal.set(Calendar.MINUTE, 30);
			seekLateBy.setProgress(0);
			seekBarMins.setProgress(0);
			txtReachingBy.setText("Reaching by:"+" "+initialTime);
			editComment.setText("");
			hourLimit = 10;
			minLimit = 30;

		}


	}


	@Override
	public void onAccelerationChanged(float x, float y, float z) 
	{
		// TODO Auto-generated method stub

	}


	@Override
	public void onShake(float force) 
	{
		// TODO Auto-generated method stub

		hour = cal.get(Calendar.HOUR);
		hour = hour+1;
		mins = cal.get(Calendar.MINUTE);
		timeString = hour+":"+mins+":"+today.second;
		showDialog(timeString, "Confirm Running late by 1 hour.");
	}
	


	public void showDialog(String timeString, String title)
	{

		builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(title);
		builder.setMessage("Reaching at:"+" "+ timeString+"?");

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing
				
			}
		});

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) 
			{
				// Do nothing but close the dialog
			
				new AddStatusTask().execute();

			}

		});


		builder.show();
	}


	@Override
	public void onResume() {
		super.onResume();

		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isSupported(getActivity()))
		{

			//Start Accelerometer Listening
			AccelerometerManager.startListening(this);
		}
		timeString =null;
		//cal.set(Calendar.HOUR_OF_DAY, 10);
		//cal.set(Calendar.MINUTE, 30);
		seekLateBy.setProgress(0);
		seekBarMins.setProgress(0);
		txtReachingBy.setText("Reaching by:"+" "+initialTime);
		editComment.setText("");
		hourLimit = 10;
		minLimit = 30;

	}

	@Override
	public void onStop() {
		super.onStop();

		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {

			//Start Accelerometer Listening
			AccelerometerManager.stopListening();


		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Sensor", "Service  distroy");

		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {

			//Start Accelerometer Listening
			AccelerometerManager.stopListening();


		}

	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		timeString =null;
		//cal.set(Calendar.HOUR_OF_DAY, 10);
		//cal.set(Calendar.MINUTE, 30);
		seekLateBy.setProgress(0);
		seekBarMins.setProgress(0);
		txtReachingBy.setText("Reaching by:"+" "+initialTime);
		editComment.setText("");
		hourLimit = 10;
		minLimit = 30;
	}


}
