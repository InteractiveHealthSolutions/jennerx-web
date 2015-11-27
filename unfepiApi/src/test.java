/*
 * 
 */


import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class test.
 */
public class test {

/**
 * The main method.
 *
 * @param args the arguments
 */
public static void main(String[] args) {
	String regx="";
	if(System.getProperty("os.name").toLowerCase().indexOf("linux")!=-1){
		regx="[a-zA-Z]:(\\\\[\\w-]+)+";
	}
	Pattern p=Pattern.compile(regx);
	Matcher m=p.matcher("c:\\jdksds-sds\\cxzjsbc");
	System.out.println(m.matches());
	
/*	System.out.println(Context.getIRSetting("child.child-id.min-length", "6"));
	try{
	Context.updateIrSetting("child.child-id.min-length", "5", null);;
	}catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println(Context.getIRSetting("child.child-id.min-length", "7"));*/
}
}
