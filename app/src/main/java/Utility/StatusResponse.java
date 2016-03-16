package Utility;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusResponse 
{


	private String createdBy, comment, location, slot, startTime, lunch;

	
	
	


	public StatusResponse(String createdBy, String comment, String location,
			String slot, String startTime, String lunch) {
		super();
		this.createdBy = createdBy;
		this.comment = comment;
		this.location = location;
		this.slot = slot;
		this.startTime = startTime;
		this.lunch  = lunch;
	}


	public StatusResponse() {
		// TODO Auto-generated constructor stub
	}


	public String getLunch() {
		return lunch;
	}

	public void setLunch(String lunch) {
		this.lunch = lunch;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	


}
