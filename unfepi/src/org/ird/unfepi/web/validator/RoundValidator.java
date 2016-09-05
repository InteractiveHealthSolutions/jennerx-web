package org.ird.unfepi.web.validator;

import java.util.List;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Round;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class RoundValidator implements Validator{

	@Override
	public boolean supports(Class clazz) {
		return Round.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors error) {
		Round round = (Round)obj;
		
		if(round.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(round.getName())){
			error.rejectValue("name", "", "invalid or empty round name");
		}
		ServiceContext sc = Context.getServices(); 
		
		List<Round> rL;
		if(round.getRoundId() != null){
			rL = sc.getCustomQueryService().getDataByHQL("from Round where name like '" + round.getName() +"' and roundId != " + round.getRoundId());
		}
		else{
			rL = sc.getCustomQueryService().getDataByHQL("from Round where name like '" + round.getName() +"'");
		}
		
//		List<Round> rL = sc.getCustomQueryService().getDataByHQL("from Round where name like '" + round.getName() +"'");
		if (rL != null && rL.size() > 0){
			error.rejectValue("name", "", "health program of this name already exist");
		}
	}

}
