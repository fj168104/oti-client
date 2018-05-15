package com.mr.sac.oti.comm.server;

import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.biz.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmbedServer {

	private Server server;
	private Thread thread;
	private static Log log = LogFactory.get();

	public void start(final int port, String requestMessageId, String responseMessageId, boolean isDaemon) throws Exception {
		JettyServer server = new JettyServer(port, requestMessageId, responseMessageId);
		thread = new Thread(server);
		// daemon, service jvm, user thread leave >>> daemon leave >>> jvm leave
		thread.setDaemon(isDaemon);
		thread.start();
	}

	/**
	 * 非后台启动
	 *
	 * @param port
	 * @throws Exception
	 */
	public void startNotDaemon(final int port, String requestMessageId, String responseMessageId) throws Exception {
		start(port, requestMessageId, responseMessageId,false);
	}

	public void destroy() {
		// destroy server
		if (server != null) {
			try {
				server.stop();
				server.destroy();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		if (thread.isAlive()) {
			thread.interrupt();
		}
	}

	static class JettyServer implements Runnable {
		private static Log log = LogFactory.get();
		private Server server = null;
		private int port;
		private String requestMessageId;
		private String responseMessageId;

		public JettyServer(int port, String requestMessageId, String responseMessageId) {
			this.requestMessageId = requestMessageId;
			this.responseMessageId = responseMessageId;
			this.port = port;
		}

		public Server getServer() {
			return server;
		}

		public void run() {
			server = new Server(port);
			server.setHandler(new JettyHandler(requestMessageId, responseMessageId));

			try {
				server.start();
				server.join();
				log.info(">>>>>>>>>>> jetty server join success, port={}", port);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	static class JettyHandler extends AbstractHandler {

		private String requestMessageId;
		private String responseMessageId;

		private static Log log = LogFactory.get();

		public JettyHandler(String requestMessageId, String responseMessageId) {
			this.requestMessageId = requestMessageId;
			this.responseMessageId = responseMessageId;
		}

		public void handle(String target,
						   Request baseRequest,
						   HttpServletRequest request,
						   HttpServletResponse response) throws IOException,
				ServletException {
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);

			PrintWriter out = response.getWriter();

			log.info("target >>> {}", target);
//			byte[] bytes = readBytes(request);
//			log.info("received message >>>> {}" + new String(bytes,"UTF-8"));
			String requestString = request.getParameter("request");
			OTIContainer container = OTIContainer.getInstance();

			Transaction transaction = container.newServiceTransaction(requestMessageId, responseMessageId);
			Object responseObj = transaction.communicate(requestString, new OtiHandler());
			out.print(responseObj);
			baseRequest.setHandled(true);
			out.close();
		}

		/**
		 * read bytes from http request
		 *
		 * @param request
		 * @return
		 * @throws IOException
		 */
		public static final byte[] readBytes(HttpServletRequest request) throws IOException {
			request.setCharacterEncoding("UTF-8");
			int contentLen = request.getContentLength();
			InputStream is = request.getInputStream();
			if (contentLen > 0) {
				int readLen = 0;
				int readLengthThisTime = 0;
				byte[] message = new byte[contentLen];
				try {
					while (readLen != contentLen) {
						readLengthThisTime = is.read(message, readLen, contentLen - readLen);
						if (readLengthThisTime == -1) {
							break;
						}
						readLen += readLengthThisTime;
					}
					return message;
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					throw e;
				}
			}
			return new byte[]{};
		}
	}

	/**
	 * oti业务处理类
	 */
	static class OtiHandler implements Handler {

		@Override
		public Map<String, Object> process(Message message) {
			Map<String, Object> map = new LinkedHashMap<>();
			return map;
		}
	}

}

