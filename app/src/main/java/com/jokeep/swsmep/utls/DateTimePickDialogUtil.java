package com.jokeep.swsmep.utls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.jokeep.swsmep.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间选择控件 使用方法： private EditText inputDate;//需要设置的日期时间文本编辑框 private String
 * initDateTime="2012年9月3日 14:44",//初始日期时间值 在点击事件中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 *
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 *           dateTimePicKDialog=new
 *           DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 *           dateTimePicKDialog.dateTimePicKDialog(inputDate);
 *
 *           } });
 *
 * @author
 */
public class DateTimePickDialogUtil implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Activity activity;
	private static String datesizetime;
	private int sizeint;

	/**
	 * 日期时间弹出选择框构造函数
	 *
	 * @param activity
	 *            ：调用的父activity
	 * @param initDateTime
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DateTimePickDialogUtil(Activity activity, String initDateTime,int sizeint) {
		this.activity = activity;
		this.initDateTime = initDateTime;
		this.sizeint = sizeint;
	}

	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "年"
					+ calendar.get(Calendar.MONTH) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
		}

		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	/**
	 * 弹出日期时间选择框方法
	 *
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog dateTimePicKDialog(final TextView inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		init(datePicker, timePicker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(this);

		ad = new AlertDialog.Builder(activity)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (sizeint == 2) {
							try {
								int intsize = DateCompare(initDateTime, datesizetime);
								if (intsize == 1) {
									Toast.makeText(activity, "结束时间不能小于开始时间...", Toast.LENGTH_SHORT).show();
									dateTime = "请选择结束时间";
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {

						}
						inputDate.setText(dateTime);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText("");
					}
				}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}
	public static int DateCompare(String s1,String s2) throws Exception {
		int i = 0;
		//设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		//得到指定模范的时间
		Date d1 = sdf.parse(s1);
		Date d2 = sdf.parse(s2);
		int l = (int)(d1.getTime() - d2.getTime());
		if(l > 0) {
			i = 1;
		}else{
			i = 0;
		}
		return i;
	}
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

		dateTime = sdf.format(calendar.getTime());
		datesizetime = dateTime;
		String stringyear = dateTime.substring(0, 11);
		String week = getWeek(stringyear);
		String stringhour = dateTime.substring(12, 17);
		dateTime = stringyear +" "+week +" "+ stringhour;
		ad.setTitle(dateTime);
	}
	public static String SizeTime(){
		return datesizetime;
	}
	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 *
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期
		String time = spliteString(initDateTime, "日", "index", "back"); // 时间

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		String hourStr = spliteString(time, ":", "index", "front"); // 时
		String minuteStr = spliteString(time, ":", "index", "back"); // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute);
		return calendar;
	}

	/**
	 * 截取子串
	 *
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
									  String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}
	/**
	 * 判断当前日期是星期几
	 *
	 * @param  pTime     设置的需要判断的时间  //格式如2012-09-08
	 *
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */

//  String pTime = "2012-03-12";
	private String getWeek(String pTime) {
		String Week = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "星期天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "星期一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "星期二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "星期三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "星期四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "星期五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "星期六";
		}
		return Week;
	}
}
