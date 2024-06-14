package pers.spj.custom.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author supeijin
 */

public final class LogUtil {
	private LogUtil() {
		//no instance
	}
	public static String getExceptionMsg(Throwable ex) {
		if(ex==null){
			return "Throwable ==null ";
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		printWriter.close();
		return writer.toString();
	}
}
