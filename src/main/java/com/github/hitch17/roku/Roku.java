package com.github.hitch17.roku;

import static com.github.hitch17.roku.Console.BLUE;
import static com.github.hitch17.roku.Console.NORMAL;
import static com.github.hitch17.roku.Console.PURPLE;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.Thread;

public class Roku {

	private static final String USER_AGENT = "Mozilla/5.0";
  private static final int DEFAULT_PORT = 8060;
  private static final int TYPE_DELAY = 500;

  private String rokuUrl = "";
	private int rokuPort = DEFAULT_PORT;

	public Roku(String url) {
	  this(url, DEFAULT_PORT);
	}
	
	public Roku(String URL, int port) {
		rokuPort = port;
		rokuUrl = "http://" + URL.replace("http://", "").replace("https://", "").replace("/", "") + ":" + port;
		if (URL.trim() != "") {
			System.out.println(BLUE + "[*] " + PURPLE + "Verifying connection..." + NORMAL);
			if (verify(rokuUrl))
				System.out.println(BLUE + "[*] " + PURPLE + "Roku connection verified." + NORMAL);
			else
				System.out.println(BLUE + "[*] " + PURPLE + "Roku connection could not be verified -- either there is not a Roku at this address or it is unresponsive. Commands may still be issued." + NORMAL);
		}
	}
	
	public boolean verify(String rokuURL) {
		try {
			URL url = new URL(rokuURL);
			InputStream is = url.openStream();
			int ptr = 0;
			StringBuffer buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
			String data = buffer.toString();
			if (data.toLowerCase().contains("roku"))
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean keypress(String key) {
		return sendPost(rokuUrl + "/keypress/" + key);
	}
	
	public boolean netflix() {
		return sendPost(rokuUrl + "/launch/12");
	}
	
	public boolean amazon() {
		return sendPost(rokuUrl + "/launch/13");
	}
	
	public boolean keyup(String key) {
		return sendPost(rokuUrl + "/keyup/" + key);
	}
	
	public boolean keydown(String key) {
		return sendPost(rokuUrl + "/keydown/" + key);
	}
	
	public boolean enterString(String str) {
		String[] s = str.toLowerCase().replace(" ", "+").split("");
		try {
			for (String t : s) {
				keypress("Lit_" + t);
				Thread.sleep(TYPE_DELAY);
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	private boolean sendPost(String url) {
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			String urlParameters = "";
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			// int responseCode = con.getResponseCode();  
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
