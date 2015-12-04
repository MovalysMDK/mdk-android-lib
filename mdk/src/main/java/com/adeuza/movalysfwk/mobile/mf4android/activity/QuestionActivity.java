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
package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTableRow;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.InDisplayQuestionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.InDisplayQuestionParameter.QuestionResult;

/**
 * <p>Activity to display question : the question, the action buttons and button actions are configurable</p>
 *
 *
 */
public class QuestionActivity extends Activity {

	/** the in parameter */
	private InDisplayQuestionParameter questionParameters = null;
	/** 'lapplication courante */
	private AndroidApplication application = null;
	
	/**
	 * Create an activity with question and buttons
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		
		this.application = (AndroidApplication)(Application.getInstance());
		
		questionParameters = (InDisplayQuestionParameter) this.getIntent().getExtras().get("parameterIn");
		//on utilise le screen_question comme base
		this.setContentView(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_screen_question));
		TextView oQuestionView = (TextView)this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.screen_question_text));
		oQuestionView.setText(questionParameters.questioncode);

		//on ajoute les bouttons en inflatant le composant button
		MMTableRow oButtonLayout = (MMTableRow)this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.screen_question_button_layout));
		LayoutInflater oInflater=this.getLayoutInflater();
		
		Button oButton = null;
		for(QuestionResult oQuestionResult : questionParameters.questionResults) {
			oButton = (Button) oInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_button_question),null);
			oButton.setText(oQuestionResult.label);
			oButton.setOnClickListener(new QuestionButtonClickListener(oQuestionResult.action));
			oButtonLayout.addView(oButton);
		}
	}
	
	/**
	 * Click listener for button. The listener associated an action to a button
	 */
	private class QuestionButtonClickListener implements OnClickListener {
		
		/** the action to associated */
		private ActionHandler action = null;
		
		/**
		 * Constructs a new listener
		 * @param p_oAction the action to associated
		 */
		public QuestionButtonClickListener(ActionHandler p_oAction) {
			this.action = p_oAction;
		}

		/**
		 * Close activity and call an action
		 * {@inheritDoc}
		 */
		@Override
		public void onClick(View p_oArg0) {
			QuestionActivity.this.finish();
			//SMA : pour garder la compatibilité
			try {
				Application.getInstance().getController().doOnAsyncActionEnd(questionParameters.getActionId(), action);
			} catch (ActionException e) {
				Log.e("QA", "QA", e);
			}
		}
		
		
	}
}
