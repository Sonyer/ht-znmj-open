Date.prototype.Format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}

/**
 * 获取当前时间字符串
 */
function getNow() {
	var datetime = new Date();
	return datetime.Format('yyyy-MM-dd hh:mm:ss');
}

/**
 * 获取当日
 */
function getToday() {
	var datetime = new Date();
	return datetime.Format('yyyy-MM-dd');
}

/**
 * 当前时间增加指定天数，负数则是减去指定天数
 * 
 * @param day
 * @returns
 */
function getNowAddDay(day) {
	var datetime = new Date();
	var times = datetime.getTime() + (day * 24 * 60 * 60 * 1000);
	var date = new Date(times);
	return date.Format('yyyy-MM-dd hh:mm:ss');
}

/**
 * 当前时间增加指定天数，负数则是减去指定天数
 * 
 * @param day
 * @returns
 */
function getTodayAddDay(day) {
	var datetime = new Date();
	var times = datetime.getTime() + (day * 24 * 60 * 60 * 1000);
	var date = new Date(times);
	return date.Format('yyyy-MM-dd');
}

/**
 * 根据传入日期加减指定天数，返回指定格式的日期
 * @param strDate
 * @param day
 * @param strFormat
 * @returns
 */
function getNewDay(strDate, day, strFormat) {
	var datetime = new Date(strDate);
	var times = datetime.getTime() + (day * 24 * 60 * 60 * 1000);
	var date = new Date(times);
	return date.Format(strFormat);
}