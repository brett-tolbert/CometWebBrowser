package com.example.cometbrowser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Brett Tolbert
 * CIS 4340
 * Comet Web Browser application
 */

public class MainActivity extends Activity {
	
	//variables for UI elements
	EditText txtURL;
	Button browse;
	WebView browser;
	
//handler that will allow 	
private Handler htmlHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			
			browser.loadData(String.valueOf(msg.obj), "text/html", "UTF-8"); //load the HTML data into the webview
			//browser.getSettings().setJavaScriptEnabled(true);
			
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtURL = (EditText) findViewById(R.id.editTextInput);
		browser = (WebView) findViewById(R.id.webViewBrowser);
		browse = (Button) findViewById(R.id.buttonGo);
		
		browse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				/*
				 * Check that some text has been entered for the URL.
				 */
				
				if(txtURL.getText().toString().isEmpty()){
					Toast.makeText(getApplicationContext(), 
							"Enter the URL", Toast.LENGTH_LONG).show();
				}
				else{	
						Thread browserThread = new Thread(){
							@Override
							public void run(){
								try{
								
								String validURL = "http://";
								//String validPlusURL = "www.";
								String usableURL = "";
								
								//check that user has entered "http://" into the EditText
								if(!txtURL.getText().toString().startsWith(validURL)){
									usableURL = validURL + txtURL.getText().toString();
								}
								else{
									usableURL =  txtURL.getText().toString();
								}
									
								URL browserURL = new URL(usableURL);
								
								/*
								 * download the content of the specified URL to  a local string variable
								 */
								 BufferedReader in = 
										 new BufferedReader(new InputStreamReader(browserURL.openStream()));
								 
								 
								 String inputLine;
								 String outputLine = "";
								 
								 while ((inputLine = in.readLine()) != null){
									 outputLine = outputLine + inputLine + "\n";
									 //System.out.println("******* OUTPUTLINE: " + outputLine);
								 }
							
								 
								in.close();
								
								Message msg = htmlHandler.obtainMessage();
								msg.obj = outputLine;
								htmlHandler.sendMessage(msg); //pass message from working thread to the handler 
								
								
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};					
						//start running the thread
						browserThread.start();
				}
			}
		});
	}
	
	
/*
 * Below is unused code. 
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
