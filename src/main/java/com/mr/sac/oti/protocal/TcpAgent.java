package com.mr.sac.oti.protocal;

import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by feng on 18-5-10
 */
public class TcpAgent implements ProtocolAgent {
	private static Log log = LogFactory.get();

	private String IP;
	private String Port;
	private Socket client;
	private final int TIME_OUT = 1000 * 30;    //改大一点测试用
	private final int BUFF_LEN = 5000;
	private static String charset = "UTF-8";

	@Override
	public Object exchange(String endPoint, Object mObject) {
		String returnObj = null;
		OutputStream outputStream = null;
		InputStream inputStream =null;
		String[] ep = endPoint.split(":");
		try {
			client = new Socket(ep[0], Integer.parseInt(ep[1]));
			// 建立连接后获得输出流
			 outputStream = client.getOutputStream();
			String message = (String) mObject;
			client.getOutputStream().write(message.getBytes(charset));
			//通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
			client.shutdownOutput();

			 inputStream = client.getInputStream();
			byte[] bytes = new byte[5000];
			int len;
			StringBuilder sb = new StringBuilder();
			while ((len = inputStream.read(bytes)) != -1) {
				//注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
				sb.append(new String(bytes, 0, len,charset));
			}
			log.debug("get message from server: " + sb);
			returnObj = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(!Objects.isNull(inputStream)){
					inputStream.close();
				}
				if(!Objects.isNull(outputStream)){
					outputStream.close();
				}
				if(!Objects.isNull(client)){
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return returnObj;
	}
}
