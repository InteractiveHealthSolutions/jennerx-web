/*import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class testDataAdd {
	public static void main(String[] args) {
		try {

			OutputStream os = new FileOutputStream(System.getProperty("user.home")+"/imrscsvfiles/datafiles/csv"+new Date().getTime()+".txt");
			os.write("testtsttttttttttchvgjfshbvajsdfhnvdfvdfsv".getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	SessionFactory sf=	new Configuration().configure().buildSessionFactory();
	Session ss=sf.openSession();
	Transaction t=ss.beginTransaction();

	User u = new User(0,"admin");
	u.setFirstName("ad");
	u.setStatus(UserStatus.ACTIVE);
	u.addRole(new DAORoleImpl(ss).findByName("admin"));
	u.setPassword("koxWMkNaSfk2QVv934zAXxgKwx6ZBaF6S3NTCbccOKo=");
	u.setEmail("maimoona.kausar@irdinformatics.org");
	
	ss.save(u);
	t.commit();
	ss.flush();



	}
}
*/