package com.dyn.server.http;

import com.google.gson.Gson;
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

public class GetBadge extends Thread {

	public static JsonElement jsonResponse;
	public static String response;
	private String UUID;
	private String secretKey = "";
	private String orgKey = "";

	public GetBadge(String uuid, String secret, String key) {
		if(uuid != "")
			return;
		this.UUID = uuid;
		this.secretKey = secret;
		this.orgKey = key;
		setName("Server Mod HTTP Get");
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
			token.setSubject("badges");
			JsonObject sPayload = new JsonObject();
			sPayload.addProperty("key", this.orgKey);
			token.addJsonObject("payload", sPayload);

			HttpGet getReq = new HttpGet("http://chicago.col-engine-staging.com/partner_organizations/api.json?jwt="
					+ token.serializeAndSign());
			getReq.setHeader("Accept", "application/json");
			getReq.setHeader("Authorization", "JWT token=" + this.orgKey);
			getReq.addHeader("jwt", token.serializeAndSign());
			
			// Execute and get the response.
			HttpResponse reply = httpclient.execute(getReq);
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
