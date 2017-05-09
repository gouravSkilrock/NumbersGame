package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Test {

	
	
public static void main(String[] args) {
	
	MyThread mt=new MyThread("A");
	MyThread mt1=new MyThread("B");
	mt.start();
	mt1.start();
	
	
}
}

class MyThread extends Thread{
	
	String s="Hye";
	String s1="Bye";
	public MyThread(String s) {
		setName(s);
	}
	
	public void run(){
		if ("A".equals(getName())) {
			synchronized (s) {
				synchronized (s1) {
					System.out.println("Thread A");
				}
			}
			
		} else {
			synchronized (s1) {
				synchronized (s) {
					System.out.println("Thread B");
				}
			}
			
		}
		
	}
}



/*class lock{
	 public static Object cacheLock = new Object();
	  public static Object tableLock = new Object();
	  public void oneMethod() throws InterruptedException {
	    synchronized (cacheLock) {
	    	 Thread.sleep(5);
	      synchronized (tableLock) { 
	       System.out.println("Thread 1");
	      }
	    }
	  }
	  public void anotherMethod() {
	    synchronized (tableLock) {
	      synchronized (cacheLock) { 
	       System.out.println("Thread 2");
	      }
	    }
	  }
}
*/