package com.example.snsmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.snsmap.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	/*
	 * XmlPP��php�̉�͂ɐ���
	 * ���̓��[�U�[�o�^�ƃ��O�C��������
	 */
	
	private InputFilter[] filter = {new MyFilter(8)};
	private InputFilter[] filterPass = {new MyFilter(16)};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
				
		EditText user = (EditText)findViewById(R.id.logUser);
		user.setFilters(this.filter);
		user = (EditText)findViewById(R.id.logPass);
		user.setFilters(this.filterPass);
		
		
		
	}
	
	public void onClick(View v){
		EditText user = (EditText)findViewById(R.id.logUser);
		TextView tv = (TextView)findViewById(R.id.logErrer);
		String errerMes=null;
		if(user.getText().length() < 6){
			errerMes = ("UserName��6�����ȏ����Ă�������\n");
			tv.setTextColor(Color.RED);
			//Intent i = new Intent(this,MainActivity.class);
			//startActivity(i);
		}
		
		user = (EditText)findViewById(R.id.logPass);
		if(user.getText().length() < 6){
			errerMes = errerMes.toString() + ("Password��6�����ȏ����Ă�������");
			tv.setTextColor(Color.RED);
		}
	
		if(null == errerMes){//�G���[�Ȃ�����http��
			user = (EditText)findViewById(R.id.logUser);
			String name = user.getText().toString();
			user = (EditText)findViewById(R.id.logPass);
			String pass = user.getText().toString();
			
			
			String url = "http://192.168.11.16/snsmap/main/testver.php";
			
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams httpParams = httpclient.getParams();
			httpParams.setParameter("http.useragent", "snsmap");//User-agent�̐ݒ�@�ʏ�̓u���E�U�Ƃ��[���Ƃ�
			HttpPost httppost = new HttpPost(url);
			
			//POST���M����f�[�^���i�[
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("user", name));
			nameValuePairs.add(new BasicNameValuePair("pass", pass));
			
			XmlPullParser xmlPP = Xml.newPullParser();
			
			
			try{
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
				HttpResponse respone = httpclient.execute(httppost);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				respone.getEntity().writeTo(byteArrayOutputStream);
				
				//�I����̉������擾�@�@XmlPullParser�ŉ�͂��K�v�ȏ�񂾂��i�[����
				if(respone.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					
					//byteArrya~�ɑS�Ă̕������i�[�����̂ł����StringReader�^�ɓ����
					StringReader strReader = new StringReader(byteArrayOutputStream.toString());
					xmlPP.setInput(strReader);
					
					Log.d("String",byteArrayOutputStream.toString());
					
					Integer eventType = xmlPP.getEventType();					
					while(eventType != XmlPullParser.END_DOCUMENT){
						if(eventType == XmlPullParser.START_DOCUMENT)
							Log.d("xml","Start document");
						else if(eventType == XmlPullParser.END_DOCUMENT)
							Log.d("xml","END Document");
						else if(eventType == XmlPullParser.START_TAG)
							Log.d("xml","Start Tag:"+xmlPP.getName()+":"+xmlPP.getText());
						else if(eventType == XmlPullParser.END_TAG)
							Log.d("xml","END Tag:"+xmlPP.getName());
						else if(eventType == XmlPullParser.TEXT)
							Log.d("xml","TEXT:"+xmlPP.getName());
						
						if(eventType == XmlPullParser.START_TAG && "body".equals(xmlPP.getName()))
							Log.d("xmlPP",xmlPP.nextText());//MySQL����User���̎擾�ɐ���
						eventType = xmlPP.next();
					}
					
					
					
					//String get = byteArrayOutputStream.toString();
					//Log.d("Login",get);//POST������ɍs���Ă��邱�Ƃ��m�F
					
					
				}else{
					Toast.makeText(this, "��������"+respone.getStatusLine().getStatusCode(), Toast.LENGTH_SHORT).show();
				}
				
			}catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, "LoginNG", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
