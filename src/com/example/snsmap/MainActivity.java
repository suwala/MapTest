package com.example.snsmap;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import com.example.snsmap.R;
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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.support.v4.app.NavUtils;

//�A�C�R���ꗗ�͊����@���̃X�e�b�v��


public class MainActivity extends MapActivity implements LocationListener{//googleMap���g���ۂ�MapActivity���p������

	/*
	 * Longitude���o�x
	 * Latitude���ܓx
	 * 
	 * Overlay���g��MAP�ɉ摜����������
	 * �d�l�ύX���������炵���Â��������ɒ���
	 * 
	 * 
	 * ���ݏ����ʒu���l�F�X�V����3��
	 * 
	 * distance(GeoP,GeoP)�œ�_�̋��������߂���炵��
	 * 
	 * LocationManeger.removeUpdates(this);�ňʒu��񃊃N�̒�~
	 * 
	 * Location(����)�@�̈����͉��Ɏg���̂�
	 * 
	 * protected Dialog onCreateDialog(int id)���g����
	 * �_�C�A���O�Ƀ��C�A�E�g�𗬂����ނ��Ƃɐ���
	 * �O���b�h�r���[�𗬂�����ŃA�C�R�����X�g�����邩��
	 * 
	 * #getView���ł�IconListData[] list = IconListData.values();
	 * list[posion]�Ȃ񂩂͂悭�g���`���ۂ��̂Ŋo���Ă�����
	 * 
	 */


	/*������
	 *
	 * case�̏��O?
	 */

//-----Overlay��2�����ԂɂȂ��Ă�̂œ��ꂳ���� OverlayPlus�ɓ��ꒆ
//�A�C�R���̒ǉ����@���ǂ����邩

	private MapView map;
	//private ArrayList<Location> oldLocation = new ArrayList<Location>();
	//private PinItemizedOverlay itemovarlay;
	//private ArrayList<GeoPoint> gp = new ArrayList<GeoPoint>();
	public static int zoom = -1;
	private LocationManager lastLocation;
	private ArrayList<OverlayItems> items=new ArrayList<OverlayItems>();
	private GeoPoint nowGp;
	private Button btn;
	private static Calendar calendar = Calendar.getInstance();//���̎��_�ł��̓��̓��t���Z�b�g�����
	private SimpleDateFormat sdf = new SimpleDateFormat("'D'yyMMdd");
	private Date oldDate;
	private Boolean nowFlag=false; 
	private int icon = R.drawable.icon01;//�g�p����A�C�R��
	
	@Override
	protected void onStop() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStop();
		zoom = map.getZoomLevel();
		
		
		/*
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
		DBHepler dbh = new DBHepler(this,sdf1.format(date));
		SQLiteDatabase db = dbh.getReadableDatabase();
		dbh.dbClear(db);
		*/
		
		

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("DRAW",calendar.getTime().toString());
		map = (MapView)findViewById(R.id.mapview);
		MapController c = map.getController();
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		
		//zoom��-1�̎��@�T�[�r�X���N������
		if(zoom == -1){
			this.isService();
		}

		Log.d("oncre",(new Date(System.currentTimeMillis()).toString()));

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
		
		DBHepler dbh = new DBHepler(this,sdf1.format(date));
		SQLiteDatabase db = dbh.getReadableDatabase();
		boolean isEof;
		
		Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'",null);
		String str;
		sdf1=new SimpleDateFormat("yyMMdd");
		isEof = cursor.moveToFirst();
		
		
		while(isEof){//�e�[�u�������A���Ă���
			try{
			str = cursor.getString(1).replace("D", "");
			//str = str.replace("D", "");
			this.oldDate = sdf1.parse(str);
			Log.d("onCre",this.oldDate.toString());
			break;
			}catch (Exception e) {
				// TODO: handle exception
				Log.d("onCre","Date�^�ɕϊ��ł��܂���ł���");
			}
			isEof = cursor.moveToNext();
		}
		
		
		
		dbh.close();
		

		if(zoom == -1)
			c.setZoom(15);//�Y�[���l�̐ݒ�
		else
			c.setZoom(zoom);
		
		//c.setCenter(new GeoPoint(35455281,139629711));//���ݒn�̐ݒ�@������GeoPoint�@���̏ꍇ�͓����ɂȂ�

		/*�񐄏��̂����@.setBuilt�`���g����
        ZoomControls zc = (ZoomControls)map.getZoomControls();
        zc.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        zc.setGravity(Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);//��ʉ��ɕ\�������Y�[���E�B�W�F�b�g�̐ݒu�֌W
        map.addView(zc);
		 */

		
		
		this.btn=new Button(this);
		this.btn.setText("���ݒn�̎擾");
		this.btn.setTextSize(10);
		

		/*
		final Button btn2 = new Button(this);
		//���t��؂�ւ���{�^��
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				
			}
		});
		*/
		LinearLayout linear_layout = new LinearLayout(this);
		View view = getLayoutInflater().inflate(R.layout.buttons, linear_layout);
		
		LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		//btn2.setLayoutParams(param);
		//btn2.setGravity(Gravity.RIGHT+Gravity.RIGHT);
		this.map.addView(view,param);
		
		/*
		this.btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.btn.setGravity(Gravity.TOP+Gravity.CENTER_HORIZONTAL);
		this.map.addView(btn);
		this.map.setBuiltInZoomControls(true);
		*/
		
		
		Calendar calendar2 = Calendar.getInstance();
		
		//���t�݂̂��r�������̂Ł@HMS������������
		calendar2.set(Calendar.HOUR_OF_DAY,0);
		calendar2.set(Calendar.MINUTE,0);
		calendar2.set(Calendar.SECOND,0);
		calendar2.set(Calendar.MILLISECOND,0);
		
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);
		
		//���t���v���X����{�^���Ō��݂̓��t���z���Ȃ��悤�ɂ���(�����Ȃ牟���Ȃ�����)
		if(calendar2.equals(calendar)){
			Button btn2 = (Button)findViewById(R.id.button2);
			btn2.setEnabled(false);
		}
			
			
		
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
		
		class onClickButton extends MainActivity implements OnClickListener{

			//private ArrayList<GeoPoint> gp;
			private Context context;
			private MainActivity activity;
			
			public onClickButton(Context context){
				
				//this.gp = mainActiovoty.gp;
				this.context = context;
				this.activity = (MainActivity)context;
				
			}
			
			@Override
			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u

				/*locationManager.getLastKnownLocation���g���Č��ݒn���擾
				location�ɓ����geopoint�����o��
				�擾�ł��Ȃ��ꍇ�̓G���[��������̂ő΍����邱��
				 */

				btn.setEnabled(false);
				lastLocation = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
				
				lastLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, activity);
				lastLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, activity);
				
				Log.d("DRAW",calendar.getTime().toString());
				
				/*
				
				if(location!=null){
					GeoPoint gp = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
					MapController mapcon = map.getController();
									
					//�C���X�^���X���Ⴄ�H�̂�this���ƃG���[�ɂȂ�@MainActivi�̃C���X�^���X�������p���Ń��\�b�h���Ăяo��
					drawOverlay();
					
					mapcon.animateTo(gp);
					Log.d("Button",String.valueOf(gp));
				}else{
					Toast.makeText(MainActivity.this, "���ݒn���擾�ł��܂���", Toast.LENGTH_LONG).show();
				}
				
				lastLocation.removeUpdates(activity);
				*/
			}
		}

		
		
		this.btn.setOnClickListener (new onClickButton(this));

		param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		this.btn.setLayoutParams(param);
		this.btn.setGravity(Gravity.TOP+Gravity.CENTER_HORIZONTAL);
		this.map.addView(this.btn);
		this.map.setBuiltInZoomControls(true);
		
		
		

		
		
		
		//LocationManager�̐ݒ�@GPS�X�V���Ԃ�Preferences����ǂݍ��ށ@�����ꍇ��15����
		//this.setRequestLocation(readPreferences());
		
		Log.d("prefs�̒��g",String.valueOf(this.readPreferences()));
		
		/*�����ʒu�𓌋���
        GeoPoint tokyo = new GeoPoint(35681396, 139766049);
        c.animateTo(tokyo);
		 */
		
/*
		try{
			DBHepler dbh = new DBHepler(this);
			SQLiteDatabase db = dbh.getReadableDatabase();
			boolean isEof;

			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");

			//�e�[�u�����ꗗ��Ԃ��N�G��	�Ȃ������ꍇ�͐V���Ƀe�[�u�����쐬
			Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'",null);
			isEof = cursor.moveToFirst();
			
			String str = "";
			
			while(isEof){//�e�[�u�������A���Ă���
				str += cursor.getString(1)+":";
				isEof = cursor.moveToNext();
			}
			
			Log.d("onCre",str);
			
			cursor.close();
			dbh.close();
			db.close();
			
			//DB->GP�֓ǂݍ��ރ��\�b�h
			this.ReadDataBase(sdf1.format(date));
			
		}catch (Exception e) {
			// TODO: handle exception
		}

		
        /*�����Ƒ���GeoPoint��ݒ肵Overlay�ɕ`��

        GeoPoint tokyo = new GeoPoint(35681396, 139766049);
        GeoPoint osaka = new GeoPoint(34701895, 135494975);
        pinOverlay.addPoint(tokyo);
        pinOverlay.addPoint(osaka);
		 */
		
		//zoom��-1�̎��@�T�[�r�X�̋N���Ԋu��3���ɂ���
		if(this.readPreferences()==0){
			this.writePreferences(LocationMinute.MINUTE3);
			this.isService();
		}
		
		
			
	}

	
	
	
	@Override
	protected void onStart() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onStart();
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");

		
		this.ReadDataBase(sdf1.format(calendar.getTime()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		if(item.getItemId() == R.id.menu_settings){//�|�C���g�̏���

			this.pinClearS();
			this.onCreateDialog2();
			//new GridIconList(this);
			
			

		}else if(item.getItemId() == R.id.menu_settime){//�X�V���Ԃ̐ݒ�
			Intent i = new Intent(this,TimeList.class);
			this.startActivityForResult(i, 0);
			
			
		}else if(item.getItemId() == R.id.menu_path){//�O�Ղ̕\��

			Intent intent = new Intent(this,LogList.class);
			this.startActivityForResult(intent, 1);
			
			
		}else if(item.getItemId() == R.id.menu_gpout){//���O�̏o��
			
			ArrayList<String> list = new ArrayList<String>();
			
			for(OverlayItems gp:this.items){
				
				list.add(gp.getDate()+" "+gp.getGeoPoint().toString());
				
			}
			
			SdLog sdlog = new SdLog(list);
			sdlog.outFile();
			
			Toast.makeText(this,sdlog.getFileName()+"�ɕۑ����܂���", Toast.LENGTH_LONG).show();
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
		 * .startActivityForResult(intent, x);x��requset_code
		 * x�̒l�ŕ���\
		 * 
		 * 
		 * data�ɂ͌Ăяo����̃A�N�e�B�ŕێ����Ă��C���e���g������Γ���
		 * Intent.put(key,value);�ŕۑ�
		 *  data.getStringExtra(name)�ȂǂŎ��o��
		 *  
		 * */
		if(resultCode == RESULT_OK){
			if(requestCode == 0){//�X�V���Ԃ̐ݒ�

				//this.setRequestLocation(data.getLongExtra("time", 0));
				Log.d("time",String.valueOf(data.getLongExtra("time", 0)));
				if(this.readPreferences() != data.getLongExtra("time", 0)){
					this.writePreferences(data.getLongExtra("time", this.readPreferences()));

					//�V���ɃT�[�r�X���X�^�[�g������
					this.isService();
				}
				
			}else if(requestCode == 1){//�I�������e�[�u���̒l���󂯎��@�O�Ղ�\��
				
				String date = data.getStringExtra("date");
				
				SimpleDateFormat smple = new SimpleDateFormat("yyMMdd");
				try{
					Log.d("date1",date.toString());
					Date date1 = smple.parse(date.replace("D", ""));
				
					Log.d("date2",date1.toString());
					calendar.setTime(date1);
					Log.d("Itme",calendar.getTime().toString());
				}catch (Exception e) {
					// TODO: handle exception
					Log.d("date","era-desu!");
				}
				
				this.buttonOnOff();
				this.mapToLine(date);
				
			}
		}
	}

	//�Ƃ肠�����K�{
	@Override
	protected boolean isRouteDisplayed() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}

	
	//Location����������Ă��邩�HGPS�@�\��������Location�������Ȃ�(?)
	public void updateDisplay(Location location){
		if(location==null){
			Log.e("HelloLocation","location is null");
			return;
		}else
			Log.e("HelloLocation","location is not null");
	}


	//Overlay�N���A���\�b�h�@map.invalidate();��Overlay�̍ĕ`���Y�ꂸ��
	public void pinClearS(){

		Log.d("clear","�����܂�");
		this.map.getOverlays().clear();
		this.map.invalidate();
	}


	public long readPreferences(){
		SharedPreferences prefs = getSharedPreferences("Maps", MODE_PRIVATE);
		return prefs.getLong("time", LocationMinute.MINUTE3);
		
	}
	
	public void writePreferences(long l){
		SharedPreferences prefs = getSharedPreferences("Maps", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("time", l);
		editor.commit();
	}
	
	//�w�肵��GP���overlay�ɉ摜���������ރ��\�b�h
	public void drawOverlay(){
		
		
		//Drawble�������ɂ���PinItemizedOverlay�̃R���X�g���N�^�ɓn��
		//this.itemovarlay = new PinItemizedOverlay(getResources().getDrawable(R.drawable.icon01),this);

		/*map.getOverlays().add()���\�b�h��MapView�̃I�[�o�[���C��pinOverlay��`��@
        pinOverlay��List��GeoPoint��ێ����Ă���@���̕ێ����Ă���|�C���g�S�Ă�`�悷��
        pinOverlay�̓��[�J���Ȃ̂����烊�X�g�ŕێ�����Ӗ��͂���̂�? -> �C���X�^���X�ϐ��ɕύX
		   
		
		
		this.itemovarlay.addPoint(this.nowGp);
		map.getOverlays().add(this.itemovarlay);
		map.getOverlays().clear();*/
		
		//addPoint(gp)���\�b�h��gp�̈ʒu�ɕ`��
		
		
		//Overlay���g������plus�Ō��ݒn��GP��n���`�悷��
		Drawable draw = getResources().getDrawable(this.icon);
		Drawable draw2 = getResources().getDrawable(R.drawable.icon02);
		OverlayPlus plus = new OverlayPlus(this, draw,draw2,this.items,this.nowFlag,calendar.getTime());
		plus.addGp(nowGp);
		map.getOverlays().add(plus);
	
		
	}
	
	//�_�C�A���O�ɉ摜��\��������@�������g��Ȃ������I
	public void onClickImgDailog(View v){
		
		ImageGetter imageGetter = new ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				int id = Integer.parseInt(source);
				Drawable d = getResources().getDrawable(id);
				d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
				Log.d("llogg",source);
				return d;
			}
		};
		
		
		AlertDialog alerdiDialog = new AlertDialog.Builder(this).setMessage(Html.fromHtml
				("�摜�e�X�g<br><img src='"+R.drawable.ic_launcher +"'/>",imageGetter,null)).create();
		alerdiDialog.show();
		
		
		
				
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Log.d("Main",String.valueOf(event.getX())+":"+String.valueOf(event.getY()));
		return super.onTouchEvent(event);
	}

	
	//setView()���g���Ǝ��̃��C�A�E�g���g�����_�C�A���O���쐬����
	//���̂�OverlayPlus���炵���Ă΂�ĂȂ�
	protected void onCreateDialog2(){

		//GridView�̐ݒ�
		GridView gridView = new GridView(this);
		gridView.setNumColumns(GridView.AUTO_FIT);
		gridView.setVerticalSpacing(50);
		gridView.setColumnWidth(70);
		
			
		final AlertDialog dia = new AlertDialog.Builder(this).create();
		
		//adapter�̐ݒ�
		ImageAdapter image = new ImageAdapter(this);
		Drawable drawble;
		for(IconListData i:IconListData.values()){
			drawble = getResources().getDrawable(i.getId());
			image.add(drawble);
		}
		gridView.setAdapter(image);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				IconListData[] list = IconListData.values();//Enum��z��փZ�b�g�悭�g���炵��
				icon = list[arg2].getId();//�N���b�N�����A�C�R���̂h�c���Z�b�g
				dia.dismiss();//�Ώۂ̃_�C�A���O����郁�\�b�h
				
			}
		});
		
		dia.setView(gridView);
		dia.show();
		
		//������setView����̃��\�b�h�`�F�[�����o���邪�A�����Əo���Ȃ��Ȃ��
		//new AlertDialog.Builder(this).setView(gridView).create().show();
		
		
	}
	
	public void ReadDataBase(String date){
		
		DBHepler dbh = new DBHepler(this,date);
		SQLiteDatabase db = dbh.getReadableDatabase();
		//dbh.test(db);
		
		Cursor cursor = db.query(date,new String[]{"Longitude","Latitude","MapDate","Message","Icon"},null,null,null,null,null);
		Boolean isEof = cursor.moveToFirst();
		
		
		GeoPoint setGp;

		this.items.clear();
		
		Integer i=0;
		
		while(isEof){
			
			setGp = new GeoPoint(cursor.getInt(1),cursor.getInt(0));
			
			OverlayItems setItems = new OverlayItems();
			setItems.setGeoPoint(setGp);
			setItems.setDate(cursor.getString(2));
			setItems.setMessage(cursor.getString(3));
			setItems.setIconNum(cursor.getInt(4));
			
			if(cursor.getString(3) != null)
			Log.d("datavase",cursor.getString(3)+":"+i.toString());
			
			this.items.add(setItems);
			
			isEof = cursor.moveToNext();
			i++;
		}
		
		cursor.moveToLast();
		
		
		dbh.close();

		
		Log.d("read",String.valueOf(this.items.size()));
		Log.d("database","�S��close");		
	}
	
	
	public boolean isService(){
		ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = am.getRunningServices(Integer.MAX_VALUE);
		for(RunningServiceInfo r:serviceList){//.getClassName()�ŃT�[�r�X�����擾��.started�ŋN����Ԃ̊m�F
			//Log.d("service",LocationService.class.getCanonicalName());
			if(LocationService.class.getCanonicalName().equals(r.service.getClassName())){
				
				return true;
				}
			}
		
		//����I�Ɏ��s����AlarmManager�̐ݒ�
		Intent intent = new Intent(MainActivity.this,LocationService.class);
		PendingIntent service = PendingIntent.getService(MainActivity.this, 0, intent, 0);
		//intent.putExtra("time", this.readPreferences());
		long first = System.currentTimeMillis();
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		//��Ԗڂ̈����ŃX���[�v��Ԃ̎��̍s�����Z�b�g�@(�X���[�v�������p����)
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, first, this.readPreferences(), service);
		
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		this.btn.setEnabled(true);
		
		if(location!=null){
			nowGp = new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
			calendar = Calendar.getInstance();
			this.buttonOnOff();
			this.map.getOverlays().clear();
			MapController c = map.getController();
			this.nowFlag = true;
			this.drawOverlay();
			c.animateTo(nowGp);
		}else{
			Toast.makeText(this, "���ݒn���擾�ł��܂���", Toast.LENGTH_LONG).show();
		}
		
		this.lastLocation.removeUpdates(this);
		
	}

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
		

	public void dataIs(View v){
		
		if(v.getId() == R.id.button1){//���t���}�C�i�X����
			calendar.add(Calendar.DATE, -1);
		}else if(v.getId() == R.id.button2){
			calendar.add(Calendar.DATE, 1);
		}
		else{
			;
		}
		SimpleDateFormat simple = new SimpleDateFormat("yyyy/MM/dd");
		Toast.makeText(this, simple.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
		
		if(this.buttonOnOff()){
			this.pinClearS();
			this.ReadDataBase(this.sdf.format(calendar.getTime()));

			this.drawOverlay();
		}
	}
	
	public Boolean buttonOnOff(){

		this.nowFlag = false;
		Calendar calendar2 = Calendar.getInstance();
		
		//���t�݂̂��r�������̂Ł@HMS������������
		calendar2.set(Calendar.HOUR_OF_DAY,0);
		calendar2.set(Calendar.MINUTE,0);
		calendar2.set(Calendar.SECOND,0);
		calendar2.set(Calendar.MILLISECOND,0);
		

		//�Q�Ƃ��Ă�����t�����A���̓��t�𒴂��Ȃ��悤�ɂ���
		if(calendar2.equals(calendar)||calendar2.before(calendar)){
			Button btn = (Button)findViewById(R.id.button2);
			btn.setEnabled(false);
		}else{
			Button btn = (Button)findViewById(R.id.button2);
			btn.setEnabled(true);
		}		
		
		if(oldDate.equals(calendar.getTime()) || this.oldDate.after(calendar.getTime())){
			Button btn = (Button)findViewById(R.id.button1);
			btn.setEnabled(false);
		}else{
			Button btn = (Button)findViewById(R.id.button1);
			btn.setEnabled(true);
		}
		//Line����Icon��
		//this.mapToLine(this.sdf.format(calendar.getTime()));
		
		return true;
	}
	
	public void mapToLine(String date){
		try{
			if(date!=null){

				Log.d("Line",date);

				this.ReadDataBase(date);

				this.pinClearS();



				//f�Ɉړ��������v�Z���đ��
				Location location = new Location("now");
				Location oldLocation = new Location("old");
				GeoPoint gp;

				Float f=(float) 0;

				for(int i=0;i<this.items.size()-1;i++){


					gp = this.items.get(i+1).getGeoPoint();
					location.setLatitude(gp.getLatitudeE6()/1E6);
					location.setLatitude(gp.getLongitudeE6()/1E6);

					gp = this.items.get(i).getGeoPoint();
					oldLocation.setLatitude(gp.getLatitudeE6()/1E6);
					oldLocation.setLatitude(gp.getLongitudeE6()/1E6);


					f+=location.distanceTo(oldLocation);

				}

				Toast totas = Toast.makeText(this, String.valueOf(f)+"m", 1000);
				totas.show();

				//GP���m����Ō���
				LineOverlay lineOverlay = new LineOverlay(this.items);
				map.getOverlays().add(lineOverlay);
				map.invalidate();

				
				Log.d("ItemsSize",String.valueOf(this.items.size()));
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

}
