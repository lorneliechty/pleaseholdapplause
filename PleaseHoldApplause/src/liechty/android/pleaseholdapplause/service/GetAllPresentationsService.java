package liechty.android.pleaseholdapplause.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

public class GetAllPresentationsService extends IntentService {
	
	private static final String LOG_TAG = GetAllPresentationsService.class.getSimpleName();
	
	public GetAllPresentationsService() {
		super("GetAllPresentationsService");
	}
	
	private HttpResponse getPresentationsFromWeb() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(
				"http://10.0.2.2:8000/presentations/"	// use emulator's host machine's loopback (accessed via 10.0.2.2 magic IP)
				);
		
		httpPost.setHeader("content-type", "application/json");
		HttpResponse response = null;
		
		try {
			response = httpClient.execute(httpPost);
		} 
		catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG, "Problems with UnsupportedEncodingException!", e);
		} 
		catch (ClientProtocolException e) {
			Log.e(LOG_TAG, "Problems with ClientProtocolException!", e);
		} 
		catch (IOException e) {
			Log.e(LOG_TAG, "Problems with IOException!", e);
		}

		return response;
	}
	
	private ArrayList<ContentValues> webPresentationResponseToContentValuesArray(HttpResponse response) {
		ArrayList<ContentValues> valuesArray = null;
		
		if (response != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String json = reader.readLine();
				JSONArray jaPres = new JSONArray(json);
				
				valuesArray = new ArrayList<ContentValues>();
				for (int i=0; i<jaPres.length(); i++) {
					JSONObject jPres = jaPres.getJSONObject(i);
					
					ContentValues values = new ContentValues();
					values.put(PHAProviderContract.Presentation.CursorColumns.SPEAKER, jPres.optString("speaker"));
					values.put(PHAProviderContract.Presentation.CursorColumns.TITLE, jPres.optString("title"));
					values.put(PHAProviderContract.Presentation.CursorColumns.VENUE, jPres.optString("venue"));
					values.put(PHAProviderContract.Presentation.CursorColumns.START_TIME_epoch_s, jPres.optLong("start", 0));
					values.put(PHAProviderContract.Presentation.CursorColumns.END_TIME_epoch_s, jPres.optLong("end", 0));
					values.put(PHAProviderContract.Presentation.CursorColumns.WEB_ID, jPres.optLong("web_id", 0));
					
					valuesArray.add(values);
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		
		return valuesArray;
	}
	
	private void upsertProviderWithLatestData(ArrayList<ContentValues> valuesArray) {
		// do this the super lazy way... delete everything then insert all new values :(
		// ... this sorta works for us because we use the web ID as our authority for most things...
		// but this will cause us problems if we're too dependent on static URIs in our GUI.
		getContentResolver().delete(PHAProviderContract.Presentation.getPresentations(), null, null);
		
		// Now insert the new values... the super lazy way... iteratively rather than using batch operations :(
		for (int i=0; i<valuesArray.size(); i++) {
			ContentValues values = valuesArray.get(i);
			getContentResolver().insert(PHAProviderContract.Presentation.getPresentations(), values);
		}
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		HttpResponse response = getPresentationsFromWeb();
		
		if (response != null) {
			Log.d(LOG_TAG, "Processing Server response");
			ArrayList<ContentValues> values = webPresentationResponseToContentValuesArray(response);
			
			upsertProviderWithLatestData(values);
		}
	}
}




