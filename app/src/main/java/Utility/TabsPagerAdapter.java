package Utility;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zevenapps.dailystatusupdater.Reports;
import com.zevenapps.dailystatusupdater.RunningLateStatus;
import com.zevenapps.dailystatusupdater.WorkFromStatus;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
	
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
  public Fragment getItem(int index) {
 
    	    switch (index)
        {
        case 0:
            // Running late fragment
            return new RunningLateStatus();
        case 1:
            // WFH fragment
        	return new WorkFromStatus();
        case 2:
            // Report fragment
        	return new Reports();
           
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}