package com.example.googlemaptests;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class OverlayItems  {

	private GeoPoint gp;
	private String message;
	private Integer iconNum;
	

	
	public void setGeoPoint(GeoPoint gp){
		this.gp = gp;
	}
	
	public GeoPoint getGeoPoint(){
		return this.gp;
	}
	
	public void setIconNum(int iconNum){
		this.iconNum = iconNum;
	}
	
	public int getIconNum(){
		return this.iconNum;
	}
	
	public void setMessage(String str){
		this.message = str;
	}
	
	public String getMessage(){
		return this.message;
	}
}
