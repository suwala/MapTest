package com.example.snsmap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LogList extends ListActivity{
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent();
		
		//i.putExtra("time", time[position].getTime());
		i.putExtra("date", (String)l.getItemAtPosition(position));
		Log.d("date",(String)l.getItemAtPosition(position));
		this.setResult(RESULT_OK, i);
		//finish()�ŃA�N�e�B�r�e�B���I��������
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);

		DBHepler dbh = new DBHepler(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		boolean isEof;
		Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'",null);
		isEof = cursor.moveToFirst();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		/* adapter.add �A�C�e����ǉ����܂�
		 * 
		 * DataBase����e�[�u������ǂݎ��
		 * �擪��D���t���ꍇ�̂�adapter�ɒǉ�����
		 * (���C���h�J�[�h���������쓮���Ȃ���������)
		 * 
		 */
		while(isEof){
			if(cursor.getString(1).indexOf("D") == 0)
				adapter.add(cursor.getString(1));
			isEof = cursor.moveToNext();
		}
		
		dbh.close();
		
		// �A�_�v�^�[��ݒ肵�܂�
		this.setListAdapter(adapter);
	}

}
