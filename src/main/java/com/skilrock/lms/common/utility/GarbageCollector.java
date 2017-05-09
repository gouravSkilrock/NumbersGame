package com.skilrock.lms.common.utility;


public class GarbageCollector extends Thread {
	//static Log logger = LogFactory.getLog(GarbageCollector.class);

	public static void main(String[] args) {
		GarbageCollector coll = new GarbageCollector();
		coll.start();
		String abc = "";
		for (int i = 0; i < 1000000; i++) {
			abc = abc + i;
		}

	}

	@Override
	public void run() {
		System.out.println("garbage collector start....");
		Runtime runtime = Runtime.getRuntime();
		long m1 = runtime.freeMemory();
		System.gc();
		long m2 = runtime.freeMemory();
		System.out.println("Total Memory with JVM " + runtime.totalMemory()
				/ (1024 * 1024) + "MB==Memory Before GC =" + m1 / 1024
				+ " KB===Memory After GC =" + m2 / 1024
				+ " KB==Memory Freed is ======= " + (m2 - m1) / 1024 + " KB");
		try {
			Thread.sleep(30 * 60 * 1000);
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
