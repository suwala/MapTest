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
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.support.v4.app.NavUtils;

public class MainActivity extends MapActivity implements LocationListener {//googleMapを使う際はMapActivityを継承する

	/*
	 * Longitudeが経度
	 * Latitudeが緯度
	 * 
	 * Overlayを使いMAPに画像を書き込む
	 * 仕様変更があったらしく古い書き方に注意
	 * 
	 * 
	 * 現在初期位置横浜：更新時間15分
	 * 
	 * distance(GeoP,GeoP)で二点の距離が求められるらしい
	 * 
	 * LocationManeger.removeUpdates(this);で位置情報リクの停止
	 * 
	 * Location(引数)　の引数は何に使うのか
	 * 
	 * protected Dialog onCreateDialog(int id)を使って
	 * ダイアログにレイアウトを流し込むことに成功
	 * グリッドビューを流し込んでアイコンリストが作れるかも
	 * 
	 * 
	 */


	/*未実装
	 * アイコンの変更
	 * ログの一覧日付ごと？
	 * ロケーションチェンジ部分が怪しい
	 * caseの除外？
	 * ポイントの消去でDB事消去してる件
	 */



	private MapView map;
	//private ArrayList<Location> oldLocation = new ArrayList<Location>();
	private ArrayList<GeoPoint> gp = new ArrayList<GeoPoint>();

	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();

		DBHepler dbh = new DBHepler(this);
		SQLiteDatabase db = dbh.getReadableDatabase();

		dbh.dbClear(db);//dbをクリアする事で二重登録を防ぐ
		dbh.databaseInsert(db, this.gp);

		dbh.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		Date date = new Date(System.currentTimeMillis());
		//2012-09-10と帰ってくる
		Log.d("date.toString",date.toString());


		map = (MapView)findViewById(R.id.mapview);
		MapController c = map.getController();



		c.setZoom(15);//ズーム値の設定
		//c.setCenter(new GeoPoint(35455281,139629711));//現在地の設定　引数はGeoPoint　この場合は東京になる

		/*非推奨のやり方　.setBuilt～を使おう
        ZoomControls zc = (ZoomControls)map.getZoomControls();
        zc.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        zc.setGravity(Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);//画面下に表示されるズームウィジェットの設置関係
        map.addView(zc);
		 */

		Button btn = new Button(this);
		btn.setText("現在地の取得");
		btn.setTextSize(10);

		/*無名インナークラスの場合
		 * 
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ

				LocationManager locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
				Location myLocate = locationManager.getLastKnownLocation("gps");
				if(myLocate!=null){
					GeoPoint gp = new GeoPoint((int)(myLocate.getLatitude()*1e6), (int)(myLocate.getLongitude()*1e6));
					c.animateTo(gp);
				}
			}
		});*/

		
		/*
		 * MapControllerを内部に持つボタンの作成
		 * ボタン内部のｃはアドレスが一緒なので
		 * Activityのｃが変化しても追い続ける
		 * 
		 * new OnClickListener() {}の形では無名インナークラスとなりローカル変数を渡せない
		 * デフォオルトコンストラクタしかないので
		 * コンストラクタを通しての引渡しも出来ない
		 * 
		 * 新たに有名(?)クラスを作りOnClickListenerをインターフェースで継承し
		 * 引数を持ったデフォルトコンストラクタを作る　class名onClickButton
		 * classを作ったら
		 * 
		 * btn.setOnClickListener (new onClickButton(c));で実装
		 * 
		 */
		class onClickButton extends MainActivity implements OnClickListener{

			private ArrayList<GeoPoint> gp;
			private Context context;
			private MapView map;
			
			public onClickButton(MapController c,MainActivity mainActiovoty,Context context,MapView map){
				
				this.gp = mainActiovoty.gp;
				this.context = context;
				this.map = map;
			}
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ

				/*locationManager.getLastKnownLocationを使って現在地を取得
				locationに入れてgeopointを取り出す
				取得できない場合はエラー落ちするので対策を練ること
				*/
				
				if(!this.gp.isEmpty()){
							
					MapController mapcon = map.getController();
					//getResources().getDrawable(R.drawable.icon01);
					DrawOverlay.drawOverlay(context.getResources().getDrawable(R.drawable.icon06),this.gp.get(0),this.map);
					mapcon.animateTo(this.gp.get(this.gp.size()-1));
					Log.d("Button",this.gp.get(this.gp.size()-1).toString());
				}else{
					Toast.makeText(MainActivity.this, "現在地を取得できません", Toast.LENGTH_LONG).show();
				}
			}
		}

		
		btn.setOnClickListener (new onClickButton(c, MainActivity.this,this,this.map));



		btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		btn.setGravity(Gravity.TOP+Gravity.CENTER_HORIZONTAL);
		map.addView(btn);
		map.setBuiltInZoomControls(true);

		
		
		//LocationManagerの設定　GPS更新時間をPreferencesから読み込む　無い場合は15分に
		this.setRequestLocation(readPreferences());
		
		Log.d("prefsの中身",String.valueOf(this.readPreferences()));
		
		


		/*初期位置を東京へ
        GeoPoint tokyo = new GeoPoint(35681396, 139766049);
        c.animateTo(tokyo);
		 */

		try{
			DBHepler dbh = new DBHepler(this);
			SQLiteDatabase db = dbh.getReadableDatabase();
			boolean isEof;

			
			SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
			Log.d("sdf1.format(date)",sdf1.format(date));
			
			//Cursor cursor = db.query(sdf1.format(date), new String[]{"Longitude","Latitude"},null, null, null, null, null);
			
					
			Cursor cursor = db.query("date", new String[]{"Longitude","Latitude"},null, null, null, null, null);
			
			isEof = cursor.moveToFirst();
			GeoPoint setGp;

			while(isEof){
				Log.d("GPのサイズ",String.valueOf(this.gp.size()));

				setGp = new GeoPoint(cursor.getInt(1),cursor.getInt(0));
				this.gp.add(setGp);
				isEof = cursor.moveToNext();
				Log.d("getGeoPoint",String.valueOf(cursor.getInt(1))+":"+String.valueOf(cursor.getInt(1)));
			}
			
			/*
			cursor = db.rawQuery("select count(*) from "+sdf1.format(date), null);
			cursor.moveToLast();
			Log.d("dbのレコード数",String.valueOf(cursor.getLong(0)));
			*/

			dbh.close();
		}catch (Exception e) {
			// TODO: handle exception
		}

		/*
		 * locationで緯度経度を取得すれば起動時の現在地を取得可能
		 * 
        //pinに画像を読み込む
        Drawable pin = getResources().getDrawable(R.drawable.maru);
        //pinを引数にしてPinItemizedOverlayのコンストラクタに渡す
        PinItemizedOverlay pinOverlay = new PinItemizedOverlay(pin);
        //map.getOverlays().add()メソッドでpinOverlayを描画？の準備？
        map.getOverlays().add(pinOverlay);

        //東京と大阪のGeoPointを設定しOverlayに描画

        GeoPoint tokyo = new GeoPoint(35681396, 139766049);
        GeoPoint osaka = new GeoPoint(34701895, 135494975);
        pinOverlay.addPoint(tokyo);
        pinOverlay.addPoint(osaka);
		 */
		
		
		Gallery gallery = new Gallery(this);
		gallery.setBackgroundColor(Color.BLUE);
		
		gallery.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		gallery.setGravity(Gravity.BOTTOM+Gravity.CENTER_HORIZONTAL);
		map.addView(gallery);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d("GpSize",String.valueOf(this.gp.size()));
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



	//メニューから選択すると現在地に移動する
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ

		if(item.getItemId() == R.id.menu_settings){//ポイントの消去

			this.pinClearS();
			DBHepler dbh = new DBHepler(this);
			SQLiteDatabase db = dbh.getReadableDatabase();
			dbh.dbClear(db);
			dbh.close();

		}else if(item.getItemId() == R.id.menu_settime){//更新時間の設定
			Intent i = new Intent(this,TimeList.class);
			this.startActivityForResult(i, 0);
		}else if(item.getItemId() == R.id.menu_path){//軌跡の表示

			//GP同士を線で結ぶ
			LineOverlay lineOverlay = new LineOverlay(gp);
			map.getOverlays().add(lineOverlay);
			map.invalidate();

			
			//fに移動距離を計算して代入
			Location location = new Location("now");
			Location oldLocation = new Location("old");
			
			Float f=(float) 0;
			
			for(int i=0;i<gp.size()-1;i++){
				location.setLatitude(gp.get(i+1).getLatitudeE6()/1E6);
				location.setLatitude(gp.get(i+1).getLongitudeE6()/1E6);

				oldLocation.setLatitude(gp.get(i).getLatitudeE6()/1E6);
				oldLocation.setLatitude(gp.get(i).getLongitudeE6()/1E6);


				f+=location.distanceTo(oldLocation);

				Log.d("移動距離(m)",String.valueOf(f));
			}

			/*
			location.setLatitude(gp.get(0).getLatitudeE6()/1E6);
			location.setLatitude(gp.get(0).getLongitudeE6()/1E6);

			oldLocation.setLatitude(gp.get(1).getLatitudeE6()/1E6);
			oldLocation.setLatitude(gp.get(1).getLongitudeE6()/1E6);*/

			Toast totas = Toast.makeText(this, String.valueOf(f)+"m", 1000);
			totas.show();

			//	        for(Location l:this.oldLocation)
			//	        		f += location.distanceTo(l);

			Dialog dia = this.onCreateDialog(0);
			dia.show();
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);


		/*呼び出し先のアクティビティでsetResult(RESULT_OK, i);を実行すると
		 * resultCodeに結果が入るRESULT_OK成功　RESULT_CANCELEDキャンセルなど
		 * resultCodeは成否の分岐に使える?
		 * 
		 * resultCodeとrequestCodeの使い分けがイマイチ不明
		 * 
		 * dataには呼び出し先のアクティで保持してたインテントがあれば入る
		 * Intent.put(key,value);で保存
		 *  data.getStringExtra(name)などで取り出す
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

	//とりあえず必須
	@Override
	protected boolean isRouteDisplayed() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


	/*現在地が移動したときに実行される
	 * 
	 * エミュだとKMLで瞬間移動してたが　実機では動くたびに実行されるので電池消費が激しそう
	 * 
	 * GeoPointを溜め込んで
	 * animateTo(GeoPoint)をハンドラースレッド？で定期的に実行すれば
	 * 軌跡を描けるかも
	 * 但し直線で結ぶだけなので正確ではない（道路を突っ切る）
	 * 
	 * 60分に設定したのに呼ばれてること確認
	 * 移動してないのに（ｒｙ
	 * スリープ中でもdrawが呼ばれてること確認
	 * 45秒周期？
	 * 
	 */
	@Override
	public void onLocationChanged(Location location) {

		/*location の主なメソッド
		Log.v("----------", "----------");
        Log.v("Latitude", String.valueOf(location.getLatitude()));緯度
        Log.v("Longitude", String.valueOf(location.getLongitude()));経度
        Log.v("Accuracy", String.valueOf(location.getAccuracy()));
        Log.v("Altitude", String.valueOf(location.getAltitude()));高度（海抜ｘメートル）
        Log.v("Time", String.valueOf(location.getTime()));測定した時間
        Log.v("Speed", String.valueOf(location.getSpeed()));移動速度の取得m/s（車に乗ってるときなど）
        Log.v("Bearing", String.valueOf(location.getBearing()));方位　北を0度とみなす　時計回りに+90度
		 */



		// 現在地を取得&保存
		GeoPoint gp = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));

		// コントローラーを使用して、現在地にマップを移動
		MapController mapCtrl = map.getController();
		mapCtrl.animateTo(gp);

		/*
		//pinに画像を読み込む
		Drawable pin = getResources().getDrawable(R.drawable.icon04);
		//pinを引数にしてPinItemizedOverlayのコンストラクタに渡す
		PinItemizedOverlay pinOverlay = new PinItemizedOverlay(pin);

		/*map.getOverlays().add()メソッドでMapViewのオーバーレイにpinOverlayを描画　
        pinOverlayはListでGeoPointを保持している　その保持しているポイント全てを描画する
        pinOverlayはローカルなのだからリストで保持する意味はあるのか?
		 */     /*   
		map.getOverlays().add(pinOverlay);
		//addPoint(gp)メソッドでgpの位置に描画
		pinOverlay.addPoint(gp);
		

		/*

		if(this.oldLocation.isEmpty())
			Log.d("test","空です");
		if(this.oldLocation.size() == 4){
			Float f=(float) 0;
			for(Location l:this.oldLocation)
				f += location.distanceTo(l);
		}

		//インスタンス変数にLocationとGeoPointをそれぞれ保存
		this.oldLocation.add(location);
		*/
		this.gp.add(gp);

		Log.d("location","---"+String.valueOf(this.gp.size()));

	}

	//Locationが実装されているか？GPS機能が無いとLocationが動かない(?)
	public void updateDisplay(Location location){
		if(location==null){
			Log.e("HelloLocation","location is null");
			return;
		}else
			Log.e("HelloLocation","location is not null");
	}

	//勝手に増えてた人達
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO 自動生成されたメソッド・スタブ

	}


	//Overlayクリアメソッド　map.invalidate();でOverlayの再描画を忘れずに
	public void pinClearS(){

		map.getOverlays().clear();
		map.invalidate();
	}

	public void setRequestLocation(long i){
		//LocationManagerの取得 位置情報サービス取得
		LocationManager location = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		/*位置情報が変化したときのリスナの登録　requestLocationUpdates(サービスGPS/3G/Wifi,位置情報の更新間隔ms,位置情報の最低更新距離m,登録するリスナ);
		秒数をセットしただけだと怒涛の勢いで更新されたので　距離も1mに設定
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
	
	//指定したGP上のoverlayに画像を書き込むメソッド
	public void drawOverlay(){
		
		//pinに画像を読み込む
		Drawable pin = getResources().getDrawable(R.drawable.icon01);
		//pinを引数にしてPinItemizedOverlayのコンストラクタに渡す
		PinItemizedOverlay pinOverlay = new PinItemizedOverlay(pin);

		/*map.getOverlays().add()メソッドでMapViewのオーバーレイにpinOverlayを描画　
        pinOverlayはListでGeoPointを保持している　その保持しているポイント全てを描画する
        pinOverlayはローカルなのだからリストで保持する意味はあるのか?
		 */        
		this.map.getOverlays().add(pinOverlay);
		//addPoint(gp)メソッドでgpの位置に描画
		pinOverlay.addPoint(this.gp.get(this.gp.size()-1));
	}
	
	//ダイアログに画像を表示させる　しかし使わなかった！
	public void onClickImgDailog(View v){
		
		ImageGetter imageGetter = new ImageGetter() {
			
			@Override
			public Drawable getDrawable(String source) {
				// TODO 自動生成されたメソッド・スタブ
				
				int id = Integer.parseInt(source);
				Drawable d = getResources().getDrawable(id);
				d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
				Log.d("llogg",source);
				return d;
			}
		};
		
		
		AlertDialog alerdiDialog = new AlertDialog.Builder(this).setMessage(Html.fromHtml
				("画像テスト<br><img src='"+R.drawable.ic_launcher +"'/>",imageGetter,null)).create();
		alerdiDialog.show();
		
		
		
				
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d("Main",String.valueOf(event.getX())+":"+String.valueOf(event.getY()));
		return super.onTouchEvent(event);
	}

	
	
	protected Dialog onCreateDialog(int id){

		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

		final View ymView = inflater.inflate(R.layout.iconlist, null);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.iconLayout);
		ImageView iv = (ImageView)findViewById(R.drawable.icon01);
		
		return new AlertDialog.Builder(this).setTitle("aaa")
		.setView(ymView).create();
		
	}
	
}
