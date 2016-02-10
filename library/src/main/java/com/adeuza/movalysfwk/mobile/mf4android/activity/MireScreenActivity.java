package com.adeuza.movalysfwk.mobile.mf4android.activity;

import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.fwk_screen_mire_test;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_density;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_densitydpi;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_hight_pixels;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_scaled_density;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_width_pixels;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_xdpi;
import static com.adeuza.movalysfwk.mobile.mf4android.application.AndroidApplicationR.mire_metrics_ydpi;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;

/**
 * <p>Activity used for screen tests.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 * @since Barcelone (9 déc. 2010)
 */
public class MireScreenActivity extends AbstractMMActivity {

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle p_oSavedInstanceState) {
		super.onCreate(p_oSavedInstanceState);
		setContentView(this.getAndroidApplication().getAndroidIdByRKey(fwk_screen_mire_test));
		
		DisplayMetrics oMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(oMetrics);
		
		
		/* metrics */
		TextView oMetricsDensity = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_density));
		oMetricsDensity.setText(StringUtils.concat("density = ",String.valueOf(oMetrics.density)));
		TextView oMetricsDensityDpi = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_densitydpi));
		oMetricsDensityDpi.setText(StringUtils.concat("densityDpi = ",String.valueOf(oMetrics.densityDpi)));
		TextView oMetricsScaledDensity = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_scaled_density));
		oMetricsScaledDensity.setText(StringUtils.concat("scaledDensity = ",String.valueOf(oMetrics.scaledDensity)));
		TextView oMetricsHightPixels = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_hight_pixels));
		oMetricsHightPixels.setText(StringUtils.concat("heightPixels = ",String.valueOf(oMetrics.heightPixels)));
		TextView oMetricsWidthPixels = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_width_pixels));
		oMetricsWidthPixels.setText(StringUtils.concat("widthPixels = ",String.valueOf(oMetrics.widthPixels)));
		TextView oMetricsXdpi = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_xdpi));
		oMetricsXdpi.setText(StringUtils.concat("xdpi = ",String.valueOf(oMetrics.xdpi)));
		TextView oMetricsYdpi = (TextView) this.findViewById(this.getAndroidApplication().getAndroidIdByRKey(mire_metrics_ydpi));
		oMetricsYdpi.setText(StringUtils.concat("ydpi = ",String.valueOf(oMetrics.ydpi)));
		
	}
}
