/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.List;

public class InvOrderBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BookBean> bookList;
	private OrderedGameBean orderedGameBean;
	private List<PackBean> packList;

	public List<BookBean> getBookList() {
		return bookList;
	}

	public OrderedGameBean getOrderedGameBean() {
		return orderedGameBean;
	}

	public List<PackBean> getPackList() {
		return packList;
	}

	public void setBookList(List<BookBean> bookList) {
		this.bookList = bookList;
	}

	public void setOrderedGameBean(OrderedGameBean orderedGameBean) {
		this.orderedGameBean = orderedGameBean;
	}

	public void setPackList(List<PackBean> packList) {
		this.packList = packList;
	}

}
