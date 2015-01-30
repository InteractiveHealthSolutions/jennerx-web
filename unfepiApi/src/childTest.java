import java.util.Iterator;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.IdentifierType;


public class childTest {

	public static void main(String[] args) throws InstanceAlreadyExistsException {
		
		Context.instantiate(null);
		ServiceContext sc = Context.getServices();
		
		Child child = new Child();
		child=sc.getChildService().findChildById("01001120620005", false, new String[]{"idMapper"});
		//a valid child should be provided
							
		IdMapper childIdMapper = child.getIdMapper();
		List<Identifier> identifiers = childIdMapper.getIdentifiers();
		Iterator< Identifier> idIterator = identifiers.iterator();
		while(idIterator.hasNext())
		{
			//an old id was found
			Identifier oldIdentifer = idIterator.next();
			oldIdentifer.setPreferred(false);
		}
		
		//Create the new Identifier
		Identifier newIdentifier = new Identifier();
		newIdentifier.setIdentifier("10000000101020");
		IdentifierType idtyp = new IdentifierType();
		idtyp.setIdentifierTypeId(1);
		newIdentifier.setIdentifierType(idtyp);
		newIdentifier.setPreferred(true);
		newIdentifier.setIdMapper(childIdMapper);
		
		//Add the new identifier to the list maintained by IdMapper object
		identifiers.add(newIdentifier);
		childIdMapper.setIdentifiers(identifiers);
		sc.getIdMapperService().updateIdMapper(childIdMapper);
		sc.commitTransaction();
		/*Session ss = Context.getNewSession();
		try{
			ss.createSQLQuery("CREATE TEMPORARY TABLE table2 AS (SELECT NOW())").executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try{
			ss.createSQLQuery("CREATE TEMPORARY TABLE table2 AS (SELECT NOW())").executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try{
			ss.createSQLQuery("DROP TEMPORARY TABLE table2").executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ss.clear();
		ss.close();
		
		Session ss1 = Context.getNewSession();
		try{
			ss1.createSQLQuery("CREATE TEMPORARY TABLE table2 AS (SELECT NOW())").executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try{
			ss1.createSQLQuery("CREATE TEMPORARY TABLE table2 AS (SELECT NOW())").executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		ss1.close();*/
		
	}
}
