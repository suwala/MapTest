package com.example.googlemaptests;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;


//http://wikiwiki.jp/android/?Google%20Map%A4%CBDrawable%A4%F2%C7%DB%C3%D6%A4%B9%A4%EBより
public class PinItemizedOverlay extends ItemizedOverlay<PinOverlayItem> {
	
	
	//GeoPointクラスは緯度と経度をint型で保持
	private List<GeoPoint> points = new ArrayList<GeoPoint>();
	
	private Drawable icon;
	private Context context;

	//PinItemizedOverlayのコンストラクタ
	public PinItemizedOverlay(Drawable defaultMarker,Context context){
		super(boundCenterBottom(defaultMarker));
		
		this.context=context;
		this.icon = defaultMarker;
	}

	public void setIcon(Drawable icon){
		this.icon=icon;
	}

	
	@Override
	public boolean onTap(GeoPoint gp, MapView map) {
		// TODO 自動生成されたメソッド・スタブ
		
		Projection projection = map.getProjection();
		int hitIndex = hitTest(projection,gp);
		
		if(hitIndex != -1){
			Toast.makeText(context, "タッチ", Toast.LENGTH_SHORT).show();
		}
		
		return super.onTap(gp, map);
		
		
	}
	
	public int hitTest(Projection pj,GeoPoint gp){
		
		Point hit = new Point();
		pj.toPixels(gp, hit);
		
		for(int i=0;i<this.size();i++){
			Point point = new Point();
			pj.toPixels(this.points.get(i), point);
			
			int halfWidth = this.icon.getIntrinsicWidth()*2;
			
			
			int left = point.x - halfWidth;
			int right = point.x + halfWidth;
			int top = point.y - this.icon.getIntrinsicHeight()*2;
			int bottom = point.y;
			
			Log.d("overlay",String.valueOf(left)+":"+String.valueOf(point.x));
			
			if(left <= hit.x && hit.x <= right){
				if(top <= hit.y && hit.y <= bottom){
					//hitIndex = i;
					return i;
				}

			}
		}
		return -1;
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

	public void addPoint(GeoPoint gp){
		this.points.add(gp);
		this.populate();
		
	}
	
	public void clearPoint(){
		Log.d("Overlay","aaaa");
		for(GeoPoint g:this.points)
			Log.d("Overlay",g.toString());
		this.points.clear();
		populate();
	}
}
