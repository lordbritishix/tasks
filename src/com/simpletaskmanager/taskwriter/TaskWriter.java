package com.simpletaskmanager.taskwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;
import android.util.Log;

import com.simpletaskmanager.data.Task;

public class TaskWriter {
	private static TaskWriter m_instance = new TaskWriter();
	public static final String FILENAME = "tasks.txt";
	
	private TaskWriter() {
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		throw new CloneNotSupportedException();
	}
	
	public static TaskWriter getInstance() {
		return m_instance;
	}

	private boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	
	public boolean exportTasksToFile(Task[] data) {
		Log.i("Info", "Attempting to export to file");
		if (!isExternalStorageWritable()) {
			Log.i("Info", "Storage not writable");
			return false;
		}
		
		File root =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); 
		
		if (!root.exists()) {
			root.mkdirs();
		}
		
		File file = new File(root, FILENAME);
		
		if (file.exists()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e) {
		}

		if (!file.exists()) {
			return false;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fos);
			for (int x = 0; x < data.length; ++x) {
				Log.i("Info", "Writing data: " + data[x].toString());
				pw.println(data[x].toString());
			}
			
			pw.flush();
			pw.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	    return true;
	}

	
}
