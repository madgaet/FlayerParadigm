package com.trollCorporation.common.utils;

public interface Observable {
	
	boolean addObserver(final Observer observer);
	boolean removeObserver(final Observer observer);
}
