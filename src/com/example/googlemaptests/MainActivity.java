package com.example.googlemaptests;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.R.array;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.support.v4.app.NavUtils;

public class MainActivity extends MapActivity implements LocationListener {//googleMap���g���ۂ�MapActivity���p������

	/*
	 * Longitude���o�x
	 * Latitude���ܓx
	 * 
	 * Overlay���g��MAP�ɉ摜����������
	 * �d�l�ύX���������炵���Â��������ɒ���
	 * 
	 * 
	 * ���ݏ����ʒu���l�F�X�V����15��
	 * 
	 * distance(GeoP,GeoP)�œ�_�̋��������߂���炵��
	 * 
	 * LocationManeger.removeUpdates(this);�ňʒu��񃊃N�̒�~
	 * 
	 * 
	 */


	/*������
	 * �A�C�R���̕ύX
	 * ���O�̈ꗗ���t���ƁH
	 * ���P�[�V�����`�F���W������������
	 * case�̏��O�H
	 * �|�C���g�̏�����DB���������Ă錏
	 */



	private MapView map;
	//private ArrayList<Location> oldLocation = new ArrayList<Location>();
	private ArrayList<GeoPoint> gp = new ArrayList<GeoPoint>();

	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();

		DBHepler dbh = new DBHepler(this);
		SQLiteDatabase db = dbh.getReadableDatabase();

		dbh.dbClear(db);//db���N���A���鎖�œ�d�o�^��h��
		dbh.databaseInsert(db, this.gp);

		dbh.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Date date = new Date(System.currentTimeMillis());
		//2012-09-10�ƋA���Ă���
		Log.d("test",date.toString());


		map = (MapView)findViewById(R.id.mapview);
		MapController c = map.getController();



		c.setZoom(15);//�Y�[���l�̐ݒ�
		//c.setCenter(new GeoPoint(35455281,139629711));//���ݒn�̐ݒ�@������GeoPoint�@���̏ꍇ�͓����ɂȂ�

		/*�񐄏��̂����@.setBuilt�`���g����
        ZoomControls zc = (ZoomControls)map.getZoomControls();
        zc.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        zc.setGravity(Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);//��ʉ��ɕ\�������Y�[���E�B�W�F�b�g�̐ݒu�֌W
        map.addView(zc);
		 */

		Button btn = new Button(this);
		btn.setText("���ݒn�̎擾");
		btn.setTextSize(10);

		/*�����C���i�[�N���X�̏ꍇ
		 * 
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u

				LocationManager locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
				Location myLocate = locationManager.getLastKnownLocation("gps");
				if(myLocate!=null){
					GeoPoint gp = new GeoPoint((int)(myLocate.getLatitude()*1e6), (int)(myLocate.getLongitude()*1e6));
					c.animateTo(gp);
				}
			}
		});*/

		
		/*
		 * MapController������Ɏ��{�^���̍쐬
		 * �{�^�������̂��̓A�h���X���ꏏ�Ȃ̂�
		 * Activity�̂����ω����Ă��ǂ�������
		 * 
		 * new OnClickListener() {}�̌`�ł͖����C���i�[�N���X�ƂȂ胍�[�J���ϐ���n���Ȃ�
		 * �f�t�H�I���g�R���X�g���N�^�����Ȃ��̂�
		 * �R���X�g���N�^��ʂ��Ă̈��n�����o���Ȃ�
		 * 
		 * �V���ɗL��(?)�N���X�����OnClickListener���C���^�[�t�F�[�X�Ōp����
		 * �������������f�t�H���g�R���X�g���N�^�����@class��onClickButton
		 * class���������
		 * 
		 * btn.setOnClickListener (new onClickButton(c));�Ŏ���
		 * 
		 */
		class onClickButton implements OnClickListener{

			MapController c;
			public onClickButton(MapController c){
				this.c = c;
			}

			
			
			@Override
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u

				
				/*locationManager.getLastKnownLocation���g���Č��ݒn���擾
				location�ɓ����geopoint�����o��
				�擾�ł��Ȃ��ꍇ�̓G���[��������̂ő΍����邱��
				*/
				Log.d("btn","�����ꂽ��");
				LocationManager locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
				Location myLocate = locationManager.getLastKnownLocation("gps");
				if(myLocate!=null){
					GeoPoint gp = new GeoPoint((int)(myLocate.getLatitude()*1e6), (int)(myLocate.getLongitude()*1e6));
					this.c.animateTo(gp);
				}else{
					Toast.makeText(MainActivity.this, "���ݒn���擾�ł��܂���", Toast.LENGTH_LONG).show();
				}
			}
		}

		btn.setOnClickListener (new onClickButton(c));



		btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		btn.setGravity(Gravity.TOP+Gravity.CENTER_HORIZONTAL);
		map.addView(btn);
		map.setBuiltInZoomControls(true);

		
		
		//LocationManager�̐ݒ�@GPS�X�V���Ԃ�Preferences����ǂݍ��ށ@�����ꍇ��15����
		this.setRequestLocation(readPreferences());
		Log.d("prefs",String.valueOf(this.readPreferences()));
		
		


		/*�����ʒu�𓌋���
        GeoPoint tokyo = new GeoPoint(35681396, 139766049);
        c.animateTo(tokyo);
		 */

		try{
			DBHepler dbh = new DBHepler(this);
			SQLiteDatabase db = dbh.getReadableDatabase();
			boolean isEof;

			SimpleDateFormat sdf1 = new SimpleDateFormat("'yyMMdd'");
			Cursor cursor = db.query(sdf1.format(date), new String[]{"Longitude","Latitude"},null, null, null, null, null);

			isEof = cursor.moveToFirst();
			GeoPoint setGp;

			while(isEof){
				Log.d("GP",String.valueOf(this.gp.size()));

				setGp = new GeoPoint(cursor.getInt(1),cursor.getInt(0));
				this.gp.add(setGp);
				isEof = cursor.moveToNext();
			}

			cursor.close();
			db.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		Log.d("GP","DB�ǂݍ��݊J�n");
		for(GeoPoint g:gp){
			Log.d("GP",String.valueOf(g.getLongitudeE6())+":"+String.valueOf(g.getLatitudeE6()));
		}
		Log.d("GP","DB�ǂݍ��ݏI��");



		/*
		 * location�ňܓx�o�x���擾����΋N�����̌��ݒn���擾�\
		 * 
        //pin�ɉ摜��ǂݍ���
        Drawable pin = getResources().getDrawable(R.drawable.maru);
        //pin�������ɂ���PinItemizedOverlay�̃R���X�g���N�^�ɓn��
        PinItemizedOverlay pinOverlay = new PinItemizedOverlay(pin);
        //map.getOverlays().add()���\�b�h��pinOverlay��`��H�̏����H
        map.getOverlays().add(pinOverlay);

        //�����Ƒ���GeoPoint��ݒ肵Overlay�ɕ`��

        GeoPoint tokyo = new GeoPoint(35681396, 139766049);
        GeoPoint osaka = new GeoPoint(34701895, 135494975);
        pinOverlay.addPoint(tokyo);
        pinOverlay.addPoint(osaka);
		 */


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d("GpSize",String.valueOf(this.gp.size()));
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



	//���j���[����I������ƌ��ݒn�Ɉړ�����
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		if(item.getItemId() == R.id.menu_settings){//�|�C���g�̏���

			this.pinClearS();
			DBHepler dbh = new DBHepler(this);
			SQLiteDatabase db = dbh.getReadableDatabase();
			dbh.dbClear(db);
			dbh.close();

		}else if(item.getItemId() == R.id.menu_settime){//�X�V���Ԃ̐ݒ�
			Intent i = new Intent(this,TimeList.class);
			this.startActivityForResult(i, 0);
		}else if(item.getItemId() == R.id.menu_path){//�O�Ղ̕\��

			LineOverlay lineOverlay = new LineOverlay(gp);
			map.getOverlays().add(lineOverlay);
			map.invalidate();

			Location location = new Location("now");
			Location oldLocation = new Location("old");


			//f�Ɉړ��������v�Z���đ��
			Float f=(float) 0;
			for(int i=0;i<gp.size()-1;i++){
				location.setLatitude(gp.get(i+1).getLatitudeE6()/1E6);
				location.setLatitude(gp.get(i+1).getLongitudeE6()/1E6);

				oldLocation.setLatitude(gp.get(i).getLatitudeE6()/1E6);
				oldLocation.setLatitude(gp.get(i).getLongitudeE6()/1E6);


				f+=location.distanceTo(oldLocation);

				Log.d("test",String.valueOf(f));
			}

			/*
			location.setLatitude(gp.get(0).getLatitudeE6()/1E6);
			location.setLatitude(gp.get(0).getLongitudeE6()/1E6);

			oldLocation.setLatitude(gp.get(1).getLatitudeE6()/1E6);
			oldLocation.setLatitude(gp.get(1).getLongitudeE6()/1E6);*/

			Toast totas = Toast.makeText(this, String.valueOf(f), 1000);
			totas.show();

			//	        for(Location l:this.oldLocation)
			//	        		f += location.distanceTo(l);






		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onActivityResult(requestCode, resultCode, data);


		/*�Ăяo����̃A�N�e�B�r�e�B��setResult(RESULT_OK, i);�����s�����
		 * resultCode�Ɍ��ʂ�����RESULT_OK�����@RESULT_CANCELED�L�����Z���Ȃ�
		 * resultCode�͐��ۂ̕���Ɏg����?
		 * 
		 * resultCode��requestCode�̎g���������C�}�C�`�s��
		 * 
		 * data�ɂ͌Ăяo����̃A�N�e�B�ŕێ����Ă��C���e���g������Γ���
		 * Intent.put(key,value);�ŕۑ�
		 *  data.getStringExtra(name)�ȂǂŎ��o��
		 *  
		 * */
		if(resultCode == RESULT_OK){
			if(requestCode == 0){

				this.setRequestLocation(data.getLongExtra("time", 0));
				Log.d("time",String.valueOf(data.getLongExtra("time", 0)));
				this.writePreferences(data.getLongExtra("time", this.readPreferences()));
			}

		}
	}

	//�Ƃ肠�����K�{
	@Override
	protected boolean isRouteDisplayed() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}


	/*���ݒn���ړ������Ƃ��Ɏ��s�����
	 * 
	 * �G�~������KML�ŏu�Ԉړ����Ă����@���@�ł͓������тɎ��s�����̂œd�r�����������
	 * 
	 * GeoPoint�𗭂ߍ����
	 * animateTo(GeoPoint)���n���h���[�X���b�h�H�Œ���I�Ɏ��s�����
	 * �O�Ղ�`���邩��
	 * �A�������Ō��Ԃ����Ȃ̂Ő��m�ł͂Ȃ��i���H��˂��؂�j
	 * 
	 * 60���ɐݒ肵���̂ɌĂ΂�Ă邱�Ɗm�F
	 * �ړ����ĂȂ��̂Ɂi����
	 * �X���[�v���ł�draw���Ă΂�Ă邱�Ɗm�F
	 * 45�b�����H
	 * 
	 */
	@Override
	public void onLocationChanged(Location location) {

		/*location �̎�ȃ��\�b�h
		Log.v("----------", "----------");
        Log.v("Latitude", String.valueOf(location.getLatitude()));�ܓx
        Log.v("Longitude", String.valueOf(location.getLongitude()));�o�x
        Log.v("Accuracy", String.valueOf(location.getAccuracy()));
        Log.v("Altitude", String.valueOf(location.getAltitude()));���x�i�C�������[�g���j
        Log.v("Time", String.valueOf(location.getTime()));���肵������
        Log.v("Speed", String.valueOf(location.getSpeed()));�ړ����x�̎擾m/s�i�Ԃɏ���Ă�Ƃ��Ȃǁj
        Log.v("Bearing", String.valueOf(location.getBearing()));���ʁ@�k��0�x�Ƃ݂Ȃ��@���v����+90�x
		 */



		// ���ݒn���擾&�ۑ�
		GeoPoint gp = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));

		// �R���g���[���[���g�p���āA���ݒn�Ƀ}�b�v���ړ�
		MapController mapCtrl = map.getController();
		mapCtrl.animateTo(gp);

		//pin�ɉ摜��ǂݍ���
		Drawable pin = getResources().getDrawable(R.drawable.icon04);
		//pin�������ɂ���PinItemizedOverlay�̃R���X�g���N�^�ɓn��
		PinItemizedOverlay pinOverlay = new PinItemizedOverlay(pin);

		/*map.getOverlays().add()���\�b�h��MapView�̃I�[�o�[���C��pinOverlay��`��@
        pinOverlay��List��GeoPoint��ێ����Ă���@���̕ێ����Ă���|�C���g�S�Ă�`�悷��
        pinOverlay�̓��[�J���Ȃ̂����烊�X�g�ŕێ�����Ӗ��͂���̂�?
		 */        
		map.getOverlays().add(pinOverlay);
		//addPoint(gp)���\�b�h��gp�̈ʒu�ɕ`��
		pinOverlay.addPoint(gp);

		/*�ړ��O�̈ʒu��oldLocation�ɕێ����Ă������ݒn�Ƃ̒������������[�g���ŋ��߂�@ ����̂ݑO��̈ʒu���Ȃ��̂�!=null�ŉ��
        GeoPoint�����ł͋��߂��Ȃ��̂��H .distanceTo�̑��肪����΁E�E
		 


		if(this.oldLocation.isEmpty())
			Log.d("test","��ł�");
		if(this.oldLocation.size() == 4){
			Float f=(float) 0;
			for(Location l:this.oldLocation)
				f += location.distanceTo(l);
		}

		//�C���X�^���X�ϐ���Location��GeoPoint�����ꂼ��ۑ�
		this.oldLocation.add(location);
		*/
		this.gp.add(gp);

		Log.d("location","---"+String.valueOf(this.gp.size()));

	}

	//Location����������Ă��邩�HGPS�@�\��������Location�������Ȃ�(?)
	public void updateDisplay(Location location){
		if(location==null){
			Log.e("HelloLocation","location is null");
			return;
		}else
			Log.e("HelloLocation","location is not null");
	}

	//����ɑ����Ă��l�B
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}


	//Overlay�N���A���\�b�h�@map.invalidate();��Overlay�̍ĕ`���Y�ꂸ��
	public void pinClearS(){

		map.getOverlays().clear();
		map.invalidate();
	}

	public void setRequestLocation(long i){
		//LocationManager�̎擾 �ʒu���T�[�r�X�擾
		LocationManager location = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		/*�ʒu��񂪕ω������Ƃ��̃��X�i�̓o�^�@requestLocationUpdates(�T�[�r�XGPS/3G/Wifi,�ʒu���̍X�V�Ԋums,�ʒu���̍Œ�X�V����m,�o�^���郊�X�i);
		�b�����Z�b�g�����������Ɠ{���̐����ōX�V���ꂽ�̂Ł@������1m�ɐݒ�
		*/
		location.requestLocationUpdates(LocationManager.GPS_PROVIDER, i, 1,this);
		location.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, i, 1,this);

	}
	
	public long readPreferences(){
		SharedPreferences prefs = getSharedPreferences("Maps", MODE_PRIVATE);
		return prefs.getLong("time", LocationMinute.MINUTE15.getTime());
		
	}
	
	public void writePreferences(long l){
		SharedPreferences prefs = getSharedPreferences("Maps", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("time", l);
		editor.commit();
	}
}
