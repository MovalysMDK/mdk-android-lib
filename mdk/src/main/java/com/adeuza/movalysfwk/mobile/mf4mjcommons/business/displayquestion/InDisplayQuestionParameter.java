/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AsyncRedirectActionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.AsyncRedirectActionParameterImpl;

/**
 * <p>Parameter to display a question</p>
 *
 *
 */
//A2A_DEV changer les entiers avec autres choses que les entiers, mettre des chaînes
public class InDisplayQuestionParameter extends AsyncRedirectActionParameterImpl implements AsyncRedirectActionParameter {

	/** serial id */
	private static final long serialVersionUID = 1579859208851498645L;
	
	/** id of question code in strings xml file */
	public String questioncode = null;
	
	/** list of question result : button, label and associated action */
	public List<QuestionResult> questionResults = null;
	
	/**
	 * Construct a new parameter
	 */
	public InDisplayQuestionParameter() {
		this.questionResults = new ArrayList<QuestionResult>();
	}
	
	/**
	 * Add a question result
	 *
	 * @param p_oAction associated action
	 * @param p_sLabel associated in label in xml file
	 */
	public void addQuestionResult(ActionHandler p_oAction, String p_sLabel) {
		this.questionResults.add(new QuestionResult(p_oAction, p_sLabel));
	}
	
	/**
	 * Question result, associates a label and an action
	 */
	public class QuestionResult implements Serializable {
		
		/** serial id */
		private static final long serialVersionUID = -2463804174806884687L;

		/** action */
		public ActionHandler action = null;
		
		/** label */
		public String label = null;
		
		/**
		 * Construc a new question result
		 * @param p_oAction associated action
		 * @param p_sLabel associated label
		 */
		public QuestionResult(ActionHandler p_oAction, String p_sLabel) {
			this.action = p_oAction;
			this.label = p_sLabel;
		}
	}
}
