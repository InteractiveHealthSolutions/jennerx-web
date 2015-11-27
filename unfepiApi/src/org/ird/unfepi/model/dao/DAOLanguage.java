package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Language;


public interface DAOLanguage extends DAO{

	Language getById(short languageId);
	
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Language> getAll();
	
	Language getByName(String languageName);
}
