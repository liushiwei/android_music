package com.android.music;

import java.util.ArrayList;

public class Folder {
	String mPath;
	ArrayList<String> mFiles;
	
	Folder(String path){
		mPath = path;
		mFiles = new ArrayList<String>(10);
	}
	
	public ArrayList<String> getMediaFiles(){
		return mFiles;
	}
	
	public void addFile(String file){
		mFiles.add(file);
	}
	

}
