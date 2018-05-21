package com.mr.sac.oti.comm.server;

import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.pack.Parser;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class SocketServer extends Thread {
	private volatile boolean serverFlag = true;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private String requestMessageId;
	private String responseMessageId;

	private static Log log = LogFactory.get();

	public static Parser parser = OTIContainer.JSON_PARSER;
	public static String charset = "UTF-8";

	public static SocketServer start(int port,
										 String requestMessageId,
										 String responseMessageId,
										 boolean isDaemon) throws Exception {
		SocketServer thread = new SocketServer(port, requestMessageId, responseMessageId);
		thread.setDaemon(isDaemon);
		thread.start();
		return thread;
	}

	public static SocketServer startNotDaemon(int port,
										 String requestMessageId,
										 String responseMessageId) throws Exception {
		SocketServer thread = start(port, requestMessageId, responseMessageId, false);
		thread.join();
		return thread;
	}

	public SocketServer(int port,
						String requestMessageId,
						String responseMessageId) throws Exception {
		this.requestMessageId = requestMessageId;
		this.responseMessageId = responseMessageId;
		serverSocket = new ServerSocket(port);
		int poolSize = Runtime.getRuntime().availableProcessors() * 2;

		executorService = new ThreadPoolExecutor(poolSize,
				poolSize,
				0,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(1000));
	}

	public void run() {
		service();
	}

	private void service() {
		while (serverFlag) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				executorService.execute(new SocketHandler(socket,
						requestMessageId,
						responseMessageId,
						parser,
						charset));
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				if (!serverFlag) {
					log.error("SocketServer异常退出");
				} else {
					e.printStackTrace();
				}
			}
		}
	}

	public void stopSevice() throws UnknownHostException, IOException {
		serverFlag = false;
		serverSocket.close();
		shutdownAndAwaitTermination(executorService);
	}

	private void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}

	}

}
