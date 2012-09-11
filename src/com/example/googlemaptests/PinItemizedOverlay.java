package com.example.googlemaptests;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;


//http://wikiwiki.jp/android/?Google%20Map%A4%CBDrawable%A4%F2%C7%DB%C3%D6%A4%B9%A4%EB���
public class PinItemizedOverlay extends ItemizedOverlay<PinOverlayItem> {
	
	
	//GeoPoint�N���X�͈ܓx�ƌo�x��int�^�ŕێ�
	private List<GeoPoint> points = new ArrayList<GeoPoint>();
	
	//PinItemizedOverlay�̃R���X�g���N�^
	public PinItemizedOverlay(Drawable defaultMarker){
		super(boundCenterBottom(defaultMarker));

	}

	@Override
	protected PinOverlayItem createItem(int i){
		GeoPoint point = this.points.get(i);
		return new PinOverlayItem(point);
	}

	
	@Override
	public int size() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
