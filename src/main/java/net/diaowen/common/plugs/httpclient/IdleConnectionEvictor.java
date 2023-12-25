package net.diaowen.common.plugs.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.logging.Logger;

public class IdleConnectionEvictor extends Thread {
	/**
	 * 日志
	 */
	private final Logger logger = Logger.getLogger(IdleConnectionEvictor.class.getName());

	private final HttpClientConnectionManager connMgr;

	private volatile boolean shutdown;

	public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
		this.connMgr = connMgr;
		// 启动当前线程
		this.start();
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					// 关闭失效的连接
					connMgr.closeExpiredConnections();
				}
			}
		} catch (InterruptedException ex) {
			logger.warning(ex.getMessage());
			Thread.currentThread().interrupt();
			// 结束
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}