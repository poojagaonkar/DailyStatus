package Utility;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.CookieManager;

import com.zevenapps.dailystatusupdater.LoginActivity;

public class StorageHelper
{
	SharedPreferences pref;
    
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    public static final String PREF_NAME = "ZevenUserDetails";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // AccessTOken (make variable public to access from outside)
    public static final String ACCESS_TOKEN = "accesstoken";
    public static final String EXPIRES_IN = "expires_in";
    public static final String EXPIRES_ON = "expires_on";
    public static final String ID_TOKEN = "id_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String RESOURCE = "resource";
    public static final String SCOPE = "scope";
    public static final String TOKEN_TYPE = "token_type";
    public static final String USER_NAME = "username";
    public static final String AUTH_CODE = "auth_code";
    public static final String NAME = "name";
    
 // Constructor
    public StorageHelper(Context context)
    {
		// TODO Auto-generated constructor
    
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String mAuthCode, String mAccessToken,
			String mExpiresIn, String mExpiresOn, String mIdToken,
			String mRefreshToken, String mResource, String mScope,
			String mTokenType, String userName, String firstname) 
	{
		// TODO Auto-generated method stub
    	editor.clear();
    	editor.putString(ACCESS_TOKEN, mAccessToken);
    	editor.putString(EXPIRES_IN, mExpiresIn);
    	editor.putString(EXPIRES_ON, mExpiresOn);
    	editor.putString(ID_TOKEN, mIdToken);
    	editor.putString(REFRESH_TOKEN, mRefreshToken);
    	editor.putString(RESOURCE, mResource);
    	editor.putString(SCOPE, mScope);
    	editor.putString(TOKEN_TYPE, mTokenType);
    	editor.putString(USER_NAME, userName);
    	editor.putString(NAME, firstname);
    	editor.putString(AUTH_CODE, mAuthCode);
    	editor.putBoolean(IS_LOGIN, true);
        // commit changes
        editor.commit();
	}
 
    
    //Get user details
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
      
        user.put(ACCESS_TOKEN, pref.getString(ACCESS_TOKEN, null));
        user.put(EXPIRES_IN, pref.getString(EXPIRES_IN, null));
        user.put(EXPIRES_ON, pref.getString(EXPIRES_ON, null));
        user.put(ID_TOKEN, pref.getString(ID_TOKEN, null));
        user.put(REFRESH_TOKEN, pref.getString(REFRESH_TOKEN, null));
        user.put(RESOURCE, pref.getString(RESOURCE, null));
        user.put(SCOPE, pref.getString(SCOPE, null));
        user.put(TOKEN_TYPE, pref.getString(TOKEN_TYPE, null));
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        user.put(NAME, pref.getString(NAME, null));
        user.put(AUTH_CODE, pref.getString(AUTH_CODE, null));
        
        return user;
    }
    
    /**
     * 
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin()
    {
        // Check login status
        if(!this.isLoggedIn())
        {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
              _context.startActivity(i);
        }
         
    }
 // This function clears all session data and redirect the user to LoginActivity
    /**
         * Clear session details
         * */
        public void logoutUser()
        {
            // Clearing all data from Shared Preferences
           editor.clear();
        	
        	
        	editor.putBoolean(IS_LOGIN, false);
            editor.commit();
            
            
        
            // After logout redirect user to Login Activity
            CookieManager.getInstance().removeAllCookie();
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
            
        }
        
        public boolean isLoggedIn()
        {
            return pref.getBoolean(IS_LOGIN, false);
        }

		

    

}
