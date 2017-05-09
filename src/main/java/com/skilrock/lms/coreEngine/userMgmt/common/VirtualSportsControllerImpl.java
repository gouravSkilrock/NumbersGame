package com.skilrock.lms.coreEngine.userMgmt.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSRejectedExecutionHandler;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;

class VirtualSportsRunnable implements Runnable {
	private String methodType;
	private Object object = null;

	public VirtualSportsRunnable() {
	}

	public VirtualSportsRunnable(String methodType, Object object) {
		this.methodType = methodType;
		this.object = object;
	}

	@Override
	public void run() {
		try {
			if ("REGISTRATION".equals(methodType)) {
				VirtualSportsIntegration.Single.INSTANCE.instance.registerRetailer((VSRequestBean) object);
			}
		} catch (LMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class VirtualSportsCallable implements Callable<VSResponseBean> {
	private String methodType;
	private Object object = null;

	public VirtualSportsCallable() {
	}

	public VirtualSportsCallable(String methodType, Object object) {
		this.methodType = methodType;
		this.object = object;
	}

	@Override
	public VSResponseBean call() {
		try {
			if ("RESET_PASSWORD".equals(methodType)) {
				return VirtualSportsIntegration.Single.INSTANCE.instance.resetPassword((VSRequestBean) object);
			} else if ("GET_SALE_TXN_STATUS".equals(methodType)) {
				return VirtualSportsIntegration.Single.INSTANCE.instance.getSaleTxnStatus((VSRequestBean) object);
			}
		} catch (LMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

public class VirtualSportsControllerImpl {
	private ExecutorService executor = null;
	private VirtualSportsControllerImpl() {
		executor = new ThreadPoolExecutor(2, 10, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10), new LMSRejectedExecutionHandler());
	}

	public enum Single {
		INSTANCE;
		VirtualSportsControllerImpl instance = new VirtualSportsControllerImpl();

		public VirtualSportsControllerImpl getInstance() {
			if (instance == null)
				return new VirtualSportsControllerImpl();
			else
				return instance;
		}
	}

	public void registerRetailer(VSRequestBean vsRequestBean) {
		executor.execute(new VirtualSportsRunnable("REGISTRATION", vsRequestBean));
	}
	
	public VSResponseBean resetPassword(VSRequestBean vsRequestBean) throws InterruptedException, ExecutionException {
		Future<VSResponseBean> futur = executor.submit(new VirtualSportsCallable("RESET_PASSWORD", vsRequestBean));
		VSResponseBean vsResponseBean = futur.get();
		return vsResponseBean;
	}
	
	public VSResponseBean getTxnStatus(VSRequestBean vsRequestBean) throws InterruptedException, ExecutionException {
		Future<VSResponseBean> futur = executor.submit(new VirtualSportsCallable("GET_SALE_TXN_STATUS", vsRequestBean));
		VSResponseBean vsResponseBean = futur.get();
		return vsResponseBean;
	}
}