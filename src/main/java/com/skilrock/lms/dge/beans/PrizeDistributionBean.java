package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.Map;

public class PrizeDistributionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<Integer, Double> actual_prize_amount_map;
	private Map<Integer, Double> carryForward_amount_map;
	private int draw_id;
	private int gameNo;
	private double prize_fund;
	private double rest_amount_pool;
	private String ticketNo;

	public Map<Integer, Double> getActual_prize_amount_map() {
		return actual_prize_amount_map;
	}

	public Map<Integer, Double> getCarryForward_amount_map() {
		return carryForward_amount_map;
	}

	public int getDraw_id() {
		return draw_id;
	}

	public int getGameNo() {
		return gameNo;
	}

	public double getPrize_fund() {
		return prize_fund;
	}

	public double getRest_amount_pool() {
		return rest_amount_pool;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setActual_prize_amount_map(
			Map<Integer, Double> actual_prize_amount_map) {
		this.actual_prize_amount_map = actual_prize_amount_map;
	}

	public void setCarryForward_amount_map(
			Map<Integer, Double> carryForward_amount_map) {
		this.carryForward_amount_map = carryForward_amount_map;
	}

	public void setDraw_id(int draw_id) {
		this.draw_id = draw_id;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setPrize_fund(double prize_fund) {
		this.prize_fund = prize_fund;
	}

	public void setRest_amount_pool(double rest_amount_pool) {
		this.rest_amount_pool = rest_amount_pool;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

}