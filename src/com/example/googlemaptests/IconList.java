package com.example.googlemaptests;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;




public class IconList extends Activity {


	/*
	 * ���X�g�̕��т��c���ɑΉ��������N���X�̗\��
	 * �摜���i�q��ɕ\�������ā@ ��������C�x���g�����݂����Ȍ`
	 * GridView�Ƃ����̂�����̂ł�����ɕύX
	 * 
	 * 
	 */
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		//ArrayList<Bitmap> list = load();
		//Bitmap
		this.onCreateDialog(0);
		
		
	}
	
	protected Dialog onCreateDialog(int id){
		
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.iconLayout);
		
		return new AlertDialog.Builder(IconList.this)
		.setView(layout).create();
		
	}

}

/* �i�q�󃊃X�g�̎c�[ extends ListActivity���g���Ă�
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO �����������ꂽ���\�b�h�E�X�^�u
	super.onCreate(savedInstanceState);
	
	//Map�Ƀ��\�[�X���i�[�@�����Intger�݂̂�����List�ł��悳��?
	Map<String,Integer> iconMap = new HashMap<String, Integer>(); 
	Integer img = getResources().getIdentifier("icon01", "drawable", getPackageName());
	//Integer img = R.drawable.icon01;
	iconMap.put("image", img);
	
	Map<String,Integer> iconMap2 = new HashMap<String, Integer>(); 
	Integer img2 = getResources().getIdentifier("icon02", "drawable", getPackageName());
	iconMap2.put("image", img2);
	
	Map<String,Integer> iconMap3 = new HashMap<String, Integer>(); 
	Integer img3 = getResources().getIdentifier("icon03", "drawable", getPackageName());
	iconMap3.put("image", img3);
	
	
	//List��Map���i�[
	List<Map<String,Integer>> data = new ArrayList<Map<String,Integer>>();
	data.add(iconMap);
	data.add(iconMap2);
	data.add(iconMap3);
	
	String[] from = {"image"};
	int[] to = {R.id.iconlist1};
	
	//.setListAdapter(adapter)�Ń��X�g�ɒǉ�
	SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.iconlist,from,to);
	this.setListAdapter(adapter);
	
}
*/
