package com.utils.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 多线程处理一批任务。</br>
 * 这样理解：<br>
 * 有一个queue，里面的都是待操作任务；<br>
 * 每次从queue中取出QUEUE_POP_SIZE个任务；<br>
 * 调用dealJobs方法，多线程同时执行这QUEUE_POP_SIZE个任务。直到执行完毕方法才结束。 <br>
 * 休息TIME_OUT的时间。然后重新判定队列有没有任务。
 * 
 * @require:
 * 		任务执行过程job子类。实现任务的独立过程。
 * 
 * @date 2013-1-6
 * @author LiangRL
 * @alias E.E.
 * @param <T>
 */
public class Listener<T extends Job> {
	
	/** 队列最大长度 */
	private int batchExecuveSize = 3;
	
	/** 是否正在运行 */
	private boolean listened = false;
	
	/** 待处理任务 */
	private java.util.Queue<T> queue = new ConcurrentLinkedQueue<T>();

	/** 判定周期 */
	private long TIME_OUT = 10000l;
	
	/** 这种东西，单例就够了。 */
	private static Listener<Job> instance;
	
	private Listener() {
	}

	public static Listener<Job> getInstance(){
		if(instance == null){
			instance = new Listener<Job>();
		}
		return instance;
	}
	
	/**
	 * queue 和 jobsDealing存在不存在对象
	 * @param t
	 * @return
	 */
	public boolean contains(T t){
		while(queue.iterator().hasNext()){
			T temp = queue.iterator().next();
			if(temp.equals(t)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 入一个任务到队列
	 * @param obj
	 * @return
	 */
	public synchronized boolean push(T obj) {
		if (contains(obj)) {
			return false;
		} else {
			queue.add(obj);
			return true;
		}
	}

	/**
	 * 进入一批任务到队列
	 * @param list
	 */
	public void push(List<T> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		for (T obj : list) {
			push(obj);
		}
	}	
	
	/** 启动方法 */
	public void start() {
		if (!listened) {
			listenQueue();
			listened = true;
		} else {
			throw new RuntimeException("已经开始监听！不能重复启动！");
		}
	}
	
	/** 
	 * 强制性关闭</br>
	 * 可能抛出异常
	 * */
	public void stop(){
		listened = false;
		queue.clear();
	}

	/** 待处理出列QUEUE_POP_SIZE个任务-->正在处理 */
	private List<T> pop() {
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < batchExecuveSize; i++) {
			T temp = queue.poll();
			if (temp == null) {
				break;
			} else {
				result.add(temp);
			}
		}
		return result;
	}

	/**
	 * 处理任务
	 * 待传进来的任务列表全部结束了，才能结束方法
	 * @param list
	 */
	private void dealJobs(List<T> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		List<Thread> threadList = new ArrayList<Thread>();
		for (Job temp : list) {
			threadList.add(new Thread(temp));
		}
		for (Thread t : threadList) {
			t.start();
		}
		//
		for (;;) {
			//强制性关闭
			if(!listened){
				break;
			}
			if (isThreadListAllDead(threadList)) {
				break;
			}
			try {
				Thread.sleep(TIME_OUT);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(),e);
			}
		}
	}

	/**
	 * 
	 * @param threadList
	 * @return
	 */
	private boolean isThreadListAllDead(List<Thread> threadList) {
		if (threadList == null || threadList.size() == 0) {
			return true;
		}
		for (Thread t : threadList) {
			if (t.isAlive()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	private void listenQueue() {
		new Thread(new Runnable() {
			public void run() {
				try {
					for (;;) {
						List<T> jobsDealing = pop();
						dealJobs(jobsDealing);
						jobsDealing.clear();
					}
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(),e);
				}
			}
		}).start();
	}
	
	//////////////////////////////////////////getter and setter
	
	public int getBatchExecuveSize() {
		return batchExecuveSize;
	}

	public long getTIME_OUT() {
		return TIME_OUT;
	}

	public void setBatchExecuveSize(int batchExecuveSize) {
		this.batchExecuveSize = batchExecuveSize;
	}

	public void setListened(boolean listened) {
		this.listened = listened;
	}

	public void setTIME_OUT(long tIME_OUT) {
		TIME_OUT = tIME_OUT;
	}
}
