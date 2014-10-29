package com.trollCorporation.common.utils;

public interface Observable {
	
	void addObserver(final Observer observer);
	void removeObserver(final Observer observer);
}
