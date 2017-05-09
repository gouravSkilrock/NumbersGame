package com.skilrock.lms.common.utility;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DayWiseTimeList {
	static Log logger = LogFactory.getLog(DayWiseTimeList.class);

	public List<List<Timestamp>> getTimeList(Timestamp presentDate,
			Timestamp toDate) {

		List<Timestamp> frTimeList = new ArrayList<Timestamp>();
		List<Timestamp> toTimeList = new ArrayList<Timestamp>();
		List<List<Timestamp>> timeList = new ArrayList<List<Timestamp>>();
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.setTimeInMillis(presentDate.getTime());
		to.setTimeInMillis(toDate.getTime());

		Calendar manipulatedFrom = Calendar.getInstance();
		Calendar manipulatedTo = Calendar.getInstance();

		Calendar tempFrom = Calendar.getInstance();
		Calendar tempTo = Calendar.getInstance();

		manipulatedFrom.setTimeInMillis(from.getTimeInMillis());
		manipulatedTo.setTimeInMillis(to.getTimeInMillis());

		manipulatedFrom.set(manipulatedFrom.get(Calendar.YEAR), manipulatedFrom
				.get(Calendar.MONTH), manipulatedFrom.get(Calendar.DATE), 0, 0,
				0);
		manipulatedTo
				.set(manipulatedTo.get(Calendar.YEAR), manipulatedTo
						.get(Calendar.MONTH), manipulatedTo.get(Calendar.DATE),
						0, 0, 0);

		logger.debug(" from " + from.getTime() + "   to " + to.getTime()
				+ "  manipulatedTo " + manipulatedTo.getTime());

		if (manipulatedFrom.get(Calendar.YEAR) == manipulatedTo
				.get(Calendar.YEAR)
				&& manipulatedFrom.get(Calendar.MONTH) == manipulatedTo
						.get(Calendar.MONTH)
				&& manipulatedFrom.get(Calendar.DATE) == manipulatedTo
						.get(Calendar.DATE)
				&& manipulatedFrom.get(Calendar.HOUR) == manipulatedTo
						.get(Calendar.HOUR)
				&& manipulatedFrom.get(Calendar.MINUTE) == manipulatedTo
						.get(Calendar.MINUTE)
				&& manipulatedFrom.get(Calendar.SECOND) == manipulatedTo
						.get(Calendar.SECOND)) {
			to.add(Calendar.DATE, 1);
			frTimeList.add(new Timestamp(from.getTimeInMillis()));
			toTimeList.add(new Timestamp(to.getTimeInMillis()));
			logger.debug(" ###reconRetTktwiseEntry  same date 1111 frTimeList "
					+ frTimeList);
			logger.debug(" ###reconRetTktwiseEntry same date  11111toTimeList "
					+ toTimeList);

		} else {

			tempFrom.setTimeInMillis(from.getTimeInMillis());
			tempTo.setTimeInMillis(manipulatedFrom.getTimeInMillis());
			tempTo.add(Calendar.DATE, 1);
			int j = 0;

			while (!(tempFrom.get(Calendar.YEAR) == manipulatedTo
					.get(Calendar.YEAR)
					&& tempFrom.get(Calendar.MONTH) == manipulatedTo
							.get(Calendar.MONTH)
					&& tempFrom.get(Calendar.DATE) == manipulatedTo
							.get(Calendar.DATE)
					&& tempFrom.get(Calendar.HOUR) == manipulatedTo
							.get(Calendar.HOUR)
					&& tempFrom.get(Calendar.MINUTE) == manipulatedTo
							.get(Calendar.MINUTE) && tempFrom
					.get(Calendar.SECOND) == manipulatedTo.get(Calendar.SECOND))) {
				j++;

				frTimeList.add(new Timestamp(tempFrom.getTimeInMillis()));
				toTimeList.add(new Timestamp(tempTo.getTimeInMillis()));

				tempFrom.setTimeInMillis(tempTo.getTimeInMillis());
				tempTo.add(Calendar.DATE, 1);

			}
			if (tempTo.compareTo(to) > 0) {
				tempTo.add(Calendar.DATE, -1);
				if (!(tempTo.get(Calendar.YEAR) == to.get(Calendar.YEAR)
						&& tempTo.get(Calendar.MONTH) == to.get(Calendar.MONTH)
						&& tempTo.get(Calendar.DATE) == to.get(Calendar.DATE)
						&& tempTo.get(Calendar.HOUR) == to.get(Calendar.HOUR)
						&& tempTo.get(Calendar.MINUTE) == to
								.get(Calendar.MINUTE) && tempTo
						.get(Calendar.SECOND) == to.get(Calendar.SECOND))) {
					frTimeList.add(new Timestamp(tempTo.getTimeInMillis()));
					toTimeList.add(new Timestamp(to.getTimeInMillis()));
				}
			}

			logger
					.debug("&& frTimeList.size()!=0** ###reconRetTktwiseEntry  Fianal 1111 frTimeList "
							+ frTimeList);
			logger.debug("** ###reconRetTktwiseEntry Fianal  11111toTimeList "
					+ toTimeList);

		}
		timeList.add(frTimeList);
		timeList.add(toTimeList);
		return timeList;
	}

}
