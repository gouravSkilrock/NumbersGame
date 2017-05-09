
(function ($) {
	$.fn.countdown = function (options, callback) {
		var settings = $.extend({
			date: null,
			offset: null,
			currentDate: null,
			day: 'Day',
			days: 'Days',
			hour: 'Hour',
			hours: 'Hours',
			minute: 'Minute',
			minutes: 'Minutes',
			second: 'Second',
			seconds: 'Seconds'
		}, options);

	
		// Save container
		var container = this;

		/**
		 * Change client's local date to match offset timezone
		 * @return {Object} Fixed Date object.
		 */
		var currentDate = function () {
			var date = new Date(settings.currentDate);
			settings.currentDate = date.setSeconds(date.getSeconds()+1);
			cDate = settings.currentDate;
			return settings.currentDate;
	    };
		
		

		/**
		 * Main countdown function that calculates everything
		 */
		function countdown () {
			var target_date = new Date(settings.date), // set target date
				current_date = currentDate(); // get fixed current date

			// difference of dates
			var difference = target_date - current_date;

			// if difference is negative than it's pass the target date
			if (difference < 0) {
				// stop timer
				clearInterval(interval);

				if (callback && typeof callback === 'function') callback();

				return;
			}

			// basic math variables
			var _second = 1000,
				_minute = _second * 60,
				_hour = _minute * 60,
				_day = _hour * 24;

			// calculate dates
			var days = Math.floor(difference / _day),
				hours = Math.floor((difference % _day) / _hour),
				minutes = Math.floor((difference % _hour) / _minute),
				seconds = Math.floor((difference % _minute) / _second);

				// fix dates so that it will show two digets
				days = (String(days).length >= 2) ? days : '0' + days;
				hours = (String(hours).length >= 2) ? hours : '0' + hours;
				minutes = (String(minutes).length >= 2) ? minutes : '0' + minutes;
				seconds = (String(seconds).length >= 2) ? seconds : '0' + seconds;

			// based on the date change the refrence wording
			var text_days = (days === 1) ? settings.day : settings.days,
				text_hours = (hours === 1) ? settings.hour : settings.hours,
				text_minutes = (minutes === 1) ? settings.minute : settings.minutes,
				text_seconds = (seconds === 1) ? settings.second : settings.seconds;

			// set to DOM
			/*$('#days').text(days);
			$('#hours').text(hours);
			$('#minutes').text(minutes);
			$('#seconds').text(seconds);

			container.find('.days_text').text(text_days);
			container.find('.hours_text').text(text_hours);
			container.find('.minutes_text').text(text_minutes);
			container.find('.seconds_text').text(text_seconds);*/
			
			if(days == 00 && hours == 00 && minutes == 00 && seconds == 00 ){
				//alert("Current draw freezed !!!");
				/*$("#side-menu").empty();
				$("#side-menu-match-list").empty();
				$("#side-menu-result-report").empty()
				$('#midPannel').empty();*/
				//alert("draw freezed");
				drawFreezeNotify();
				//updateMidPanel(0,0);
			}
		};
		

		
		// start
		interval = setInterval(countdown, 1000);
	};

})(jQuery);
