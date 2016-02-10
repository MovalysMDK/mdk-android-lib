package com.adeuza.movalysfwk.mobile.mf4android.ui.command;

import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4android.activity.AbstractMMActivity;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplication;
import com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR;
import com.adeuza.movalysfwk.mobile.mf4android.ui.views.MMTextView;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;


/**
 * <p>
 * 	Classe de gestion de l'impression android.
 * 	Affichage d'une popup pour représenter le traitement en cours.
 * </p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author dmaurange
 * @since MF-Annapurna
 */
public abstract class AbstractBtPrintCommand extends AbstractMMActivity implements OnClickListener{
	/** Tag */
	private static final String TAG = "BtPrintCommand";
	/** Cle de rotation */
	private static final String ROTATION_KEY1 = "printTask";

	/** the context*/
	private AndroidApplication oApplication=(AndroidApplication)Application.getInstance();

	/**the ressources*/
	//private Resources oResources;
	
	/** the object to print*/
	private PrintableViewModel objectToPrint=null;
	
	/** the reference field in the dialog */
	private MMTextView oUiReference;
	/** the message field in the dialog */
	private MMTextView oUiMessage;
	/** the error field in the dialog */
	private MMTextView oUiError;
	/** parent of the error message*/
	private View oUiErrorParent;

	/** The ok button */
	private View oUiCloseButton;
	
	/** the code used to get the result of the print in the caller activity*/
	private int resultCode;
	
	/**the print is delegated in an asynch task to avoid ANR*/
	private PrintAsyncTask oTask;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle p_oSavedInstanceState) {
		//oResources=((Activity)AbstractBtPrintCommand.this.oApplication.peekCurrentVisibleScreenFromStack()).getResources();
		super.onCreate(p_oSavedInstanceState);
		objectToPrint=Application.getInstance().getTranscientObjectFromCache("printableObject", PrintableViewModel.class);
		this.setContentView(this.getViewId());
		this.setTitle(Application.getInstance().getStringResource(AndroidApplicationR.component_btprint__btprintDialog__title));
		View oUiRefLabel = this.findViewById(this.oApplication.getAndroidIdByRKey(AndroidApplicationR.component_btprint__btprintDialog_reference__labelui));
		oUiReference = (MMTextView) this.findViewById(this.oApplication.getAndroidIdByRKey(AndroidApplicationR.component_btprint__btprintDialog_reference__value));
		oUiMessage = (MMTextView) this.findViewById(this.oApplication.getAndroidIdByRKey(AndroidApplicationR.component_btprint__btprintDialog_message__value));
		oUiError = (MMTextView) this.findViewById(this.oApplication.getAndroidIdByRKey(AndroidApplicationR.component_btprint__btprintDialog_error__value));
		oUiCloseButton = (View) this.findViewById(this.oApplication.getAndroidIdByRKey(AndroidApplicationR.component_btprint__btprintDialog_close__button));
		oUiCloseButton.setOnClickListener(this);
		
		oUiErrorParent = (View)oUiError.getParent();
		if (oUiErrorParent!=null){
			oUiErrorParent.setVisibility(View.INVISIBLE);
		}
		
		String sReference=objectToPrint.getReference();
		if (sReference==null) {
			oUiRefLabel.setVisibility(View.INVISIBLE);
			oUiReference.setVisibility(View.INVISIBLE);
		}
		else {
			oUiRefLabel.setVisibility(View.VISIBLE);
			oUiReference.setVisibility(View.VISIBLE);
			oUiReference.setText(sReference);
		}
		Map<String, Object> oRnci = (Map<String, Object>) this.getLastCustomNonConfigurationInstance();
		if (oRnci==null || !oRnci.containsKey(ROTATION_KEY1)) {
			//pré-intialisation de l'application 
			oTask=new PrintAsyncTask();
			((AndroidApplication) Application.getInstance()).execAsyncTask(oTask, objectToPrint);
		}
		else{
			//cas d'une rotation
			Object oTaskHandler = oRnci.get(ROTATION_KEY1);
			if (oTaskHandler!=null) {
				this.oTask = (PrintAsyncTask) oTaskHandler;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> onRetainCustomNonConfigurationInstance() {
		Map<String, Object> r_oMap = (Map<String, Object>) super.onRetainCustomNonConfigurationInstance();
		r_oMap.put(ROTATION_KEY1, this.oTask);
		return r_oMap;
	}
	
	
	
	/**
	 * Method to specified a custom view for this component
	 * default is component_btprint_dialog
	 * <b>overload must respect the structure : error must have a parent that can be Visible or Gone</b>
	 * @return the id of the layout
	 */
	protected int getViewId(){
		return this.oApplication.getAndroidIdByRKey(AndroidApplicationR.fwk_component_btprint_dialog);
	}
	
	/**
	 * <p>Task asynchrone d'impression</p>
	 *
	 * <p>Copyright (c) 2011</p>
	 * <p>Company: Adeuza</p>
	 *
	 * @author dmaurange
	 * @since MF-Annapurna
	 */
	private class PrintAsyncTask extends AsyncTask<PrintableViewModel, ProgressMessage, Integer>{
		private ProgressMessage message=new ProgressMessage(0, "", null);
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// connection
			message.iErrorCode = AbstractBtPrintCommand.this.connect();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Integer doInBackground(PrintableViewModel... p_oPrintableViewModels) {
			int iCptParams=0;
			
			PrintableViewModel oPrintVM=null;
			
			//la connexion s'est mal passée lors de la préparation
			if (message.iErrorCode != getPrinterOKCode()){
				// error message
				message.sMessage=StringUtils.concat(AbstractBtPrintCommand.this.oApplication.getStringResource(AndroidApplicationR.component_btprint__btprintDialog_PRINTER_ERR_OPEN_CONN__error),
						" [",String.valueOf(message.iErrorCode),"]");
				//oMessage.sErrorMessage = "Error [" + oMessage.iErrorCode + " ]";
				oApplication.getLog().error(this.getClass().getSimpleName(), "Error in bluetooth connection, can't print");
				publishProgress(message);
				
			} else {
				while (message.iErrorCode==getPrinterOKCode() && iCptParams < p_oPrintableViewModels.length){
					oPrintVM=p_oPrintableViewModels[iCptParams];
					
					//gets the status of the printer before printing
					message.iErrorCode=AbstractBtPrintCommand.this.getStatus();
					if (message.iErrorCode!=getPrinterOKCode()){
						message.sMessage=StringUtils.concat(
								AbstractBtPrintCommand.this.oApplication.getStringResource(AndroidApplicationR.component_btprint__btprintDialog_error__label),
								" [",String.valueOf(message.iErrorCode),"]");
						//oMessage.sErrorMessage="Error ["+oMessage.iErrorCode+" ]";
						oApplication.getLog().debug(TAG,StringUtils.concat("ERROR  [ ", String.valueOf(message.iErrorCode), "] "));
						publishProgress(message);
						break;
					}
					
					message.iErrorCode=AbstractBtPrintCommand.this.print(oPrintVM);
					if (message.iErrorCode!=getPrinterOKCode()){
						message.sMessage=StringUtils.concat(AbstractBtPrintCommand.this.oApplication.getStringResource(AndroidApplicationR.component_btprint__btprintDialog_error__label),
								" [",String.valueOf(message.iErrorCode),"]");
						//oMessage.sErrorMessage="Error ["+oMessage.iErrorCode+" ]";
						oApplication.getLog().debug(TAG,StringUtils.concat("ERROR  [ ", String.valueOf(message.iErrorCode), "] "));
						publishProgress(message);
						break;
					}else{
						message.sMessage=AbstractBtPrintCommand.this.oApplication.getStringResource(AndroidApplicationR.component_btprint__btprintDialog__title);
						//oMessage.sErrorMessage=null;
						publishProgress(message);
					}
					message.iErrorCode=AbstractBtPrintCommand.this.lineFeed(getEndFeedCode());
					iCptParams++;
				}
			}
			return message.iErrorCode;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onProgressUpdate(ProgressMessage... p_oValues) {
			super.onProgressUpdate(p_oValues);
			ProgressMessage oMessage=p_oValues[0];
			if (oMessage.iErrorCode== getPrinterOKCode()){
				oUiMessage.setText(oMessage.sMessage);
				if (oUiErrorParent!=null){
					oUiErrorParent.setVisibility(View.INVISIBLE);
				}
				oApplication.getLog().debug(TAG,"Printing..");
			}
			else{
				oUiMessage.setText(AbstractBtPrintCommand.this.getErrorMessage(oMessage.iErrorCode));
				if (oUiErrorParent!=null){
					oUiErrorParent.setVisibility(View.VISIBLE);
				}
				oUiError.setText(StringUtils.concat(" [ ", String.valueOf(p_oValues[0].iErrorCode), "] "));
				oApplication.getLog().debug(TAG,StringUtils.concat("ERROR  [ ", String.valueOf(p_oValues[0].iErrorCode), "] "));
			}

		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPostExecute(Integer p_iResult) {
			AbstractBtPrintCommand.this.setResultCode(p_iResult);
			AbstractBtPrintCommand.this.release();
			if (p_iResult.intValue() == getPrinterOKCode()) {
				AbstractBtPrintCommand.this.finish();
			}
		}
	}
	
	

	
	
	private final class ProgressMessage{
		private int iErrorCode=0;
		private String sMessage=null;
		//public String sErrorMessage=null;
		
		private ProgressMessage(int p_iErrorCode, String p_sMessage,String p_sErrorMessage) {
			super();
			this.iErrorCode = p_iErrorCode;
			this.sMessage = p_sMessage;
			//this.sErrorMessage = p_sErrorMessage;
		}
	
	}
	/**
	 * Accesseur resultCode
	 * @return resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}
	/**
	 * Modifieur resultCode
	 * @param p_iResultCode resultCode
	 */
	protected void setResultCode(int p_iResultCode) {
		this.resultCode = p_iResultCode;
	}


	/**
	 * onClick listeners on OK and Cancel button of the dialog {@inheritDoc}
	 */
	@Override
	public void onClick(View p_oParamView) {
		if (p_oParamView.equals(oUiCloseButton)) {
			this.release();
			try {
				oTask.cancel(true);
			} catch (Exception e) {
				Log.d(TAG, "exception on close button click " , e);				
			}
			this.setResult(this.getResultCode());
			this.finish();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBackPressed() {
		this.release();
		this.setResult(this.getResultCode());
		super.onBackPressed();
	}

	/**
	 * connect the bluetooth printer
	 * 
	 * @return 0 if connected, error code else
	 */
	protected abstract int connect();

	/**
	 * Print the {@link PrintableViewModel} that contain Esc chars for the printer
	 * @param p_oObjecttoPrint the object to print
	 * @return the error code (see constants) or 0 if ok
	 */
	public abstract int print(PrintableViewModel p_oObjecttoPrint);

	/**
	 * PGets the status of the printer
	 * this is used before printing 
	 * @return the error code (see constants) or 0 if ok
	 * */
	public abstract int getStatus();
	
	
	/**
	 * Gets the error message for this error code
	 * this is shown to the end user 
	 * @param p_iErrorCode the error code
	 * @return the error message
	 * */
	public abstract String getErrorMessage(int p_iErrorCode);
	
	
	/**
	 * Feed line
	 * @param p_iNumber the number of line to feed
	 * @return the error code
	 */
	public abstract int lineFeed(int p_iNumber);
	
	/**
	 * release the bluetooth printer <b> must be called when you called the
	 * {@link #connect()} method</b>
	 */
	protected abstract void release();

	/** Error codes */
	/** printer ok*/
	protected abstract int getPrinterOKCode();
	/**error in init*/
	protected abstract int getPrinterErrInitCode();
	/** error overflow*/
	protected abstract int getPrinterErrOverflowCode();
	/** error unsupported fonction */
	protected abstract int getPrinterErrUnsupportedCode();
	/** buffer full*/
	protected abstract int getPrinterErrBufferFullCode();
	/** error opening connection*/
	protected abstract int getPrinterErrOperConnCode();
	/**error reading data*/
	protected abstract int getPrinterErrReadDataCode();
	/** status : cover open*/
	protected abstract int getPrinterStatusCoverOpenCode();
	/**status empty paper*/
	protected abstract int getPrinterStatusEmptyPaperCode();
	/** status : error*/
	protected abstract int getPrinterStatusErrorCode();
	/** status : power low*/
	protected abstract int getPrinterPwrLowCode();
	/** status power < low*/
	protected abstract int getPrinterPwrSmallCode();
	/** the number of line to feed after one print */ 
	protected abstract int getEndFeedCode();
}