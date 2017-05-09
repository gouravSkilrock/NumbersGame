package com.skilrock.lms.common.utility;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class LMSRejectedExecutionHandler implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
		executor.execute(r);
	}
}
