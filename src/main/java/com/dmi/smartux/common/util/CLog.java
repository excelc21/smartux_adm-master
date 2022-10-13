package com.dmi.smartux.common.util;

import org.apache.commons.logging.Log;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;

/**
 * 공통 Log Class
 * @author dongho
 */
public class CLog {
	private StopWatch mStopWatch = new StopWatch();
	private String mLogIP;
	private String mLogURL;
	private String mLogMethod;
	private Log mLog;

	public CLog(Log log_logger) {
		mLog = log_logger;
		mLogIP = "";
		mLogURL = "";
		mLogMethod = "";
	}

	public CLog(Log log_logger, HttpServletRequest request) {
		mLog = log_logger;
		mLogIP = request.getRemoteAddr();
		mLogURL = request.getRequestURI();
		mLogMethod = request.getMethod();
	}

	public void startLog(Object info) {
		if (mStopWatch.isRunning()) {
			mStopWatch.stop();
		}

		mStopWatch.start();
		mLog.info(String.format("[%s] [%s][%s][START] - %s", mLogIP, mLogURL, mLogMethod, info));
	}

	public void startLog(Object... info) {
		StringBuilder sb = new StringBuilder();

		for (Object obj : info) {
			sb.append("[").append(obj).append("]");
		}

		if (mStopWatch.isRunning()) {
			mStopWatch.stop();
		}

		mStopWatch.start();
		mLog.info(String.format("[%s] [%s][%s][START] - %s", mLogIP, mLogURL, mLogMethod, sb.toString()));
	}

	public void middleLog(Object info) {
		mLog.info(String.format("[%s] [%s][%s][LOG] - %s", mLogIP, mLogURL, mLogMethod, info));
	}

	public void middleLog(Object... info) {
		StringBuilder sb = new StringBuilder();

		for (Object obj : info) {
			sb.append("[").append(obj).append("]");
		}

		mLog.info(String.format("[%s] [%s][%s][LOG] - %s", mLogIP, mLogURL, mLogMethod, sb.toString()));
	}

	public void endLog(Object info) {
		String seconds = "";

		if (mStopWatch.isRunning()) {
			mStopWatch.stop();
			seconds = String.valueOf(mStopWatch.getTotalTimeSeconds());
		}

		mLog.info(String.format("[%s] [%s][%s][END] [%s sec] - %s", mLogIP, mLogURL, mLogMethod, seconds, info));
	}

	public void endLog(Object... info) {
		StringBuilder sb = new StringBuilder();

		for (Object obj : info) {
			sb.append("[").append(obj).append("]");
		}

		String seconds = "";

		if (mStopWatch.isRunning()) {
			mStopWatch.stop();
			seconds = String.valueOf(mStopWatch.getTotalTimeSeconds());
		}

		mLog.info(String.format("[%s] [%s][%s][END] [%s sec] - %s", mLogIP, mLogURL, mLogMethod, seconds, sb.toString()));
	}

	public void resultLog(Object... info) {
		StringBuilder sb = new StringBuilder();

		for (Object obj : info) {
			sb.append("[").append(obj).append("]");
		}

		mLog.info(String.format("[%s] [%s][%s][RESULT] - %s", mLogIP, mLogURL, mLogMethod, sb.toString()));
	}

	public void errorLog(Object info) {
		mLog.error(String.format("[%s] [%s][%s] - %s", mLogIP, mLogURL, mLogMethod, info));
	}

	public void errorLog(Object... info) {
		StringBuilder sb = new StringBuilder();

		for (Object obj : info) {
			sb.append("[").append(obj).append("]");
		}

		mLog.error(String.format("[%s] [%s][%s] - %s", mLogIP, mLogURL, mLogMethod, sb.toString()));
	}
}
