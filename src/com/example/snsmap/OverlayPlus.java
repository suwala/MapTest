package com.example.snsmap;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.Toast;

import com.example.snsmap.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class OverlayPlus extends Overlay implements GestureDetector.OnGestureListener{
/*GestureDetector���������邱�Ƃɂ���ă_�u���^�b�v���̃C�x���g�̊g�����o����
 * onTap�Ŋg���C�x���g�������������ǂ�����������Ȃ�����
 * �����ł��������邯��longPress�őË�
 */
	private Context context;
	private ArrayList<OverlayItems> myItem;
	private Drawable icon;
	private int iconResoce;
	private GeoPoint now;
	private Date date;
	private Boolean nowFlag;
	
	private GestureDetector gestureDetector;
	private MotionEvent event;
	
	//add�̕��@�ɖ�肠��ߋ���DB�ɂ܂�add�o���Ă�@�ߋ��̃|�C���g�֏�������łȂ�
	//db.update�ŏ㏑���ł���炵����
	
	//�R�������s����Ă�
	public OverlayPlus(Context context,Drawable icon,int icon2,ArrayList<OverlayItems> myItem,Boolean _flag,Date _date){
		this.context = context;
		this.icon = icon;
		this.iconResoce = icon2;
		this.myItem = myItem;
		this.nowFlag = _flag;
		this.date = _date;
		//���̃N���X�����X�i�[�Ƃ��Đݒ�
		this.gestureDetector = new GestureDetector(context,this);
		
	}
	
	
	
	//OverlayItems��������悤�ɂ���
	public synchronized void setItem(ArrayList<OverlayItems> items){
		this.myItem = items;
	}
	
	
	//����gp�̓^�b�`�����|�C���g��GeoPoint�ɂȂ�
	@Override
	public boolean onTap(final GeoPoint gp, MapView map) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		
		Projection projection = map.getProjection();
		final Integer hitIndex = hitTest(projection,gp);

		//interface��abstract�ŃX������ �@�܂���������
		
		if(hitIndex != -1){
			
			Log.d("plusHit",String.valueOf(hitIndex));
			if(hitIndex ==-2){//-2��newPoint(���ݒn��\��)������
				final EditText input;
				input = new EditText(this.context);
				new AlertDialog.Builder(context)
				.setIcon(R.drawable.icon01)
				.setTitle("���b�Z�[�W����͂��Ă�")
				.setView(input)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					//�V�K�|�C���g�Ƀ��b�Z�[�W�����͂��ꂽ�ꍇ
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �����������ꂽ���\�b�h�E�X�^�u
						
						if(!input.getText().toString().equals("")){
							Log.d("inputtext","size="+String.valueOf(input.getTextSize()));
							Log.d("inputtext","text="+String.valueOf(input.getText().toString()));
							
							Log.d("inser",String.valueOf(myItem.size()));
							OverlayItems item = new OverlayItems();
							
							SimpleDateFormat sdf1 = new SimpleDateFormat("'D'yyMMdd");
							item.setItem(sdf1.format(date), input.getText().toString(), gp, iconResoce);
							myItem.add(item);
							Log.d("inser",String.valueOf(myItem.size()));
							DataBaseLogic dbl = new InsertDB();
							dbl.setData(hitIndex, input.getText().toString(), now, context, date,iconResoce);
							
							((MainActivity)context).pinClearS();
							((MainActivity)context).drawOverlay();
													
						}									
					}
				})
				.setNegativeButton("�L�����Z��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �����������ꂽ���\�b�h�E�X�^�u


					}
				})
				.show();//�����܂�New�|�C���g
			}else if(this.myItem.get(hitIndex).getMessage()==null){//�����̃|�C���g�Ƀq�b�g���@���b�Z�[�W����̏ꍇ
				
				Toast.makeText(context, this.myItem.get(hitIndex).getDate(), Toast.LENGTH_SHORT).show();
				Log.d("plus",date.toString());
				final EditText input;
				input = new EditText(this.context);
				new AlertDialog.Builder(context)
				.setIcon(R.drawable.icon01)
				.setTitle("���b�Z�[�W����͂��Ă�")
				.setView(input)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					//�����|�C���g�ւ̍X�V
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �����������ꂽ���\�b�h�E�X�^�u
						
						if(!input.getText().toString().equals("")){//���͂��������ꍇ
							Log.d("inputtext","size="+String.valueOf(input.getTextSize()));
							Log.d("inputtext","size="+String.valueOf(input.getText()));
							
							
							
							Toast.makeText(context, myItem.get(hitIndex).getDate(), Toast.LENGTH_SHORT).show();
							DataBaseLogic dbl = new UpdateDB();
							dbl.setData(hitIndex, input.getText().toString(),context, date, myItem,iconResoce);
							((MainActivity)context).pinClearS();
							((MainActivity)context).drawOverlay();
													
						}									
					}
				})
				.setNegativeButton("�L�����Z��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �����������ꂽ���\�b�h�E�X�^�u


					}
				})
				.show();
			}else{//����P�Ƀq�b�g�����b�Z�[�W�����͍ς݂̏ꍇ
				
				Toast.makeText(context, this.myItem.get(hitIndex).getDate()+"  "+this.myItem.get(hitIndex).getMessage(), Toast.LENGTH_SHORT).show();
				
			}
			
		}
		
		if(gestureDetector.onTouchEvent(event)){
			Toast.makeText(context, "Test2", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return super.onTap(gp, map);
	}

	public int hitTest(Projection pj,GeoPoint gp){

		Point hit = new Point();
		pj.toPixels(gp, hit);
		
		Log.d("oveelay",String.valueOf(this.myItem.size()));
		
		if(!this.nowFlag){
			for(int i=0;i<this.myItem.size();i++){
				Point point = new Point();
				pj.toPixels(this.myItem.get(i).getGeoPoint(), point);
				//pj.toPixels(this.now, point);


				int halfWidth = this.icon.getIntrinsicWidth()*2;
				int left = point.x - halfWidth;
				int right = point.x + halfWidth;
				int top = point.y - this.icon.getIntrinsicHeight()*2;
				int bottom = point.y;



				if(left <= hit.x && hit.x <= right){
					if(top <= hit.y && hit.y <= bottom){
						return i;
					}
				}
			}
		}else{
			Point point = new Point();
			pj.toPixels(this.now, point);
			int halfWidth = this.icon.getIntrinsicWidth()*2;
			int left = point.x - halfWidth;
			int right = point.x + halfWidth;
			int top = point.y - this.icon.getIntrinsicHeight()*2;
			int bottom = point.y;



			if(left <= hit.x && hit.x <= right){
				if(top <= hit.y && hit.y <= bottom){
					return -2;
				}
			}
		}
		return -1;

	}

	@Override
	public synchronized void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.draw(canvas, mapView, shadow);
		if(!shadow){
			
			
			Projection pj = mapView.getProjection();
			Point point = new Point();
			//���ݒn�_��`�� flag�ŊǗ�
			if(this.nowFlag){
				pj.toPixels(this.now, point);
				Rect bound = new Rect();
				
				int halfWidth = this.icon.getIntrinsicWidth()/2;
				
				bound.left = point.x - halfWidth;
				bound.right = point.x + halfWidth;
				bound.top = point.y - this.icon.getIntrinsicHeight();
				bound.bottom = point.y;
				
				this.icon.setBounds(bound);
				this.icon.draw(canvas);		
			}else{
			
			//GP�S�Ăɕ`�悷��ꍇ

				for(OverlayItems i:this.myItem){
					pj.toPixels(i.getGeoPoint(), point);
					Rect bound = new Rect();

					Drawable icon,icon2;

				
					if(null != i.getMessage())
						Log.d("plus",i.getMessage());
					
					
					if(i.getIconNum() == 0){
						icon = this.context.getResources().getDrawable(R.drawable.icon01);
						
						int halfWidth = icon.getIntrinsicWidth()/2;

						bound.left = point.x - halfWidth;
						bound.right = point.x + halfWidth;
						bound.top = point.y - icon.getIntrinsicHeight();
						bound.bottom = point.y;
						
						icon.setBounds(bound);
						icon.draw(canvas);
						
						
					}else{
						icon2 = this.context.getResources().getDrawable(i.getIconNum());
						Log.d("plus",String.valueOf(i.getIconNum()));
						Log.d("plus+",String.valueOf(R.drawable.icon01));
						
						int halfWidth = icon2.getIntrinsicWidth()/2;

						bound.left = point.x - halfWidth;
						bound.right = point.x + halfWidth;
						bound.top = point.y - icon2.getIntrinsicHeight();
						bound.bottom = point.y;
						
						icon2.setBounds(bound);
						icon2.draw(canvas);
					}
					/*
					int halfWidth = icon.getIntrinsicWidth()/2;

					bound.left = point.x - halfWidth;
					bound.right = point.x + halfWidth;
					bound.top = point.y - icon.getIntrinsicHeight();
					bound.bottom = point.y;
					
					icon.setBounds(bound);
					icon.draw(canvas);	
					*/
				}
			}
		}
	}

	public void addGp(GeoPoint gp){
		this.now = gp;
	}



	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}



	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}



	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		this.event = e;
		this.gestureDetector.onTouchEvent(e);
		return super.onTouchEvent(e, mapView);
	}


	//Overlay�����݂����������ꂽ�ꍇ
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
				
		((MainActivity)context).onCreateDialog2();
	}



	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}



	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}



	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return false;
	}
	
}
