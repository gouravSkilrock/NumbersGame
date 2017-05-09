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

package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

import java.util.List;

import com.skilrock.lms.beans.GameBean;

/**
 * This is a helper class providing methods for managing the cart used in order
 * creation
 * 
 * @author Skilrock Technologies
 * 
 */
public class ManageCartHelper {

	/**
	 * This method is used for adding a game to the cart
	 * 
	 * @param searchList
	 * @param cartList
	 * @param gameId
	 * @param orderedQty
	 */
	public void addGameToCart(List searchList, List cartList, int gameId,
			int orderedQty) {
		GameBean gameBean = null;
		int gameIdOfBean;

		if (cartList != null) {
			for (int i = 0; i < cartList.size(); i++) {
				gameBean = (GameBean) cartList.get(i);
				gameIdOfBean = gameBean.getGameId();

				if (gameId == gameIdOfBean) {
					gameBean.setOrderedQty(orderedQty);
					// cartList.add(gameBean);
					return;
				}
			}

		}

		for (int i = 0; i < searchList.size(); i++) {
			gameBean = (GameBean) searchList.get(i);
			gameIdOfBean = gameBean.getGameId();

			if (gameId == gameIdOfBean) {
				gameBean.setOrderedQty(orderedQty);
				cartList.add(gameBean);
				return;
			}
		}

	}

	/**
	 * This method is used for removing a game from the cart
	 * 
	 * @param cartList
	 * @param gameId
	 */
	public void removeFromCart(List cartList, int gameId) {

		GameBean gameBean = null;
		int gameIdOfBean;
		for (int i = 0; i < cartList.size(); i++) {
			gameBean = (GameBean) cartList.get(i);
			gameIdOfBean = gameBean.getGameId();

			if (gameId == gameIdOfBean) {
				cartList.remove(i);
			}
		}

	}

}
