package com.example.googlemaptests;

public class LocationMinute {
	/* 
	 * longの値を15・30・45・60しか持たないクラス
	 * コンストラクタがprivateなためインスタンス化できない?はず
	 *  
	 */
	
	
	
	/*参照方法　LocationTime.MINUTE15.getTime();で対応した分が帰ってくる
	 * LocationTime.toTime();で配列が帰ってくる
	 */
	
	
	public static final LocationMinute MINUTE15 = new LocationMinute(15);
	public static final LocationMinute MINUTE30= new LocationMinute(30);
	public static final LocationMinute MINUTE45 = new LocationMinute(45);
	public static final LocationMinute MINUTE60 = new LocationMinute(60);
		
	//配列で扱うときに使う
	private static final LocationMinute[] MINUTE = {MINUTE15,MINUTE30,MINUTE45,MINUTE60};
	
	private long l;
	
	//private　コンストラクタ 　渡された数値を*60*1000して返す（msをminuteへ）
	private LocationMinute(long x){
		this.l = x*60*1000;
	}
	
	//生(long)lを返す　セットや加工前の数値がほしいときに
	public long getTime(){
		return this.l;
	}
	
	//配列取得用 LocationMinute[] lt = LocationMinute.toTime();で配列をセットする
	public static LocationMinute[] toTime(){
		return MINUTE;
		
	}
	
	//ミリ秒を分に直して返すメソッド
	public long getMinute(){
		return this.l/60/1000;
	}
	
	//設定されたミリ秒を分に直し　"ｘ分"の形でStringで返すメソッド
	public String getStringMinute(){
		return String.valueOf(this.l/60/1000)+"分";
	}
	
	//debug用　0を返す
	public static long debug(){
		return 0;
	}

}
