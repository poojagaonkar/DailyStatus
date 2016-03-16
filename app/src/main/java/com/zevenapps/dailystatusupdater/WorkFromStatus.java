package com.zevenapps.dailystatusupdater;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import Utility.Constants;
import Utility.StorageHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorkFromStatus extends Fragment implements OnClickListener 
{
	private RelativeLayout rlFullDay, rlFirstHalf, rlSecondHalf;
	private TextView txtFullDay, txtFirstHalf, txtSecondHalf;
	private RadioGroup rgWorkLocation;
	private RadioButton rbHome, rbOthers;
	private Typeface robotoThin;
	private EditText editWFHComment;
	private String comment, timeslot, starttime, endtime, worklocation, username;
	private View mView;
	private RadioButton rbWorkLocation;
	public StorageHelper helper;
	public HashMap<String, String> mMap;
	public String accessToken;
	Dialog dialog1;
	CheckBox cbLunch;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.workfrom, null);

		rlFirstHalf = (RelativeLayout)mView.findViewById(R.id.RLFirstHalf);
		rlSecondHalf = (RelativeLayout)mView.findViewById(R.id.RLSecondHalf);
		rlFullDay = (RelativeLayout)mView.findViewById(R.id.RLFullDay);
		txtFirstHalf = (TextView)mView.findViewById(R.id.txtFirstHalf);
		txtSecondHalf = (TextView)mView.findViewById(R.id.txtSecondHalf);
		txtFullDay = (TextView)mView.findViewById(R.id.txtFullDay);
		editWFHComment = (EditText)mView.findViewById(R.id.ETWFHComment);
		rgWorkLocation = (RadioGroup)mView.findViewById(R.id.RGWorkLocation);
		rbHome = (RadioButton)mView.findViewById(R.id.RBHome);
		rbOthers = (RadioButton)mView.findViewById(R.id.RBOthers);

		//Text customization
		robotoThin = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Thin.ttf");
		rbHome.setTypeface(robotoThin);
		rbOthers.setTypeface(robotoThin);
		txtFirstHalf.setTypeface(robotoThin, Typeface.BOLD);
		txtFullDay.setTypeface(robotoThin,Typeface.BOLD);
		txtSecondHalf.setTypeface(robotoThin,Typeface.BOLD);
		editWFHComment.setTypeface(robotoThin);

		//Set clicks
		rlFirstHalf.setOnClickListener(this);
		rlSecondHalf.setOnClickListener(this);
		rlFullDay.setOnClickListener(this);


		return mView;

	}

	@Override
	public void onClick(View v)
	{
		
		// get selected radio button from radioGroup
		int selectedId = rgWorkLocation.getCheckedRadioButtonId();
		rbWorkLocation = (RadioButton)mView.findViewById(selectedId);
		worklocation = rbWorkLocation.getText().toString();

		if(editWFHComment.getText().toString().length()==0)
		{
			comment = "-";

		}
		else
		{
			comment = editWFHComment.getText().toString();
		}

		/*today= new Time(Time.getCurrentTimezone());
		today.setToNow();
		month = today.month+1;
		starttime = today.year+"-"+month+"-"+today.monthDay+"T"+today.hour+":"+today.minute+":"+today.second;*/
		Calendar cal = Calendar.getInstance();
		int hour =  cal.get(Calendar.HOUR);
		int min  =  cal.get(Calendar.MINUTE);
		int sec =  cal.get(Calendar.SECOND);
		int year =  cal.get(Calendar.YEAR);
		int month =  cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		
		String curTime = String.format("%02d:%02d:%02d", hour, min, sec);
		
		starttime =  String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day)+"T"+curTime;
		

		// TODO Auto-generated method stub
		AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();

		switch (v.getId())
		{
		case R.id.RLFullDay:

			timeslot = "FullDay";
			showDialog(timeslot, worklocation);
		

			break;

		case R.id.RLFirstHalf:
			timeslot = "FirstHalf";
			showDialog(timeslot, worklocation);
			break;

		case R.id.RLSecondHalf:
			timeslot = "Second Half";
			showDialog(timeslot, worklocation);
			break;
		}




	}

	public void showDialog(String timeSlot, String workLocation)
	{

		if(!timeSlot.matches("FullDay"))
		{
			dialog1 =new Dialog(getActivity());
			dialog1.setContentView(R.layout.customdialog);
			dialog1.setTitle("Confirm");
			TextView tvMessage = (TextView)dialog1.findViewById(R.id.txtMessage);
			cbLunch = (CheckBox)dialog1.findViewById(R.id.checkBoxLunch);
			Button btnok = (Button)dialog1.findViewById(R.id.btnok);
			Button btncancel = (Button)dialog1.findViewById(R.id.btncancel);
			tvMessage.setText("Work from "+" "+worklocation+" " +timeslot+"\nAre you sure?");

			btnok.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					if(cbLunch.isChecked())
					{
						new AddStatusTask("true").execute();
					}
					else
					{
						new AddStatusTask("false").execute();	
					}
					dialog1.dismiss();
				}
			});
			btncancel.setOnClickListener(new View.OnClickListener() 
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog1.dismiss();

				}
			});
			dialog1.show();
		}

		else
		{
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked

						new AddStatusTask("false").execute();
						dialog.dismiss();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						dialog.dismiss();
						break;
					}
				}
			};


			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


			builder.setMessage("Work from "+" "+worklocation+" " +timeslot+"\nAre you sure?").setPositiveButton("Yes", dialogClickListener)
			.setNegativeButton("No", dialogClickListener).show();
		}
	}
	
	

	private class AddStatusTask extends AsyncTask<Void, Void, String>
	{
		String AddedResult;
		ProgressDialog pd;
		String lunch;
		public AddStatusTask(String b)
		{
			// TODO Auto-generated constructor stub
			this.lunch = b;
		}
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

			helper = new StorageHelper(getActivity());
			mMap = helper.getUserDetails();
			accessToken = mMap.get("accesstoken");
			username = mMap.get("username");



			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.AddStatusEndpoint);

			try 
			{
				// Set header with AccessToken
				httppost.setHeader("Authorization", "Bearer"+" "+accessToken);

				//Add parameters as Form encoded
				List <NameValuePair> nvps = new ArrayList <NameValuePair>(6);
				nvps.add(new BasicNameValuePair("Comment", comment));
				nvps.add(new BasicNameValuePair("Slot", timeslot));
				nvps.add(new BasicNameValuePair("StartTime", starttime));
				nvps.add(new BasicNameValuePair("Location",worklocation));
				nvps.add(new BasicNameValuePair("CreatedBy",username));
				nvps.add(new BasicNameValuePair("Lunch",lunch));
				AbstractHttpEntity entity;
				entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);

				entity.setContentType("application/x-www-form-urlencoded");
				entity.setContentEncoding("UTF-8");
				httppost.setEntity(entity);

				//Fetch the response
				HttpResponse responsenext = httpclient.execute(httppost);
				if(responsenext.getStatusLine().getStatusCode() == 201)
				{
					HttpEntity entitynext = responsenext.getEntity();
					AddedResult= EntityUtils.toString(entitynext);
				}
				else
				{
					AddedResult = "Sorry";
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
				pd.dismiss();
				Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_LONG).show();
			}

			editWFHComment.setText("");
		}


	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}



}
