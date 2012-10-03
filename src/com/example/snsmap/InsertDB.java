package com.example.snsmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.snsmap.R;
import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class InsertDB extends DataAbs{

	
	@Override
	public void toData(ContentValues val,GeoPoint now,String input,Date date,SQLiteDatabase db,ArrayList<OverlayItems> myItem,int hitIndex) {
		// TODO 自動生成されたメソッド・スタブ
		
		
		//DBへ新規ポイントの書き込み
		val.put("Longitude",now.getLongitudeE6());
		val.put("Latitude",now.getLatitudeE6());
		val.put("MapDate", new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
		val.put("Message", input);
		val.put("Icon", R.drawable.icon02);
		
		db.insert(sdf1.format(date), null,val);
		
		//リストへ新規アイテムのADD
		OverlayItems item =new OverlayItems();
		item.setGeoPoint(new GeoPoint(now.getLongitudeE6(), now.getLatitudeE6()));
		item.setDate(new SimpleDateFormat("HH':'mm").format(Calendar.getInstance().getTime()));
		item.setMessage(input);
		item.setIconNum(R.drawable.icon02);
		myItem.add(item);
		
	}

	
}
