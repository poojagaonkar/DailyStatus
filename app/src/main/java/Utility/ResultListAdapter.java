package Utility;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.provider.UserDictionary.Words;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zevenapps.dailystatusupdater.R;

public class ResultListAdapter extends ArrayAdapter<StatusResponse>
{

	private TextView txtName, txtStatus, txtStartTime, txtLocation, txtComment, txtLunch;
	private List<StatusResponse> result;
	private String userName, status, starttime, location, lunch;
	private Context mContext;
	private String comment;
	private List<StatusResponse> finalList;
	
	private static LayoutInflater inflater = null;
	static class LazyViewHolder
	 {
		 TextView txtname;
		 TextView txtstatus;
		 TextView txtLocation;
		 TextView txtstarttime;
		 TextView txtComment;
		 TextView txtLunch;
	
	 }
	
	public ResultListAdapter(Context context, int resourceId,List<StatusResponse> items) 
	{
        super(context, resourceId, items);
        this.mContext = context;
        this.finalList = items;
    }
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View v = convertView;
		
		
		LazyViewHolder holder = new LazyViewHolder();
		if(v == null)
		{
			  v =inflater.inflate(R.layout.listresultrows, null);
		holder.txtname = (TextView)v.findViewById(R.id.txtName);
		holder.txtstatus = (TextView)v.findViewById(R.id.txtStatus);
		holder.txtstarttime = (TextView)v.findViewById(R.id.txtStartDate);
		//holder.txtLocation = (TextView)v.findViewById(R.id.txtLocation);
		holder.txtComment = (TextView)v.findViewById(R.id.txtComment);
		holder.txtLunch = (TextView)v.findViewById(R.id.txtLunch);
		v.setTag(holder);
		}
		else
	    holder = (LazyViewHolder) v.getTag();
		
		
		
		StatusResponse rowItem = finalList.get(position);
		
		
		userName = rowItem.getCreatedBy();
		userName = userName.split("@")[0];
	//	userName = Character.toUpperCase(userName.charAt(0)) + userName.substring(1);
	//	userName.replace(".", " ")
		
		status = rowItem.getSlot();
		location = rowItem.getLocation();
		comment= rowItem.getComment();
		starttime =rowItem.getStartTime();
		
		String mTime =  starttime.substring(starttime.lastIndexOf("T") + 1);
		lunch =  rowItem.getLunch();
		
		
		if(status.equals("FullDay") ||status.equals("FirstHalf") || status.equals("SecondHalf") ||
			status.equals("Full Day") ||status.equals("First Half") || status.equals("Second Half") )
		{
			status = "Work from "+" "+ location + " " +status;
		}
		
		holder.txtname.setText(userName);
		holder.txtstatus.setText(status);
		holder.txtstarttime.setText(mTime);
	//	holder.txtLocation.setText(location);
		
		
		
		if(comment.isEmpty() && comment.length()==0)
		{
			holder.txtComment.setText("-");
		}
		else
		{
			holder.txtComment.setText(comment);	
		}
		
		
		
		if(lunch=="true")
		{
		holder.txtLunch.setText("Yes");
		}
		else
		{
			holder.txtLunch.setText("No");
		}
		
		return v;
	}
	}
