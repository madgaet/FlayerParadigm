package com.trollCorporation.common.model.operations;

import java.io.Serializable;

import com.trollCorporation.common.model.Message;
import com.trollCorporation.common.model.enums.OperationType;

public class MessageOperation extends Operation implements Serializable {
	
	private static final long serialVersionUID = 6352257618680319592L;
	private Message message;
	
	public MessageOperation(Message message) {
		super(OperationType.CHATBOX_MAILING);
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
	
}
