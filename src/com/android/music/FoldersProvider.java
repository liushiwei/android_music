package com.android.music;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;  
  
public class FoldersProvider extends ContentProvider {  
        private static final String LOG_TAG = "shy.luo.providers.Folders.FolderProvider";  
  
        private static final String DB_NAME = "Folders.db";  
        private static final String DB_TABLE = "ArticlesTable";  
        private static final int DB_VERSION = 1;  
  
        private static final String DB_CREATE = "create table " + DB_TABLE +  
                                " (" + Folders.ID + " integer primary key autoincrement, " +  
                                Folders.TITLE + " text not null, " +  
                                Folders.ABSTRACT + " text not null, " +  
                                Folders.URL + " text not null);";  
  
        private static final UriMatcher uriMatcher;  
        static {  
                uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
                uriMatcher.addURI(Folders.AUTHORITY, "item", Folders.ITEM);  
                uriMatcher.addURI(Folders.AUTHORITY, "item/#", Folders.ITEM_ID);  
                uriMatcher.addURI(Folders.AUTHORITY, "pos/#", Folders.ITEM_POS);  
        }  
  
        private static final HashMap<String, String> articleProjectionMap;  
        static {  
                articleProjectionMap = new HashMap<String, String>();  
                articleProjectionMap.put(Folders.ID, Folders.ID);  
                articleProjectionMap.put(Folders.TITLE, Folders.TITLE);  
                articleProjectionMap.put(Folders.ABSTRACT, Folders.ABSTRACT);  
                articleProjectionMap.put(Folders.URL, Folders.URL);  
        }  
  
        private DBHelper dbHelper = null;  
        private ContentResolver resolver = null;  
  
        @Override  
        public boolean onCreate() {  
                Context context = getContext();  
                resolver = context.getContentResolver();  
                dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);  
  
                Log.i(LOG_TAG, "Folders Provider Create");  
  
                return true;  
        }  
  
        @Override  
        public String getType(Uri uri) {  
                switch (uriMatcher.match(uri)) {  
                case Folders.ITEM:  
                        return Folders.CONTENT_TYPE;  
                case Folders.ITEM_ID:  
                case Folders.ITEM_POS:  
                        return Folders.CONTENT_ITEM_TYPE;  
                default:  
                        throw new IllegalArgumentException("Error Uri: " + uri);  
                }  
        }  
  
        @Override  
        public Uri insert(Uri uri, ContentValues values) {  
                if(uriMatcher.match(uri) != Folders.ITEM) {  
                        throw new IllegalArgumentException("Error Uri: " + uri);  
                }  
  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
  
                long id = db.insert(DB_TABLE, Folders.ID, values);  
                if(id < 0) {  
                        throw new SQLiteException("Unable to insert " + values + " for " + uri);  
                }  
  
                Uri newUri = ContentUris.withAppendedId(uri, id);  
                resolver.notifyChange(newUri, null);  
  
                return newUri;  
        }  
  
        @Override  
        public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
                int count = 0;  
  
                switch(uriMatcher.match(uri)) {  
                case Folders.ITEM: {  
                        count = db.update(DB_TABLE, values, selection, selectionArgs);  
                        break;  
                }  
                case Folders.ITEM_ID: {  
                        String id = uri.getPathSegments().get(1);  
                        count = db.update(DB_TABLE, values, Folders.ID + "=" + id  
                                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);  
                        break;  
                }  
                default:  
                        throw new IllegalArgumentException("Error Uri: " + uri);  
                }  
  
                resolver.notifyChange(uri, null);  
  
                return count;  
        }  
  
        @Override  
        public int delete(Uri uri, String selection, String[] selectionArgs) {  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
                int count = 0;  
  
                switch(uriMatcher.match(uri)) {  
                case Folders.ITEM: {  
                        count = db.delete(DB_TABLE, selection, selectionArgs);  
                        break;  
                }  
                case Folders.ITEM_ID: {  
                        String id = uri.getPathSegments().get(1);  
                        count = db.delete(DB_TABLE, Folders.ID + "=" + id  
                                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);  
                        break;  
                }  
                default:  
                        throw new IllegalArgumentException("Error Uri: " + uri);  
                }  
  
                resolver.notifyChange(uri, null);  
  
                return count;  
        }  
  
        @Override  
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {  
                Log.i(LOG_TAG, "FolderProvider.query: " + uri);  
  
                SQLiteDatabase db = dbHelper.getReadableDatabase();  
  
                SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();  
                String limit = null;  
  
                switch (uriMatcher.match(uri)) {  
                case Folders.ITEM: {  
                        sqlBuilder.setTables(DB_TABLE);  
                        sqlBuilder.setProjectionMap(articleProjectionMap);  
                        break;  
                }  
                case Folders.ITEM_ID: {  
                        String id = uri.getPathSegments().get(1);  
                        sqlBuilder.setTables(DB_TABLE);  
                        sqlBuilder.setProjectionMap(articleProjectionMap);  
                        sqlBuilder.appendWhere(Folders.ID + "=" + id);  
                        break;  
                }  
                case Folders.ITEM_POS: {  
                        String pos = uri.getPathSegments().get(1);  
                        sqlBuilder.setTables(DB_TABLE);  
                        sqlBuilder.setProjectionMap(articleProjectionMap);  
                        limit = pos + ", 1";  
                        break;  
                }  
                default:  
                        throw new IllegalArgumentException("Error Uri: " + uri);  
                }  
  
                Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, TextUtils.isEmpty(sortOrder) ? Folders.DEFAULT_SORT_ORDER : sortOrder, limit);  
                cursor.setNotificationUri(resolver, uri);  
  
                return cursor;  
        }  
    
        @Override  
        public Bundle call(String method, String request, Bundle args) {  
                Log.i(LOG_TAG, "FolderProvider.call: " + method);  
  
                if(method.equals(Folders.METHOD_GET_ITEM_COUNT)) {  
                        return getItemCount();  
                }  
  
                throw new IllegalArgumentException("Error method call: " + method);  
        }  
  
        private Bundle getItemCount() {  
                Log.i(LOG_TAG, "FolderProvider.getItemCount");  
  
                SQLiteDatabase db = dbHelper.getReadableDatabase();  
                Cursor cursor = db.rawQuery("select count(*) from " + DB_TABLE, null);  
  
                int count = 0;  
                if (cursor.moveToFirst()) {  
                        count = cursor.getInt(0);  
                }  
  
                Bundle bundle = new Bundle();  
                bundle.putInt(Folders.KEY_ITEM_COUNT, count);  
  
                cursor.close();  
                db.close();  
  
                return bundle;  
        }  
  
        private static class DBHelper extends SQLiteOpenHelper {  
                public DBHelper(Context context, String name, CursorFactory factory, int version) {  
                        super(context, name, factory, version);  
                }  
  
                @Override  
                public void onCreate(SQLiteDatabase db) {  
                        db.execSQL(DB_CREATE);  
                }  
  
                @Override  
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
                        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);  
                        onCreate(db);  
                }  
        }  
}  