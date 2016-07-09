package cosmos.util;

public class Timer {

	private long _lastResetTime;
	
	public Timer() {
		_lastResetTime = System.currentTimeMillis();
	}
	
	public double getTimeSinceReset() {
		return ((System.currentTimeMillis() - _lastResetTime)/1000.0);
	}
	
	public void resetTime() {
		_lastResetTime = System.currentTimeMillis();
	}
	
}
