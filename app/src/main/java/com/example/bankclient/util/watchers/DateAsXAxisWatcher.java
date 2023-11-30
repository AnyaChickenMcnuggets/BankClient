package com.example.bankclient.util.watchers;

import android.content.Context;

import com.jjoe64.graphview.DefaultLabelFormatter;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper class to use date objects as x-values.
 * This will use your own Date Format or by default
 * the Android default date format to convert
 * the x-values (that has to be millis from
 * 01-01-1970) into a formatted date string.
 *
 * See the DateAsXAxis example in the GraphView-Demos project
 * to see a working example.
 *
 * @author jjoe64
 */
public class DateAsXAxisWatcher extends DefaultLabelFormatter {
    /**
     * the date format that will convert
     * the unix timestamp to string
     */
    protected final DateFormat mDateFormat;

    /**
     * calendar to avoid creating new date objects
     */
    protected final Calendar mCalendar;

    /**
     * create the formatter with the Android default date format to convert
     * the x-values.
     *
     * @param context the application context
     */
    public DateAsXAxisWatcher(Context context) {
        mDateFormat = android.text.format.DateFormat.getDateFormat(context);
        mCalendar = Calendar.getInstance();
    }

    /**
     * create the formatter with your own custom
     * date format to convert the x-values.
     *
     * @param context the application context
     * @param dateFormat custom date format
     */
    public DateAsXAxisWatcher(Context context, DateFormat dateFormat) {
        mDateFormat = dateFormat;
        mCalendar = Calendar.getInstance();
    }

    /**
     * formats the x-values as date string.
     *
     * @param value raw value
     * @param isValueX true if it's a x value, otherwise false
     * @return value converted to string
     */
    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            // format as date
            mCalendar.setTimeInMillis((long) value);
            return mDateFormat.format(mCalendar.getTimeInMillis());
        } else {
            return super.formatLabel(value, false);
        }
    }
}