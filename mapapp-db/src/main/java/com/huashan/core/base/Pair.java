package com.huashan.core.base;
public class Pair<T1,T2> {
	private T1 first = null;
	private T2 second = null;
	
	public T1 getFirst() {
		return first;
	}
	public void setFirst(T1 first) {
		this.first = first;
	}
	public T2 getSecond() {
		return second;
	}
	public void setSecond(T2 second) {
		this.second = second;
	}
	public Pair(){
		
	}
	public Pair(T1 t1, T2 t2){
		this.first = t1;
		this.second = t2;
	}
}