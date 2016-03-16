package Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.zevenapps.dailystatusupdater.R;

import Utility.ResultListAdapter.LazyViewHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ReportExpandableListAdapter extends BaseExpandableListAdapter 
{
	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<StatusResponse>> _listDataChild;
	private TextView txtName, txtStatus, txtStartTime, txtLocation, txtComment, txtLunch;

	private String userName, status, starttime, location, lunch;
	private Context mContext;
	private String comment;
	private List<StatusResponse> finalList;
	private  StatusResponse rowItem;
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



	public ReportExpandableListAdapter(Context _context,
			List<String> _listDataHeader,
			HashMap<String, List<StatusResponse>> listDataChild) 
	{
		super();
		this._context = _context;
		this._listDataHeader = _listDataHeader;
		this._listDataChild = listDataChild;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) 

	{
		// TODO Auto-generated method stub
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
		
		
		
		rowItem=_listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);
		userName = rowItem.getCreatedBy();
		status = rowItem.getSlot();
		location = rowItem.getLocation();
		comment= rowItem.getComment();
		starttime =rowItem.getStartTime();
		lunch =  rowItem.getLunch();
		
		if(!userName.isEmpty())
		{
		userName = rowItem.getCreatedBy();
		userName = userName.split("@")[0];
		userName = Character.toUpperCase(userName.charAt(0)) + userName.substring(1);
		}

		String mTime =  starttime.substring(starttime.lastIndexOf("T") + 1);
		if(status.equals("FullDay") ||status.equals("FirstHalf") || status.equals("SecondHalf") ||
				status.equals("Full Day") ||status.equals("First Half") || status.equals("Second Half") )
		{
			status = "Work from "+" "+ location + " " +status;
		}

		holder.txtname.setText(userName);
		holder.txtstatus.setText(status);
		holder.txtstarttime.setText(mTime);

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



	/*		for (Entry<String, List<StatusResponse>> ee : _listDataChild.entrySet()) 
		{
		    String title = ee.getKey();
		    List<StatusResponse> values = ee.getValue();

		    for(int i = 0; i< values.size(); i++)
		    {
		    	rowItem =  values.get(i);
		    }
			userName = rowItem.getCreatedBy();
		    status = rowItem.getSlot();
			location = rowItem.getLocation();
			comment= rowItem.getComment();
			starttime =rowItem.getStartTime();
			lunch =  rowItem.getLunch();

			String mTime =  starttime.substring(starttime.lastIndexOf("T") + 1);
			if(status.equals("FullDay") ||status.equals("FirstHalf") || status.equals("SecondHalf") ||
					status.equals("Full Day") ||status.equals("First Half") || status.equals("Second Half") )
			{
				status = "Work from "+" "+ location + " " +status;
			}

			holder.txtname.setText(userName);
			holder.txtstatus.setText(status);
			holder.txtstarttime.setText(mTime);

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

		}



	List<StatusResponse> mList = _listDataChild.get(lunch);

		//userName = rowItem.getCreatedBy();
		//userName = userName.split("@")[0];
		//	userName = Character.toUpperCase(userName.charAt(0)) + userName.substring(1);
		//	userName.replace(".", " ")

		//status = rowItem.getSlot();
		//location = rowItem.getLocation();
		//comment= rowItem.getComment();
		//starttime =rowItem.getStartTime();

	//	String mTime =  starttime.substring(starttime.lastIndexOf("T") + 1);
	//	lunch =  rowItem.getLunch();


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
		}*/

	return v;
}

@Override
public int getChildrenCount(int groupPosition) {
	// TODO Auto-generated method stub
	return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
	
}

@Override
public Object getGroup(int groupPosition) {
	// TODO Auto-generated method stub
	return this._listDataHeader.get(groupPosition);
}

@Override
public int getGroupCount() {
	// TODO Auto-generated method stub
	return this._listDataHeader.size();
}

@Override
public long getGroupId(int groupPosition) {
	// TODO Auto-generated method stub
	return groupPosition;
}

@Override
public View getGroupView(int groupPosition, boolean isExpanded,
		View convertView, ViewGroup parent) 
{
	// TODO Auto-generated method stub
	String headerTitle = (String) getGroup(groupPosition);
	if (convertView == null) {
		LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = infalInflater.inflate(R.layout.reportstatusheaders, null);
	}

	TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
	lblListHeader.setTypeface(null, Typeface.BOLD);
	lblListHeader.setText(headerTitle);

	return convertView;
}

@Override
public boolean hasStableIds() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean isChildSelectable(int groupPosition, int childPosition) {
	// TODO Auto-generated method stub
	return false;
}

}
