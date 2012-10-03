package com.example.snsmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.snsmap.R;
import com.google.android.maps.GeoPoint;

public class UpdateDB extends DataAbs {

	@Override
	public void toData(ContentValues val, GeoPoint now, String input,
			Date date, SQLiteDatabase db, ArrayList<OverlayItems> myItem,int hitIndex) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
		//�f�[�^�x�[�X�̍X�V
		val.put("Message", input);
		val.put("Icon", R.drawable.icon02);
		db.update(sdf1.format(date), val,"_id = "+(hitIndex+1),null);//3�Ԗڈ�����String�@�����int��n���Ă�悤��
		
		//�A�C�e���̍X�V
		myItem.get(hitIndex).setMessage(input);
		myItem.get(hitIndex).setIconNum(R.drawable.icon02);
		
		Log.d("text",myItem.get(hitIndex).getMessage());
		

	}

}