package com.spixsoftware.spixlibrary.main.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataBaseUtils {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final String CLEAR_TABLE_FORMAT_QUERY = "DELETE FROM %s";

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param columnValues
	 *            - used in where clouse
	 */
	public static boolean isRowExists(SQLiteDatabase db, String tableName, Map<String, String> columnValues) {
		StringBuilder query = new StringBuilder("SELECT rowid FROM " + tableName + " WHERE ");
		List<String> args = new ArrayList<String>(columnValues.size());
		for (Entry<String, String> columnValue : columnValues.entrySet()) {
			query.append(columnValue.getKey() + " = ? ");
			args.add(columnValue.getValue());
		}
		Cursor cursor = db.rawQuery(query.toString(), args.toArray(new String[args.size()]));
		try {
			return cursor.getCount() > 0;
		} finally {
			cursor.close();
		}
	}

	public static void dropTable(SQLiteDatabase db, String tableName) {
		db.execSQL("DROP TABLE '" + tableName + "'");
	}

	public static void dropTableIfExists(SQLiteDatabase db, String tableName) {
		db.execSQL("DROP TABLE IF EXISTS '" + tableName + "'");
	}

	public static void dropTablesIfExists(SQLiteDatabase db, String[] tableNames) {
		db.beginTransaction();
		try {
			for (String tableName : tableNames) {
				dropTableIfExists(db, tableName);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void addColumnIfNotExist(SQLiteDatabase db, String tableName, String columnName, String columnType) {
		Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ");", null);
		boolean isColumnExist = false;
		try {
			final int iName = cursor.getColumnIndex("name");
			if (cursor.moveToFirst()) {
				do {
					String column = cursor.getString(iName);
					if (column.equals(columnName)) {
						isColumnExist = true;
						break;
					}
				} while (cursor.moveToNext());
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (!isColumnExist) {
			db.execSQL("ALTER TABLE " + tableName + " ADD " + columnName + " " + columnType + "");
		}
	}

	public static void clearTablesIfExists(SQLiteDatabase db, String[] tableNames) {
		if (tableNames == null || tableNames.length == 0) {
			return;
		}
		db.beginTransaction();
		try {
			for (String tableName : tableNames) {
				clearTableIfExists(db, tableName);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void clearTableIfExists(SQLiteDatabase db, String tableName) {
		if (isTableExists(db, tableName)) {
			clearTable(db, tableName);
		}
	}

	public static void clearTable(SQLiteDatabase db, String tableName) {
		db.execSQL(String.format(CLEAR_TABLE_FORMAT_QUERY, tableName));
	}

	public static boolean isTableExists(SQLiteDatabase db, String tableName) {
		Cursor cursor = db.rawQuery("SELECT count(*) AS isTable FROM sqlite_master WHERE type='table' AND name=?", new String[] { tableName });
		final int iIsTable = cursor.getColumnIndexOrThrow("isTable");
		try {
			if (cursor.moveToFirst()) {
				return ObjectConverter.intToBoolean(cursor.getInt(iIsTable));
			}
		} finally {
			if (cursor != null) {
                cursor.close();
			}
		}
		return false;

	}

	public static SQLiteStatement makeInsertStatement(SQLiteDatabase db, String tableName, String[] columns) {
		String insertStatement = "INSERT INTO " + tableName + " ( " + TextUtils.join(", ", columns) + " ) VALUES (" + makeHooksForValues(columns.length) + ")";
		return db.compileStatement(insertStatement);
	}

	public static SQLiteStatement makeInsertOrReplaceStatement(SQLiteDatabase db, String tableName, String[] columns) {
		String insertOrReplaceStatementQuery = "INSERT OR REPLACE INTO " + tableName + " (" + TextUtils.join(", ", columns) + ") VALUES (" + DataBaseUtils.makeHooksForValues(columns.length) + ")";
		return db.compileStatement(insertOrReplaceStatementQuery);
	}

	public static SQLiteStatement makeReplaceStatement(SQLiteDatabase db, String tableName, String[] columns) {
		String insertOrReplaceStatementQuery = "REPLACE INTO " + tableName + " (" + TextUtils.join(", ", columns) + ") VALUES (" + DataBaseUtils.makeHooksForValues(columns.length) + ")";
		return db.compileStatement(insertOrReplaceStatementQuery);
	}

	/**
	 * @return ?,?,?
	 */
	public static String makeHooksForValues(int count) {
		StringBuilder hooks = new StringBuilder();
		hooks.append("?");
		for (int i = 1; i < count; i++) {
			hooks.append(",?");
		}
		return hooks.toString();
	}



	public static <T> void dropColumn(SQLiteDatabase db, String tableName, String[] colsToRemove, TableCreator creator) {
		db.beginTransaction();
		try {
			List<String> updatedTableColumns = getTableColumns(db, tableName);
			// Remove the columns we don't want anymore from the table's list of
			// columns
			updatedTableColumns.removeAll(Arrays.asList(colsToRemove));

			String columnsSeperated = TextUtils.join(",", updatedTableColumns);

			db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

			// Creating the table on its new format (no redundant columns)
			creator.createTable(db, tableName);

			// Populating the table with the data
			db.execSQL("INSERT INTO " + tableName + "(" + columnsSeperated + ") SELECT " + columnsSeperated + " FROM " + tableName + "_old;");
			db.execSQL("DROP TABLE " + tableName + "_old;");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static List<String> getTableColumns(SQLiteDatabase db, String tableName) {
		ArrayList<String> columns = new ArrayList<String>();
		String cmd = "pragma table_info(" + tableName + ");";
		Cursor cur = db.rawQuery(cmd, null);

		while (cur.moveToNext()) {
			columns.add(cur.getString(cur.getColumnIndex("name")));
		}
		cur.close();

		return columns;
	}

    /**
     * @return  "'id_1','id2'"
     */
	public static String prepareListForInQuery(Collection<String> items) {
		if (items == null || items.size() == 0) {
			return null;
		}
		StringBuilder itemBuilder = new StringBuilder("'");
		for (String item : items) {
			itemBuilder.append(item + "','");
		}
		itemBuilder.deleteCharAt(itemBuilder.length() - 1);
		itemBuilder.deleteCharAt(itemBuilder.length() - 1);
		return itemBuilder.toString();
	}

    /**
     * Used to search values in FTS Tables with "mach" function
     * @return  builded wildcard for given value
     */
	public static String makeMatchFunctionWildcard(String query) {
		if (TextUtils.isEmpty(query))
			return query;

		final StringBuilder builder = new StringBuilder();
		final String[] splits = TextUtils.split(query, " ");

		for (String split : splits)
			builder.append(split).append("*").append(" ");

		String result = builder.toString().trim();
		result = result.replace("'", "\'");
		result = result.replace("\"", "\\\"");
		return result;
	}

    public static String makeLikeFunctionWildcard(String query) {
        return "%" + query + "%";
    }

	public static String getLastInsertId(SQLiteDatabase db) {
		String id = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select last_insert_rowid()", null);
			id = cursor.getString(0);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return id;
	}


     /**
     * @return column1 = ? , column2 = ?
     */
	public static String makeValuesWithHooks(String[] columns) {
		String result = TextUtils.join(" = ?,", columns);
		return result + " = ?";
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface TableCreator {
		public void createTable(SQLiteDatabase db, String tableName);
	}

}
