package com.mr.sac.oti.comm;

import com.mr.sac.oti.bean.Message;
import com.mr.sac.oti.pack.Parser;

/**
 * Created by feng on 18-5-6
 */
public class DefaultClientTransaction extends ClientTransaction {



	public DefaultClientTransaction(Message requestMessage, Message responseMessage) {
		super(requestMessage, responseMessage);
	}


}
