package com.example.googlemaptests;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class OverlayPlus extends Overlay {

	private Context context;
	private ArrayList<OverlayItems> myItem;
	private Drawable icon;
	private GeoPoint now;
	
	public OverlayPlus(Context context){
		this.context = context;
	}
	
	public OverlayPlus(Context context,Drawable icon){
		this.context = context;
		this.icon = icon;
	}
	
	//OverlayItemsを見られるようにする
	public synchronized void setItem(ArrayList<OverlayItems> items){
		this.myItem = items;
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
		GeoPoint checkGp;
		
		for(int i=0;i<this.myItem.size();i++){
			Point point = new Point();
			pj.toPixels(this.myItem.get(i).getGeoPoint(), point);

			
			
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
	public synchronized void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO 自動生成されたメソッド・スタブ
		super.draw(canvas, mapView, shadow);
		
		if(!shadow){
			Projection pj = mapView.getProjection();
			Point point = new Point();
			if(now != null){
				pj.toPixels(this.now, point);
				Rect bound = new Rect();
				
				int halfWidth = this.icon.getIntrinsicWidth()/2;
				
				bound.left = point.x - halfWidth;
				bound.right = point.x + halfWidth;
				bound.top = point.y - this.icon.getIntrinsicHeight();
				bound.bottom = point.y;
				
				icon.setBounds(bound);
				icon.draw(canvas);				
			}
		}
	}

	public void addGp(GeoPoint gp){
		this.now = gp;
	}
	
}
