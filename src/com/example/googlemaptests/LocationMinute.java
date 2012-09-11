package com.example.googlemaptests;

public class LocationMinute {
	/* 
	 * long�̒l��15�E30�E45�E60���������Ȃ��N���X
	 * �R���X�g���N�^��private�Ȃ��߃C���X�^���X���ł��Ȃ�?�͂�
	 *  
	 */
	
	
	
	/*�Q�ƕ��@�@LocationTime.MINUTE15.getTime();�őΉ����������A���Ă���
	 * LocationTime.toTime();�Ŕz�񂪋A���Ă���
	 */
	
	
	public static final LocationMinute MINUTE15 = new LocationMinute(15);
	public static final LocationMinute MINUTE30= new LocationMinute(30);
	public static final LocationMinute MINUTE45 = new LocationMinute(45);
	public static final LocationMinute MINUTE60 = new LocationMinute(60);
		
	//�z��ň����Ƃ��Ɏg��
	private static final LocationMinute[] MINUTE = {MINUTE15,MINUTE30,MINUTE45,MINUTE60};
	
	private long l;
	
	//private�@�R���X�g���N�^ �@�n���ꂽ���l��*60*1000���ĕԂ��ims��minute�ցj
	private LocationMinute(long x){
		this.l = x*60*1000;
	}
	
	//��(long)l��Ԃ��@�Z�b�g����H�O�̐��l���ق����Ƃ���
	public long getTime(){
		return this.l;
	}
	
	//�z��擾�p LocationMinute[] lt = LocationMinute.toTime();�Ŕz����Z�b�g����
	public static LocationMinute[] toTime(){
		return MINUTE;
		
	}
	
	//�~���b�𕪂ɒ����ĕԂ����\�b�h
	public long getMinute(){
		return this.l/60/1000;
	}
	
	//�ݒ肳�ꂽ�~���b�𕪂ɒ����@"����"�̌`��String�ŕԂ����\�b�h
	public String getStringMinute(){
		return String.valueOf(this.l/60/1000)+"��";
	}
	
	//debug�p�@0��Ԃ�
	public static long debug(){
		return 0;
	}

}
