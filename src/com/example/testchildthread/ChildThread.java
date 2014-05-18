package com.example.testchildthread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * ChildThread: 自定义线程工具类 Usage: 创建ChildThread对象，实现WorkForMain接口中的void
 * doJob(Message msg)函数 在doJob内做耗时操作或访问网络资源
 * 
 * @author chaos
 * 
 */
public class ChildThread extends Thread {
	private Handler childHandler;
	private final Object mSync = new Object();
	private WorkForMain threadWork = null;

	public interface WorkForMain {
		public void doJob(Message msg);
	}

	public void setThreadJob(WorkForMain threadWork) {
		this.threadWork = threadWork;
	}

	public ChildThread(WorkForMain threadWork) {
		this.threadWork = threadWork;
	}

	public void run() {
		Looper.prepare();
		synchronized (mSync) {
			childHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					threadWork.doJob(msg);
				}
			};
			mSync.notifyAll();
		}
		Looper.loop();
	}

	public Handler getHandler() {
		synchronized (mSync) {
			if (null == childHandler) {
				try {
					mSync.wait();
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
		}
		return childHandler;
	}

	public void exit() {
		getHandler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.myLooper().quit();
			}
		});
	}
}
