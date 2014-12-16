package com.spixsoftware.spixlibrary.tools;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SpixCursorWrapper extends CursorWrapper {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public SpixCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Date getDateFromMillis(int columnIndex) {
		if (isNull(columnIndex)) {
			return null;
		}
		return new Date(getLong(columnIndex));
	}

	public BigDecimal getBigDecimal(int columnIndex) {
		if (!isNull(columnIndex)) {
			return new BigDecimal(getString(columnIndex));
		} else {
			return null;
		}
	}

	public Boolean getBoolean(int columnIndex) {
		return getInt(columnIndex) > 0;
	}

	public List<String> getListOfStrings(int columnIndex) {
		if (moveToFirst()) {
			List<String> items = new ArrayList<String>(getCount());
			do {
				items.add(getString(columnIndex));
			} while (moveToNext());
			return items;
		}
		return new ArrayList<String>(0);
	}

	/**
	 * @return the column value like(one,two,three) splitted by delimiter
	 */
	public List<String> getListFromArrayColumn(int columnIndex, String delimiter) {
		if (isNull(columnIndex)) {
			return new ArrayList<String>(0);
		}
		String[] values = getString(columnIndex).split(delimiter);
		return new ArrayList<String>(Arrays.asList(values));
	}

	public List<Long> getListOfLongs(int columnIndex) {
		if (moveToFirst()) {
			List<Long> items = new ArrayList<Long>(getCount());
			do {
				items.add(getLong(columnIndex));
			} while (moveToNext());
			return items;
		}
		return new ArrayList<>(0);
	}


}
