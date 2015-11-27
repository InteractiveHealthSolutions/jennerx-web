package org.ird.unfepi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hibernate.Session;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.model.User;

public class FileUtils {
	
	public static File getFile(String fileUniqueName, String additionalDirPath) throws IOException{
		String finalpath = "";
		
		String dataFilePath = Context.getSetting("downloadable.report-path", System.getProperty("user.home"));
		File dfile = new File(dataFilePath);
		if(!dfile.exists()){
			dataFilePath = System.getProperty("user.home");
		}
		if(!dataFilePath.endsWith(System.getProperty("file.separator"))){
			dataFilePath += System.getProperty("file.separator");
		}
		
		finalpath += dataFilePath;
		
		if(additionalDirPath.startsWith(System.getProperty("file.separator"))){
			additionalDirPath = additionalDirPath.substring(additionalDirPath.indexOf(System.getProperty("file.separator")));
		}
		
		if(!additionalDirPath.endsWith(System.getProperty("file.separator"))){
			additionalDirPath += System.getProperty("file.separator");
		}
		
		finalpath += additionalDirPath;
		
		if(fileUniqueName.startsWith(System.getProperty("file.separator"))){
			fileUniqueName = fileUniqueName.substring(fileUniqueName.indexOf(System.getProperty("file.separator")));
		}
		
		finalpath += fileUniqueName;
		
		File file = new File(finalpath);
		if(!file.exists()){
			File par = new File(file.getParent());
			if(!par.exists()){
				par.mkdirs();
			}
			
			file.createNewFile();
		}
		
		return file;
	}
}
