package com.signle.action;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import com.google.common.io.CharStreams;
import com.signle.util.EasySSLSocketFactory;
import com.signle.util.SSLUtilities;
import com.signle.util.URLSigner;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

@SuppressWarnings("deprecation")
@UrlBinding("/signle/findnearbyplaces/")
public class FindNearbyPlacesActionBean extends SignleActionBean {
	
	private final String APPLICATION_KEY = "AIzaSyCemBwJ6E_hjDOu66tJSCBi9ZW6SKFWMG0";
	private final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/search/json/?";
	private final String AMPERSAND = "&";
	
	@Validate(required=true)
	private Float longitude;
	
	@Validate(required=true)
	private Float latitude;
	
	@Validate(required=true)
	private Integer radius;
	
	@Validate(required=false, trim=true)
	private String name = null;

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
	public Integer getRadius() {
		return this.radius;
	}
	
	public void setRadius(Integer radius) {
		this.radius = radius;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@DefaultHandler
	public Resolution getNearbyPlaces() throws Exception {
		InputStreamReader reader = null;
		try {
			SSLUtilities.trustAllHostnames();
			SSLUtilities.trustAllHttpsCertificates();
	
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		
		HttpParams params2 = new BasicHttpParams();
		params2.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params2.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params2.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params2, HttpVersion.HTTP_1_1);
		 
		ClientConnectionManager cm = new SingleClientConnManager(params2, schemeRegistry);
		DefaultHttpClient httpclient = new DefaultHttpClient(cm, params2);
		
	//	Protocol.registerProtocol("https", 
		//		new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
			//	HttpClient httpclient = new HttpClient();
			//	GetMethod httpget = new GetMethod("https://www.whatever.com/");
		
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", APPLICATION_KEY));
		params.add(new BasicNameValuePair("location", this.latitude + "," + this.longitude));
		params.add(new BasicNameValuePair("radius", this.radius.toString()));
		params.add(new BasicNameValuePair("sensor", "false"));
		
		if (this.name != null) {
			params.add(new BasicNameValuePair("name", this.name));
		}
		
		params.add(new BasicNameValuePair("types", "casino|night_club|restaurant|bar"));
		
		
	//	Map paramNameToValue = new HashMap(); // parameter name to value map

		
		StringBuffer sb = new StringBuffer("");
		for (NameValuePair nvp: params) {
			sb.append(nvp.getName() + "=" + URLEncoder.encode(nvp.getValue(), "UTF-8"));
			sb.append("&");			
		}
		
		//URLEncoder.encode(arg0, arg1)
		String urlString = "https://maps.googleapis.com/maps/api/place/search/json?" + sb.toString();
		System.out.println("https://maps.googleapis.com/maps/api/place/search/json?" + sb.toString());
	    // Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);

		
		
	//	java.net.URI uri = URIUtils.createURI("https", "maps.googleapis.com", 443, "/maps/api/place/search/json", 
		//	    URLEncodedUtils.format(params, "UTF-8"), null);
	
	//	java.net.URI uri = URIUtils.createURI("https", "maps.googleapis.com", 443, "/maps/api/place/search/json", 
		//	    sb.toString(), null);
	
		//UrlUtils.buildFullRequestUrl(scheme, serverName, serverPort, requestURI, queryString)
		//  GetMethod hehe = new GetMethod("https://maps.googleapis.com:443/maps/api/place/search/json?key=AIzaSyCemBwJ6E_hjDOu66tJSCBi9ZW6SKFWMG0&location=-33.867054,151.19574&radius=25&sensor=false&"); 

		//URL url = new URL("https://maps.googleapis.com:443/maps/api/place/search/json?key=AIzaSyCemBwJ6E_hjDOu66tJSCBi9ZW6SKFWMG0&location=-33.867054,151.19574&radius=25&sensor=true");
		URL url = new URL(urlString);
		HttpURLConnection connection = null;
		connection = (HttpsURLConnection)url.openConnection();
		((HttpsURLConnection) connection).setHostnameVerifier(new MyHostnameVerifier());
		connection.setRequestProperty("Content-Type", "text/plain; charset=\"utf8\"");
		connection.setRequestMethod("GET");
		
		//System.out.println(connection.getResponseCode());
		
	//	HttpGet getPlaceRequest = new HttpGet(uri);
        reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
        String jsonResponse1 = CharStreams.toString(reader);
       // System.out.println(jsonResponse1);
       // System.out.println("YUPPPP");
		//HttpGet hehe = new HttpGet("https://maps.googleapis.com:443/maps/api/place/search/json?key=AIzaSyCemBwJ6E_hjDOu66tJSCBi9ZW6SKFWMG0&location=-33.867054,151.19574&radius=25&sensor=false&");
		
		 // Convert the string to a URL so we can parse it
		  //  URL url = new URL(urlString);

		  //  URLSigner signer = new URLSigner(APPLICATION_KEY);
		  //  String request = signer.signRequest(url.getPath(),url.getQuery());

		  //  System.out.println("Signed URL :" + url.getProtocol() + "://" + url.getHost() + request);
		
		//getPlaceRequest.getParams().setParameter("key", APPLICATION_KEY);
		//getPlaceRequest.getParams().setParameter("location", this.latitude + "," + this.longitude);
		//getPlaceRequest.getParams().setParameter("radius", RADIUS);
		//getPlaceRequest.getParams().setParameter("sensor", false);
		
	//	System.out.println(hehe.getURI());
		
		//System.out.println(uri);
		//   HttpResponse response = httpclient.execute(getPlaceRequest);
	          //  StringEntity responseEntity = (StringEntity) response.getEntity()    ;
		 //  reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
	       //       String jsonResponse =  CharStreams.toString(reader);
	   	//	   System.out.println(jsonResponse);
	   		return new StreamingResolution("", jsonResponse1);

		//return null;
		}  catch (HttpResponseException e) {
		    e.printStackTrace();
		  }finally {
			  
			reader.close();
		}
		return null;
	}

	public static class MyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
		// verification of hostname is switched off
		return true;
		}
		}
	
}
