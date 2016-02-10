package com.adeuza.movalysfwk.mobile.mf4android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMButton;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTableRow;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionException;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.action.ActionHandler;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.InDisplayQuestionParameter;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.business.displayquestion.InDisplayQuestionParameter.QuestionResult;

/**
 * <p>Activity to display question : the question, the action buttons and button actions are configurable</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
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
		MMTextView oQuestionView = (MMTextView)this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.screen_question_text));
		oQuestionView.setText(questionParameters.questioncode);

		//on ajoute les bouttons en inflatant le composant button
		MMTableRow oButtonLayout = (MMTableRow)this.findViewById(this.application.getAndroidIdByRKey(AndroidApplicationR.screen_question_button_layout));
		LayoutInflater oInflater=this.getLayoutInflater();
		
		MMButton oButton = null;
		for(QuestionResult oQuestionResult : questionParameters.questionResults) {
			oButton = (MMButton) oInflater.inflate(this.application.getAndroidIdByRKey(AndroidApplicationR.fwk_component_button_question),null);
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
