package wpi.edu.tmathmeyer.bannerweb;

public class Meh {
	public static void main(String[] args){
		String usrName = "tjmeyer";
		String age = "in b4 apoc~";

		Toast.makeText(getApplicationContext(),
				"username:: " + usrName + "---Age::" + age,
				Toast.LENGTH_SHORT).show();

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost("http://192.168.1.40"); 
		String resp = null;

		try {
			// create data to POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					5);
			nameValuePairs.add(new BasicNameValuePair("UserName", usrName));
			nameValuePairs.add(new BasicNameValuePair("Age", age));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost); 

			resp = response.toString();

		} catch (ClientProtocolException e) {
			System.out.println("---" + e.getMessage());
		} catch (IOException e1) {
			System.out.println("---" + e1.getMessage());
		}

		Log.i(TAG, "RTT inside post-data: " + time_passed);
	}
}

