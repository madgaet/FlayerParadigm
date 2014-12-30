package com.trollCorporation.common.model.operations;

import java.io.Serializable;

import com.trollCorporation.common.model.enums.ErrorType;
import com.trollCorporation.common.model.enums.OperationType;

public abstract class Operation implements Serializable {

	private static final long serialVersionUID = -3810944174614096102L;

	private OperationType operationType;
	private ErrorType errorType;
	
	public Operation(OperationType operationType) {
		this.operationType = operationType;
	}
	
	public OperationType getOperationType() {
		return operationType;
	}
	
	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}
	
	public ErrorType getErrorType() {
		return this.errorType;
	}
	
}
