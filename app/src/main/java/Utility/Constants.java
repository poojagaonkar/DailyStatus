package Utility;

public final class Constants 
{
	public static final int AuthenticateRequestCode = 111;
	public static final int RESULT_CODE_AUTHENTICATE = 900;
	public static final String EXTRA_CODE_URL = "ExtraCodeUrl";

	/*
	 * Constants initial

	public static final String LOGIN_URL = "https://login.windows.net/";
	public static final String TENANT = "zevenseas1.onmicrosoft.com";
	public static final String CLIENT_ID = "3700faae-7673-422b-9dfc-8b603b788cc8";
	public static final String REDIRECT_URL = "http://dailystatusservice/client";
	public static final String RESOURCE = "http://dailystatusservice/cloud";
	public static final String SERVICE_ENDPOINT = "api/status";

	// Endpoints
	public static final String AddStatusEndpoint ="https://dailystatusdev.azurewebsites.net/api/Status";
	public static final String StatusQueryendpoint = "https://dailystatusdev.azurewebsites.net/odata/OStatus?"; */



	/*
	<add key="ida:ClientId" value="f27fa1dd-cb7f-49be-b695-be6481d3e326" />
	    <add key="ida:AppIdUri" value="http://rc-services/dailystatus/cloud/prod" />
	    <add key="ida:Tenant" value="RapidCircle.com" />
	    <add key="ida:AADInstance" value="https://login.windows.net/{0}" />
	    <add key="ida:DailyStatusResourceUri" value="https://dailystatus.azurewebsites.net/" />
	    <add key="ida:RedirectUri" value="http://rc-client/dailystatus/cloud/prod" />*/
	/*
	 * Rapid circle constants
	 */


// Dev constants
	public static final String LOGIN_URL = "https://login.windows.net/";
	public static final String TENANT = "RapidCircle.com";
	public static final String CLIENT_ID ="fb626eb8-59f5-43c3-8b01-1b4887b00566"; 
	public static final String REDIRECT_URL = "http://rc-client/dailystatus/cloud/dev";
	public static final String RESOURCE = "http://rc-services/dailystatus/cloud/dev";
	public static final String SERVICE_ENDPOINT = "api/status";

	// Endpoints
	public static final String AddStatusEndpoint ="https://dailystatusdev.azurewebsites.net/odata/Statuses";
	public static final String StatusQueryendpoint = "https://dailystatusdev.azurewebsites.net/odata/Statuses";
	public static final String Notifications = "https://dailystatusdev.azurewebsites.net/Notifications";
	

	// Production Endpoints
/*	 public static final String LOGIN_URL = "https://login.windows.net/";
	public static final String TENANT = "RapidCircle.com";
	public static final String CLIENT_ID ="f27fa1dd-cb7f-49be-b695-be6481d3e326"; 
	public static final String REDIRECT_URL = "http://rc-client/dailystatus/cloud/prod";
	public static final String RESOURCE = "http://rc-services/dailystatus/cloud/prod";
	public static final String SERVICE_ENDPOINT = "api/status";

	// Endpoints
	public static final String AddStatusEndpoint ="https://dailystatus.azurewebsites.net/odata/Statuses";
	public static final String StatusQueryendpoint = "https://dailystatus.azurewebsites.net/odata/Statuses";
	public static final String Notifications = "https://dailystatus.azurewebsites.net/Notifications"; */


}
