package org.ird.android.epi.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


public class HTTPSender extends AsyncTask<Map<Object,Object>,Void,String>{

	private String url;
	private WeakReference<INetworkUser> caller;
	private String method;
	private static String TAG_HTTP_SENDER="HttpSender";
	public static final String METHOD_POST="post";
	public static final String METHOD_GET="get";
		
	//make the default constructor to force the intitiator to give the listener and method
	private HTTPSender()
	{		
	}
	
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method) throws UnsupportedHTTPMethodException
	{
		this.method = method;
		if(!((METHOD_GET.equalsIgnoreCase(method)
				||(METHOD_POST.equalsIgnoreCase(method)))))
		{
			throw new UnsupportedHTTPMethodException("Method not recognised: Should be either POST or GET");
		}
	}


	public HTTPSender(INetworkUser caller, String method, String url) throws UnsupportedHTTPMethodException
	{
		this.caller = new WeakReference<INetworkUser>(caller) ;
		this.url = url;
		this.method = method;
		
		if(!((METHOD_GET.equalsIgnoreCase(method)
				||(METHOD_POST.equalsIgnoreCase(method)))))
		{
			throw new UnsupportedHTTPMethodException("Method not recognised: Should be either POST or GET");
		}
	}
	
	public HTTPSender(INetworkUser caller) 
	{
		this.caller = new WeakReference<INetworkUser>(caller) ;
	}
	
	
	@Override
	protected String doInBackground(Map... params) 
	{		   
		try 
	    {
			//instantiates httpclient to make request
			DefaultHttpClient httpclient = new DefaultHttpClient();

			if(METHOD_GET.equalsIgnoreCase(this.method))
			{
				if(params.length>0)
				{
					Map<Object, Object> map = params[0];
					Iterator it = map.keySet().iterator();
					List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
					
					while(it.hasNext())
					{
						Object key = it.next();
						String value = (String)map.get(key);
						queryParams.add(new BasicNameValuePair((String) key, value));
					}
					
					String paramString  = URLEncodedUtils.format(queryParams, "utf-8");
					url  += "?" + paramString;
				}
				
				HttpGet httpGet = new HttpGet(url);
				StringBuilder builder = new StringBuilder();
			   
			      HttpResponse response = httpclient.execute(httpGet);
			      StatusLine statusLine = response.getStatusLine();
			      int statusCode = statusLine.getStatusCode();
			      
			      if (statusCode == 200) 
			      {
			        HttpEntity entity = response.getEntity();
			        InputStream content = entity.getContent();
			        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			        String line;
			        while ((line = reader.readLine()) != null)
			        {
			          builder.append(line);
			        }
			        return builder.toString();
			      }			   
			}
			
			else if(METHOD_POST.equalsIgnoreCase(this.method))
			{
				//url with the post data
				HttpPost httpost = new HttpPost(url);

				//convert parameters into JSON object
				JSONObject holder = getJsonObjectFromMap(params[0]);

				//passes the results to a string builder/entity
				StringEntity se = new StringEntity(holder.toString());

				//sets the post request as the resulting string
				httpost.setEntity(se);
				//sets a request header so the page receving the request
				//will know what to do with it
				httpost.setHeader("Accept", "text/plain");
				httpost.setHeader("Content-type", "application/json");

				//Handles what is returned from the page 
				ResponseHandler responseHandler = new BasicResponseHandler();
				/*HttpResponse response*/ String resp = httpclient.execute(httpost, responseHandler);
				return resp /*HTTPHelper._getResponseBody(response.getEntity())*/;

			}
			return null;

		} 
	    catch (IOException ex) 
	    {
	    	return handleException(ex);
		}
		catch(Exception ex)
		{ 
			return handleException(ex);
		}
	}
	
	private String handleException(Exception e)
	{
		e.printStackTrace();
		Log.e(TAG_HTTP_SENDER, e.getMessage());
		return null;
	}
	
	@Override
	protected void onPostExecute(String response)
	{		
		if (this.caller.get()!= null ) {
			Activity act  =  (Activity)this.caller.get();
			if(!act.isFinishing())
			{
				caller.get().responseRecieved(response);				
			}
		}
	}
	
	
	private JSONObject getJsonObjectFromMap(Map<Object,Object> params) throws JSONException {

	    //all the passed parameters from the post request
	    //iterator used to loop through all the parameters
	    //passed in the post request
	    
	    Iterator iter = params.entrySet().iterator();

	    //Stores JSON
	    JSONObject holder = new JSONObject();

	    try
		{
	    	String values[] = null;
			//While there is another entry
			while (iter.hasNext()) 
			{
			    //gets an entry in the params
			    Map.Entry pairs = (Map.Entry)iter.next();

			    //creates a key for Map
			    String key = (String)pairs.getKey();
			    
			    Object o = pairs.getValue();
			    
			    if(o instanceof String[])
				{			    	
			    	values = (String[])pairs.getValue();
			    	holder.put(key, values);
				}
				else if(o instanceof String)
				{
					String value = (String)pairs.getValue();
					holder.put(key, value);
				}
				else
				{
					holder.put(key, o);
				}			    
			}
		}
		catch (Exception e)
		{
			Log.e(TAG_HTTP_SENDER, e.getMessage());		
			
		}
	    return holder;
	}
	    public List <NameValuePair> converMaptoKeyValuePair(Map<String,Object> map)
	    {
		     // Your map goes here
		    ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		    Iterator iter = map.entrySet().iterator();
		    for (String key: map.keySet()) 
		    {
		        Object value = map.get(key);
		        if (value instanceof Collection) 
		        {
		            Collection<?> values = (Collection<?>) value;
		            for (Object v : values) 
		            {
		                // This will add a parameter for each value in the Collection/List
		                parameters.add(new BasicNameValuePair(key, v == null ? null : String.valueOf(v)));
		            }
		        } 
		        else 
		        {
		            parameters.add(new BasicNameValuePair(key, value == null ? null : String.valueOf(value)));
		        }
		        
		    }
		    return parameters;
	    }
	
	public class UnsupportedHTTPMethodException extends Exception
	{
		public UnsupportedHTTPMethodException ()
		{
			super();
		}
		
		public UnsupportedHTTPMethodException (String message)
		{
			super(message);
		}
	}
	
}


