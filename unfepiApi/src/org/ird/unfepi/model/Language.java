package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "language")
public class Language {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short languageId;
	
	/** The language name. */
	@Column(length = 30, unique = true)
	private String languageName;

	public Language() {
		// TODO Auto-generated constructor stub
	}
	
	public Language(short languageId) {
		this.languageId = languageId;
	}
	/**
	 * Gets the language id.
	 *
	 * @return the language id
	 */
	public short getLanguageId() {
		return languageId;
	}

	/**
	 * Sets the language id.
	 *
	 * @param languageId the new language id
	 */
	public void setLanguageId(short languageId) {
		this.languageId = languageId;
	}

	/**
	 * Gets the language name.
	 *
	 * @return the language name
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * Sets the language name.
	 *
	 * @param languageName the new language name
	 */
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
}
