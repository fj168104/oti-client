package com.mr.sac.oti.listen;

import com.mr.sac.oti.Transaction;

/**
 * Created by feng on 18-5-6
 */
public interface Listener {

	void handle(Transaction transaction, TransactionEvent event);
}
