/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

/**
 * @author Safwan
 *
 */
public class WomenDashboardController extends DataDisplayController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		Map<String, Object> model = new HashMap<String, Object>();
		LoggedInUser user = UserSessionUtils.getActiveUser(req);
		
		String programId = req.getParameter("womenId");
		ServiceContext sc = Context.getServices();
		try{
			if(!StringUtils.isEmptyOrWhitespaceOnly(programId)){
				Women women = sc.getWomenService().findById(Integer.parseInt(programId));
				if(women != null){
					String hqlvaccination = "FROM WomenVaccination v " +
							" LEFT JOIN FETCH v.vaccine vc " +
							" LEFT JOIN FETCH v.vaccinationCenter center " +
							" LEFT JOIN FETCH center.idMapper centerId " +
							" LEFT JOIN FETCH v.vaccinator vctor " +
							" LEFT JOIN FETCH vctor.idMapper vctorId " +
							" WHERE v.womenId = " + women.getMappedId() + 
							" ORDER BY IFNULL(vaccinationDate, 99999999999999) ASC, vaccinationDuedate ASC ";
					List vaccinations = sc.getCustomQueryService().getDataByHQL(hqlvaccination );
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("child", women);
					map.put("address", sc.getDemographicDetailsService().getAddress(women.getMappedId(), true, null));
					map.put("contacts", sc.getDemographicDetailsService().getContactNumber(women.getMappedId(), true, null));
					map.put("vaccinations", vaccinations);
					addModelAttribute(model, "datalist", map);
				}
				else {
					addModelAttribute(model, "errorMessage", "No women found with given id.");
				}
			}
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return null;
	}

}
