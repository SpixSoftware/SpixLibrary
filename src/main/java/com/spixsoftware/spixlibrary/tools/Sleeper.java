package com.spixsoftware.spixlibrary.tools;

/**
 * Use Sleeper if you want to wait for Bg task to finish
 */
public class Sleeper {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Object stopableObj = new Object();

	private int timeToSleep = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public int getTimeToSleep() {
		return timeToSleep;
	}

	/**
	 * @param timeToSleep
	 *            If 0 then wait infinitely
	 */
	public void setTimeToSleep(int timeToSleep) {
		this.timeToSleep = timeToSleep;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void sleep() {
		synchronized (stopableObj) {
			try {
				if (timeToSleep == 0) {
					stopableObj.wait();
				} else {
					stopableObj.wait(timeToSleep);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * 
	 * @param timeToSleep
	 *            - one time sleep time
	 */
	public void sleep(int timeToSleep) {
		synchronized (stopableObj) {
			try {
				if (timeToSleep == 0) {
					stopableObj.wait();
				} else {
					stopableObj.wait(timeToSleep);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public void wakeUp() {
		synchronized (stopableObj) {
			stopableObj.notify();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
