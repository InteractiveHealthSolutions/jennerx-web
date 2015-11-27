/*
 * 
 */
package org.ird.unfepi.utils.validation;

// TODO: Auto-generated Javadoc
/**
 * The Class REG_EX.
 */
public class REG_EX {
		
	/** The Constant ALPHA. */
	public static final REG_EX ALPHA = new REG_EX("[a-zA-Z]+");
	
	/** The Constant ALPHA_NUMERIC. */
	public static final REG_EX ALPHA_NUMERIC = new REG_EX("[a-zA-Z0-9]+");
	
	/** The Constant CELL_NUMBER. */
	public static final REG_EX CELL_NUMBER = new REG_EX("(\\+92|92|0)3[0-9]{9}");
	
	/** The Constant EMAIL. */
	public static final REG_EX EMAIL = new REG_EX("((\\w+)|(\\w+\\.+\\w+)|(\\w+\\-+\\w+))\\@((\\w+)|(\\w+\\-\\w+))\\.\\w{2,4}");
	
	/** The Constant NAME_CHARACTERS. */
	public static final REG_EX NAME_CHARACTERS = new REG_EX("[a-zA-Z\\.\\-/\\s]+");
	
	/** The Constant NO_SPECIAL_CHAR. */
	public static final REG_EX NO_SPECIAL_CHAR = new REG_EX("[a-zA-Z0-9\\|/\\(\\)_,\\-\\.\\s`]+");
	
	/** The Constant NUMERIC. */
	public static final REG_EX NUMERIC = new REG_EX("[0-9]+");
	
	/** The Constant PASSWORD. */
	public static final REG_EX PASSWORD = new REG_EX("[a-zA-Z0-9@!\\|~/\\$\\*\\(\\)_\\[\\]\\{\\};:\\.\\s]+");
	
	/** The Constant PHONE_NUMBER. */
	public static final REG_EX PHONE_NUMBER = new REG_EX("(\\d+([,\\-\\s]?)\\d+)+");
	
	/** The Constant PTCL_LANDLINE_NUMBER. */
	public static final REG_EX PTCL_LANDLINE_NUMBER = new REG_EX("(\\+92|92|0)?213[0-9]{7}");
	
	/** The Constant PTCL_WIRELESS_NUMBER. */
	public static final REG_EX PTCL_WIRELESS_NUMBER = new REG_EX("(\\+92|92|0)?213[0-9]{7}");
	
	/** The Constant WHITESPACE_WORD. */
	public static final REG_EX WHITESPACE_WORD = new REG_EX("[\\w\\s]+");
	
	/** The Constant WHITESPACE_ALPHA. */
	public static final REG_EX WHITESPACE_ALPHA = new REG_EX("[a-zA-Z\\s]+");
	
	/** The Constant WHITESPACE_NUMERIC. */
	public static final REG_EX WHITESPACE_NUMERIC = new REG_EX("[0-9\\s]+");
	
	/** The Constant WHITESPACE_ALPHA_NUMERIC. */
	public static final REG_EX WHITESPACE_ALPHA_NUMERIC = new REG_EX("[a-zA-Z0-9\\s]+");
	
	/** The Constant WORD. */
	public static final REG_EX WORD = new REG_EX("\\w+");

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
		   return regexp;
		}
		
		/** The regexp. */
		private final String regexp;

		/**
		 * Instantiates a new rE g_ ex.
		 *
		 * @param exp the exp
		 */
		private REG_EX(String exp) {
			regexp=exp;
		}

}
