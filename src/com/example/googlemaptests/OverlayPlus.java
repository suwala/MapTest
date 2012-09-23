package com.example.googlemaptests;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class OverlayPlus extends Overlay {

	private Context context;
	private ArrayList<OverlayItems> myItem;
	private Drawable icon;
	private Drawable icon2;
	private GeoPoint now;
	private Date date;
	private Boolean nowFlag;
	
	//addの方法に問題あり過去のDBにまでadd出来てる　過去のポイントへ書き込んでない
	//db.updateで上書きできるらしいぞ
	
	//コレが実行されてる
	public OverlayPlus(Context context,Drawable icon,Drawable icon2,ArrayList<OverlayItems> myItem,Boolean _flag,Date _date){
		this.context = context;
		this.icon = icon;
		this.icon2 = icon2;
		this.myItem = myItem;
		this.nowFlag = _flag;
		this.date = _date;
	}
	
	
	
	//OverlayItemsを見られるようにする
	public synchronized void setItem(ArrayList<OverlayItems> items){
		this.myItem = items;
	}
	
	@Override
	public boolean onTap(final GeoPoint gp, MapView map) {
		// TODO 自動生成されたメソッド・スタブ

		Projection projection = map.getProjection();
		final Integer hitIndex = hitTest(projection,gp);

		if(hitIndex != -1){
			
			Log.d("plusHit",String.valueOf(hitIndex));
			if(hitIndex ==-2){
				final EditText input;
				input = new EditText(this.context);
				new AlertDialog.Builder(context)
				.setIcon(R.drawable.icon01)
				.setTitle("メッセージを入力してね")
				.setView(input)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						
						if(!input.getText().toString().equals("")){
							Log.d("inputtext","size="+String.valueOf(input.getTextSize()));
							Log.d("inputtext","size="+String.valueOf(input.getText()));
							
							/*
							OverlayItems item = new OverlayItems();
							item.setGeoPoint(gp);
							
							
							
							item.setDate(new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
							item.setMessage(input.getText().toString());
							
							//とりあえずicon01を格納　アイコンの種類によって分岐させたい
							item.setIconNum(R.drawable.icon01);
							myItem.add(item);
							flag = true;
							*/
							
							SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
							DBHepler dbh = new DBHepler(context,sdf1.format(date));
							SQLiteDatabase db = dbh.getReadableDatabase();
							
							//レコードの数を返す
							Cursor cursor = db.rawQuery(("SELECT COUNT(*) from "+sdf1.format(date)),null);
							cursor.moveToFirst();
							Log.d("plus",String.valueOf(cursor.getInt(0)));
							
							ContentValues val = new ContentValues();
							val.put("Longitude",now.getLongitudeE6());
							val.put("Latitude",now.getLatitudeE6());
							val.put("MapDate", new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
							val.put("Message", input.getText().toString());
							val.put("Icon", R.drawable.icon02);
							
							db.insert(sdf1.format(date), null,val);
							dbh.close();
							
							cursor.moveToFirst();
							Log.d("plus2",String.valueOf(cursor.getInt(0)));
							
							OverlayItems item =new OverlayItems();
							item.setGeoPoint(new GeoPoint(gp.getLongitudeE6(), gp.getLatitudeE6()));
							item.setDate(new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
							item.setMessage(input.getText().toString());
							item.setIconNum(R.drawable.icon02);
							myItem.add(item);
							
							//myItem.get(hitIndex).setMessage(input.getText().toString());
							
							((MainActivity)context).pinClearS();
							((MainActivity)context).drawOverlay();
													
						}									
					}
				})
				.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ


					}
				})
				.show();
			}else if(this.myItem.get(hitIndex).getMessage()==null){
				
				
				
				Log.d("plus",date.toString());
				final EditText input;
				input = new EditText(this.context);
				new AlertDialog.Builder(context)
				.setIcon(R.drawable.icon01)
				.setTitle("メッセージを入力してね")
				.setView(input)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						
						if(!input.getText().toString().equals("")){
							Log.d("inputtext","size="+String.valueOf(input.getTextSize()));
							Log.d("inputtext","size="+String.valueOf(input.getText()));
							
							/*
							OverlayItems item = new OverlayItems();
							item.setGeoPoint(gp);
							
							
							
							item.setDate(new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
							item.setMessage(input.getText().toString());
							
							//とりあえずicon01を格納　アイコンの種類によって分岐させたい
							item.setIconNum(R.drawable.icon01);
							myItem.add(item);
							flag = true;
							*/
							
							Toast.makeText(context, myItem.get(hitIndex).getDate(), Toast.LENGTH_SHORT).show();
							
							SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
							DBHepler dbh = new DBHepler(context,sdf1.format(date));
							SQLiteDatabase db = dbh.getReadableDatabase();
							
							//レコードの数を返す
							Cursor cursor = db.rawQuery(("SELECT COUNT(*) from "+sdf1.format(date)),null);
							cursor.moveToFirst();
							Log.d("plus",String.valueOf(cursor.getInt(0)));
							
							ContentValues val = new ContentValues();
							//val.put("Longitude",gp.getLongitudeE6());
							//val.put("Latitude",gp.getLatitudeE6());
							//val.put("MapDate", new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
							val.put("Message", input.getText().toString());
							val.put("Icon", R.drawable.icon02);
							
							int hit = hitIndex+1;
							
							db.update(sdf1.format(date), val,"_id = "+hit,null);
							
							myItem.get(hitIndex).setMessage(input.getText().toString());
							myItem.get(hitIndex).setIconNum(R.drawable.icon02);
							
							dbh.close();
							
							Log.d("plus",String.valueOf(cursor.getInt(0)));
							//myItem.get(hitIndex).setMessage(input.getText().toString());
							
							((MainActivity)context).pinClearS();
							((MainActivity)context).drawOverlay();
													
						}									
					}
				})
				.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ


					}
				})
				.show();
			}else{
				/*if(this.myItem.get(hitIndex).getDate() != null){
					Toast toast = Toast.makeText(context, this.myItem.get(hitIndex).getDate(), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0,20);
					toast.show();
				}*/
				Toast.makeText(context, this.myItem.get(hitIndex).getDate()+"  "+this.myItem.get(hitIndex).getMessage(), Toast.LENGTH_SHORT).show();
				SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
				DBHepler dbh = new DBHepler(context,sdf1.format(date));
				SQLiteDatabase db = dbh.getReadableDatabase();
				
				ContentValues val = new ContentValues();
				
				
				val.put("Icon", R.drawable.icon02);
				int hit = hitIndex+1;
				db.update(sdf1.format(date), val,"_id = "+hit,null);
				
				dbh.close();
				
			}
			
		}
		
		return super.onTap(gp, map);
	}

	public int hitTest(Projection pj,GeoPoint gp){

		Point hit = new Point();
		pj.toPixels(gp, hit);
		
		Log.d("oveelay",String.valueOf(this.myItem.size()));
		
		if(!this.nowFlag){
			for(int i=0;i<this.myItem.size();i++){
				Point point = new Point();
				pj.toPixels(this.myItem.get(i).getGeoPoint(), point);
				//pj.toPixels(this.now, point);


				int halfWidth = this.icon.getIntrinsicWidth()*2;
				int left = point.x - halfWidth;
				int right = point.x + halfWidth;
				int top = point.y - this.icon.getIntrinsicHeight()*2;
				int bottom = point.y;



				if(left <= hit.x && hit.x <= right){
					if(top <= hit.y && hit.y <= bottom){
						return i;
					}
				}
			}
		}else{
			Point point = new Point();
			pj.toPixels(this.now, point);
			int halfWidth = this.icon.getIntrinsicWidth()*2;
			int left = point.x - halfWidth;
			int right = point.x + halfWidth;
			int top = point.y - this.icon.getIntrinsicHeight()*2;
			int bottom = point.y;



			if(left <= hit.x && hit.x <= right){
				if(top <= hit.y && hit.y <= bottom){
					return -2;
				}
			}
		}


		return -1;

	}

	@Override
	public synchronized void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO 自動生成されたメソッド・スタブ
		super.draw(canvas, mapView, shadow);
		if(!shadow){
			
			
			Projection pj = mapView.getProjection();
			Point point = new Point();
			//現在地点を描画 flagで管理
			if(this.nowFlag){
				pj.toPixels(this.now, point);
				Rect bound = new Rect();
				
				int halfWidth = this.icon.getIntrinsicWidth()/2;
				
				bound.left = point.x - halfWidth;
				bound.right = point.x + halfWidth;
				bound.top = point.y - this.icon.getIntrinsicHeight();
				bound.bottom = point.y;
				
				icon.setBounds(bound);
				icon.draw(canvas);				
			}else{
			
			//GP全てに描画する場合

				for(OverlayItems i:this.myItem){
					pj.toPixels(i.getGeoPoint(), point);
					Rect bound = new Rect();
					Drawable icon = this.icon;

					if(i.getIconNum() == R.drawable.icon02)
						icon = this.icon2;

					int halfWidth = icon.getIntrinsicWidth()/2;

					bound.left = point.x - halfWidth;
					bound.right = point.x + halfWidth;
					bound.top = point.y - icon.getIntrinsicHeight();
					bound.bottom = point.y;

					icon.setBounds(bound);
					icon.draw(canvas);	
				}
			}
		}
	}

	public void addGp(GeoPoint gp){
		this.now = gp;
	}
	
}
