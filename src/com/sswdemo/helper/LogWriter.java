package com.sswdemo.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {

	private static LogWriter mLogWriter;

	private static String mPath;

	private static Writer mWriter;

	private static SimpleDateFormat df = new SimpleDateFormat(
			"[yy-MM-dd hh:mm:ss]: ");;

	private LogWriter(String file_path) {
		this.mPath = file_path;
		this.mWriter = null;
	}

	public static LogWriter open(String file_path) {
		if (mLogWriter == null) {
			mLogWriter = new LogWriter(file_path);
		}
		File mFile = new File(mPath);
		try {
			mWriter = new BufferedWriter(new FileWriter(mPath,true), 2048);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mLogWriter;
	}

	private LogWriter() {
	}

	public void close() {
		try {
			mWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void print(String log) {
		try {
			if (mWriter == null) {
				return;
			}
			mWriter.write(df.format(new Date()));
			mWriter.write(log);
			mWriter.write("\n");
			mWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void print(Class cls, String log) { // 如果还想看是在哪个类里可以用这个方法
		try {
			mWriter.write(df.format(new Date()));

			mWriter.write(cls.getSimpleName() + " ");
			mWriter.write(log);
			mWriter.write("\n");
			mWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}