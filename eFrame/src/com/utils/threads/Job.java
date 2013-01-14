package com.utils.threads;

/**
 * 任务的执行过程。
 * <br>
 * @date 2013-1-6
 * @author LiangRL
 * @alias E.E.
 */
public abstract class Job implements Runnable {
	
	/**
	 * 必须覆盖equals方法。因为Listener中有重复元素的判定。
	 * @param obj
	 * @return
	 */
	public abstract boolean equals(Job obj);
	
	/**
	 * 具体的运行内容。
	 */
	public abstract void run();

}
