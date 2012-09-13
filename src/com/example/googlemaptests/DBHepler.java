package com.example.googlemaptests;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBHepler extends SQLiteOpenHelper {

	
	/*
	 * table名を統一しないとエラーでまくり
	 * MainActyで名前を決めるべきか
	 */
	private static final Integer VERSION = 1;
	private static final CursorFactory FACTORY = null;
	private static final String NAME = "MapTest.db";
	
	public DBHepler(Context context) {
		super(context, NAME, FACTORY, VERSION);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ

		Date date = new Date(System.currentTimeMillis());

		//
		SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
		

		
		db.execSQL("create table if not exists date("+
				"_id integer primary key autoincrement,"+
				" Longitude integer not null,"+
				" Latitude integer not null"+
				");"
		);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	public void databaseInsert(SQLiteDatabase db,ArrayList<GeoPoint> gp){
		
		Date date = new Date(System.currentTimeMillis());

		SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
		
		
		
		ContentValues val = new ContentValues();
		for(GeoPoint g:gp){
			val.put("Longitude", g.getLongitudeE6());
			val.put("Latitude",g.getLatitudeE6());
			//db.insertOrThrow(sdf1.format(date), null, val);
			db.insert("date", null, val);
		}
		
		
	}
	
	public void dbClear(SQLiteDatabase db){
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
		
		db.execSQL("DELETE FROM "+"date");
	}
	
	

}
