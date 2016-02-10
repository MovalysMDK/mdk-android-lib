package com.adeuza.movalysfwk.mobile.mf4android.ui.modele;


/**
 * <p>
 * 	Object to centralize the information for calculating the progression of a treatment on Movalys.
 * 	The two variables are:
 * 	<ul>
 * 		<li>the number of steps throughout treatment.</li>
 * 		<li>the number of the step at which we arrived.</li>
 * 	</ul> 
 * </p>
 *
 * <p>Copyright (c) 2010
 * <p>Company: Adeuza
 *
 * @author fbourlieux
 * @since Barcelone (26 nov. 2010)
 */
public class ProgressDefinition {
	
	/**
	 * The actual step of a progress bar.
	 */
	private int progressStepCount = 0;
	/**
	 * The number of step of a progress bar.
	 */
	private float totalProgressStepCount = 0;
	
	private String message = null;
	
	/** 
	 * <p>
	 *  Construct an object <em>ProgressDefinition</em>.
	 * </p>
	 */
	public ProgressDefinition() {
		//Nothing to do
	}

	/** 
	 * <p>
	 *  Construct an object <em>ProgressDefinition</em> with parameters.
	 * </p>
	 * 
	 * @param p_oProgressStepCount
	 * 				The actual step of a progress bar.
	 * 
	 * @param p_oTotalProgressStepCount
	 * 				The number of step of a progress bar.
	 */
	public ProgressDefinition(int p_oProgressStepCount, float p_oTotalProgressStepCount) {
		super();
		this.progressStepCount = p_oProgressStepCount;
		this.totalProgressStepCount = p_oTotalProgressStepCount;
	}

	/**
	 * <p>Return the object <em>progressStepCount</em></p>
	 * @return Objet progressStepCount
	 */
	public int getProgressStepCount() {
		return this.progressStepCount;
	}

	/**
	 * <p>Set the object <em>progressStepCount</em></p>. 
	 * @param p_oProgressStepCount Objet progressStepCount
	 */
	public void setProgressStepCount(int p_oProgressStepCount, String p_sMessage) {
		this.progressStepCount = p_oProgressStepCount;
		this.message = p_sMessage;
	}

	/**
	 * <p>Return the object <em>totalProgressStepCount</em></p>
	 * @return Objet totalProgressStepCount
	 */
	public float getTotalProgressStepCount() {
		return this.totalProgressStepCount;
	}
	
	public String getMessage() {
		if (this.message != null) {
			return this.message;
		}
		else {
			return "";
		}
	}
	
	public float getValue() {
		int iStep = this.getProgressStepCount();
		if (iStep == Integer.MAX_VALUE) {
			float fProgress = Float.MAX_VALUE;
			return fProgress;
		}
		else {
			float fProgress = iStep / this.getTotalProgressStepCount();
			return fProgress;
		}
	}

	/**
	 * <p>Set the object <em>totalProgressStepCount</em></p>. 
	 * @param p_oTotalProgressStepCount Objet totalProgressStepCount
	 */
	public void setTotalProgressStepCount(float p_oTotalProgressStepCount) {
		this.totalProgressStepCount = p_oTotalProgressStepCount;
	}
	
}
