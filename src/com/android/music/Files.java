package com.android.music;

import android.net.Uri;

public class Files {
	public static final String ID = "_id";
	public static final String PATH = "_path";
	public static final String FOLDER_ID = "folder_id";
	public static final String FILE_TYPE = "file_type";
	public static final String DISK_ID = "disk_id";
	public static final String FOLDER = "folder_name";

	/* Default sort order */
	public static final String DEFAULT_SORT_ORDER = "_id asc";

	/* Call Method */
	public static final String METHOD_GET_ITEM_COUNT = "METHOD_GET_ITEM_COUNT";
	public static final String KEY_ITEM_COUNT = "KEY_ITEM_COUNT";

	/* Authority */
	public static final String AUTHORITY = "com.george.providers.files";

	/* Match Code */
	public static final int ITEM = 1;
	public static final int ITEM_ID = 2;
	public static final int ITEM_DISK = 3;
	public static final int ITEM_FILE_TYPE = 4;

	/* MIME */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.george.providers.files";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.george.providers.files";

	/* Content URI */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
	public static final Uri CONTENT_DISK_URI = Uri.parse("content://" + AUTHORITY + "/disk");
	public static final Uri CONTENT_FILE_TYPE_URI = Uri.parse("content://" + AUTHORITY + "/type");
	public static final int FILE_TYPE_FOLDER = 0;
	public static final int FILE_TYPE_AUDIO = 1;
	public static final int FILE_TYPE_IMAGE = 2;
	public static final int FILE_TYPE_VIDEO = 3;
	
}
