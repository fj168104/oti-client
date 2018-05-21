package com.mr.sac.oti.comm.server;

import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.sac.oti.OTIContainer;
import com.mr.sac.oti.Transaction;
import com.mr.sac.oti.pack.Parser;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable {
	private static Log log = LogFactory.get();

	private Socket socket;
	private final int buffersize = 1024;

	private String requestMessageId;
	private String responseMessageId;
	private Parser parser;
	private String charset;

	public SocketHandler(Socket socket,
						 String requestMessageId,
						 String responseMessageId,
						 Parser parser,
						 String charset) {
		this.socket = socket;
		this.requestMessageId = requestMessageId;
		this.responseMessageId = responseMessageId;
		this.parser = parser;
		this.charset = charset;
	}

	public void run() {
		OutputStream outputStream = null;
		InputStream inputStream = null;

		try {

			inputStream = socket.getInputStream();
			byte[] bytes = new byte[buffersize];
			int len;
			StringBuilder sb = new StringBuilder();
			//只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
			while ((len = inputStream.read(bytes)) != -1) {
				// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
				sb.append(new String(bytes, 0, len, charset));
			}
			log.debug("get message from client: " + sb);

			String requestString = sb.toString();
			OTIContainer container = OTIContainer.getInstance();
			Transaction transaction = container.newServiceTransaction(requestMessageId, responseMessageId, parser);
			Object responseObj = transaction.communicate(requestString, new OtiHandler());

			outputStream = socket.getOutputStream();
			outputStream.write(((String) responseObj).getBytes(charset));

			log.debug("*******************发送返回报文成功*******************");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
