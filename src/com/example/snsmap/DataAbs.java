package com.example.snsmap;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DataAbs implements DataBaseLogic{


	@Override
	public void setData(int _hitIndex,String input,GeoPoint gp,Context context,Date date,ArrayList<OverlayItems> myItem){
		
		DBHepler dbh = new DBHepler(context,sdf1.format(date));
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		ContentValues val = new ContentValues();
		this.toData(val,gp,input,date,db,myItem,_hitIndex);
		dbh.close();
		
	}
	
	public abstract void toData(ContentValues val,GeoPoint now,String input,Date date,SQLiteDatabase db,ArrayList<OverlayItems> myItem,int hitIndex);
	
}
