package com.example.googlemaptests;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LineOverlay extends Overlay{
	
	private ArrayList<GeoPoint> gp;
	
	public LineOverlay(ArrayList<GeoPoint> gp){
		
		//�V����List�̃C���X�^���X����� this.gp=gp�Ƃ�����ꍇ�A�h���X�̃R�s�[�ƂȂ��ē��삪���������Ȃ�
		this.gp = new ArrayList<GeoPoint>(gp);
		Log.d("Line",String.valueOf(this.gp.size()));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.draw(canvas, mapView, shadow);
		if(this.gp.size() > 1){
			if(!shadow){
				Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG);
				paint.setStyle(Paint.Style.STROKE);
				paint.setAntiAlias(true);
				paint.setStrokeWidth(3);
				paint.setColor(Color.BLUE);

				Path  path = new Path();
				Projection projection = mapView.getProjection();
				Point pxStart;
				Point pxEnd;
				
				Log.d("draw","�`��J�n");
				for(int i = 0;i < this.gp.size()-1;i++){
					pxStart = projection.toPixels(gp.get(i), null);
					pxEnd = projection.toPixels(gp.get(i+1), null);
					path.moveTo(pxStart.x,pxStart.y);
					path.lineTo(pxEnd.x,pxEnd.y);
					canvas.drawPath(path, paint);
				}
				
				Log.d("LineGpSize",String.valueOf(this.gp.size()));
			}
		}
	}
	
	
	

}
