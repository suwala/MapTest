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
		// TODO 自動生成されたメソッド・スタブ
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent();
		
		//i.putExtra("time", time[position].getTime());
		i.putExtra("date", (String)l.getItemAtPosition(position));
		Log.d("date",(String)l.getItemAtPosition(position));
		this.setResult(RESULT_OK, i);
		//finish()でアクティビティを終了させる
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);

		DBHepler dbh = new DBHepler(this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		boolean isEof;
		Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table'",null);
		isEof = cursor.moveToFirst();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		/* adapter.add アイテムを追加します
		 * 
		 * DataBaseからテーブル名を読み取り
		 * 先頭にDが付く場合のみadapterに追加する
		 * (ワイルドカードが美味く作動しなかったため)
		 * 
		 */
		while(isEof){
			if(cursor.getString(1).indexOf("D") == 0)
				adapter.add(cursor.getString(1));
			isEof = cursor.moveToNext();
		}
		
		dbh.close();
		
		// アダプターを設定します
		this.setListAdapter(adapter);
	}

}
