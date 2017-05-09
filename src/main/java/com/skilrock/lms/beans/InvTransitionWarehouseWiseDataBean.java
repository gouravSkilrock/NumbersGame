package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.List;

public class InvTransitionWarehouseWiseDataBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int startIndex;
	private List<Integer> loopList;
	private int size;
	private boolean direction;
	private String upperString;
	private String lowerString;
	private String bookStatusString;

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public List<Integer> getLoopList() {
		return loopList;
	}

	public void setLoopList(List<Integer> loopList) {
		this.loopList = loopList;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isDirection() {
		return direction;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public String getUpperString() {
		return upperString;
	}

	public void setUpperString(String upperString) {
		this.upperString = upperString;
	}

	public String getLowerString() {
		return lowerString;
	}

	public void setLowerString(String lowerString) {
		this.lowerString = lowerString;
	}

	public String getBookStatusString() {
		return bookStatusString;
	}

	public void setBookStatusString(String bookStatusString) {
		this.bookStatusString = bookStatusString;
	}

	@Override
	public String toString() {
		return "InvTransitionWarehouseWiseDataBean [startIndex=" + startIndex
				+ ", loopList=" + loopList + ", size=" + size + ", direction="
				+ direction + ", upperString=" + upperString + ", lowerString="
				+ lowerString + ", bookStatusString=" + bookStatusString + "]";
	}

}
