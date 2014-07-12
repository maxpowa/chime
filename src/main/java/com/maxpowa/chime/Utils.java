package com.maxpowa.chime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.Session;

import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.mojang.api.http.BasicHttpClient;
import com.mojang.api.http.HttpBody;
import com.mojang.api.http.HttpClient;
import com.mojang.api.http.HttpHeader;

public class Utils {
	
    private static HttpClient client;
	
	protected static Logger log = null;
    
    static {
    	client = BasicHttpClient.getInstance();
    }
    
	public static boolean isValid(Session session) {
		try {
            HttpBody body = new HttpBody("{\"accessToken\": \"" + session.getToken() + "\"}");
            List<HttpHeader> headers = new ArrayList<HttpHeader>();
            headers.add(new HttpHeader("Content-Type", "application/json"));
            
            JSONObject result = post(new URL("https://api.mojang.com/validate"), body, headers);
            Utils.log.info("Got validation response => "+result.toString());
            return result.names().length() > 0;
        } catch (Exception e) {
	        return false;
        }
	}
	
	public static JSONObject post(URL url, HttpBody body, List<HttpHeader> headers) throws IOException, JSONException {
        String response = client.post(url, body, headers);
        return new JSONObject(response);
    }
	
}
