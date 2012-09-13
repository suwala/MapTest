package com.example.googlemaptests;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;


//http://wikiwiki.jp/android/?Google%20Map%A4%CBDrawable%A4%F2%C7%DB%C3%D6%A4%B9%A4%EBより
public class PinItemizedOverlay extends ItemizedOverlay<PinOverlayItem> {
	
	
	//GeoPointクラスは緯度と経度をint型で保持
	private List<GeoPoint> points = new ArrayList<GeoPoint>();
	
	//PinItemizedOverlayのコンストラクタ
	public PinItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));

	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0, MapView arg1) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d("PinItemizedTouch",String.valueOf(arg0.getX())+":"+String.valueOf(arg0.getY())+":points="+String.valueOf(points.size()));
		
		
		return super.onTouchEvent(arg0, arg1);
	}

	@Override
	protected PinOverlayItem createItem(int i){
		GeoPoint point = this.points.get(i);
		return new PinOverlayItem(point);
	}

	
	@Override
	public int size() {
		// TODO 自動生成されたメソッド・スタブ
		return this.points.size();
	}

	public void addPoint(GeoPoint point){
		this.points.add(point);
		this.populate();
		
	}
	
	public void clearPoint(){
		this.points.clear();
		populate();
	}
}
