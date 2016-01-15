package com.dyn.server.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;

import com.dyn.server.reference.Reference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class PostBadge extends Thread {

	public static JsonElement jsonResponse;
	public static String response;
	private String UUID;
	private String secretKey = "";
	private String orgKey = "";
	private int badgeID;
	
	public PostBadge(int badgeId, String uuid, String secret, String key) {
		if (uuid != "")
			return;
		this.UUID = uuid;
		this.secretKey = secret;
		this.orgKey = key;
		this.badgeID = badgeId;
		setName("Server Mod HTTP Post");
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			HttpClient httpclient = HttpClients.createDefault();

			// decode the base64 encoded string
			byte[] decodedKey = this.secretKey.getBytes();
			// rebuild key using SecretKeySpec
			SecretKey theSecretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

			HmacSHA256Signer signer = new HmacSHA256Signer(null, null, theSecretKey.getEncoded());

			// Configure JSON token with signer and SystemClock
			JsonToken token = new JsonToken(signer);
			token.setExpiration(Instant.now().plusSeconds(300)); // 5 Minutes
			token.setParam("version", "v1");
			token.setSubject("issued_badge");
			JsonObject sPayload = new JsonObject();
			sPayload.addProperty("badge_id", this.badgeID);
			sPayload.addProperty("user_identifier_type", 2);
			sPayload.addProperty("recipient", this.UUID);
			token.addJsonObject("payload", sPayload);

			HttpPost postReq = new HttpPost("http://chicago.col-engine-staging.com/partner_organizations/api.json");

			postReq.setHeader("Accept", "application/json");
			postReq.setHeader("Authorization", "JWT token=" + this.orgKey);

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("jwt", token.serializeAndSign()));

			postReq.setEntity(new UrlEncodedFormEntity(pairs));

			// Execute and get the response.
			HttpResponse reply = httpclient.execute(postReq);
			HttpEntity entity = reply.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					response = "";
					int data = instream.read();
					while (data != -1) {
						char theChar = (char) data;
						response = response + theChar;
						data = instream.read();
					}
					JsonParser jParse = new JsonParser();
					jsonResponse = jParse.parse(response);
				} finally {
					instream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
