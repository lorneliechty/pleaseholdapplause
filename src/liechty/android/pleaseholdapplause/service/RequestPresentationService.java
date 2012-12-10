package liechty.android.pleaseholdapplause.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import liechty.android.pleaseholdapplause.provider.PHAProviderContract;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class RequestPresentationService extends IntentService {
	private static final String LOG_TAG = RequestPresentationService.class.getSimpleName(); 

	public RequestPresentationService() {
		super("PHA RequestPresentationService");
	}
	
	private class PresentationData {
		public String speaker;
		public String title;
		public String venue;
		public long start;
		public long end;
	}
	
	private PresentationData getPresentationDataFromIntent(Intent intent) {
		PresentationData ret = null;
		
		// Check to see if we have a data field pointing to a URI
		if (intent.getData() != null) {
			Cursor c = getContentResolver().query(intent.getData(), null, null, null, null);
			
			if (c != null && c.moveToFirst()) {
				ret = new PresentationData();
				ret.speaker = c.getString(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.SPEAKER));
				ret.title = c.getString(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.TITLE));
				ret.venue = c.getString(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.VENUE));
				ret.start = c.getLong(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.START_TIME_epoch_s));
				ret.end = c.getLong(c.getColumnIndex(PHAProviderContract.Presentation.CursorColumns.END_TIME_epoch_s));
			}
		}
		
		return ret;
	}
	
	private HttpResponse postPresentationToWeb(PresentationData presentation) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://10.0.2.2:8000/presentations/"	// use emulator's host machine's loopback (accessed via 10.0.2.2 magic IP)
				);
		
		httpPost.setHeader("content-type", "application/json");
		JSONObject data = new JSONObject();
		
		try {
			data.put("speaker", presentation.speaker);
			data.put("title", presentation.title);
			data.put("venue", presentation.venue);
			data.put("start", presentation.start);
			data.put("end", presentation.end);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Problems with JSON!", e);
		}
		
		HttpResponse response = null;
		
		try {
			StringEntity entity = new StringEntity(data.toString(), HTTP.UTF_8);
			httpPost.setEntity(entity);
			
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
	
	private ContentValues webPresentationResponseToContentValues(HttpResponse response) {
		ContentValues values = null;
		
		if (response != null) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String json = reader.readLine();
				JSONObject jPres = new JSONObject(json);
				
				values = new ContentValues();
				values.put(PHAProviderContract.Presentation.CursorColumns.SPEAKER, jPres.optString("speaker"));
				values.put(PHAProviderContract.Presentation.CursorColumns.TITLE, jPres.optString("title"));
				values.put(PHAProviderContract.Presentation.CursorColumns.VENUE, jPres.optString("venue"));
				values.put(PHAProviderContract.Presentation.CursorColumns.START_TIME_epoch_s, jPres.optLong("start", 0));
				values.put(PHAProviderContract.Presentation.CursorColumns.END_TIME_epoch_s, jPres.optLong("end", 0));
				values.put(PHAProviderContract.Presentation.CursorColumns.WEB_ID, jPres.optLong("web_id", 0));
				
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
		
		return values;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(LOG_TAG, "Intent Received!");
		
		Log.d(LOG_TAG, "Getting Presentation Data from Intent");
		PresentationData pres = getPresentationDataFromIntent(intent);
		
		Log.d(LOG_TAG, "Posting Presentation to Web Service");
		HttpResponse response = postPresentationToWeb(pres);
		
		if (response != null) {
			Log.d(LOG_TAG, "Processing Server response");
			ContentValues values = webPresentationResponseToContentValues(response);

			if (intent.getData() != null) {
				// Update the entry in our local database now that we have a response from the server completing it's data
				Log.d(LOG_TAG, "Updating ContentProvider with latest from Web Service");
				getContentResolver().update(intent.getData(), values, null, null);
			}
			else {
				// We might want to insert a new entry for the presentation we've requested in this situation, but that's
				// not the workflow that's been built up so far... so just ignore this case for now.
			}
		}
	}
}
