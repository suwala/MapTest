package com.example.snsmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;

public interface DataBaseLogic {

	
	public SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");	
	
	public abstract void setData(int _hitIndex,String input,GeoPoint gp,Context context,Date date,ArrayList<OverlayItems> myItem);
}
