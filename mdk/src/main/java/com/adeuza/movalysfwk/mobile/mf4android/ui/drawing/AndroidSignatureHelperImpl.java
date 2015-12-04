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
package com.adeuza.movalysfwk.mobile.mf4android.ui.drawing;

import java.util.List;
import java.util.regex.Matcher;

import android.graphics.Path;

import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.MMPoint;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.SignatureHelperImpl;

/**
 * <p>
 * 	Class helping signature management for interventions in Android.
 * 	In this class we find methods to convert a String of a list of  
 * 	points in a Path object.
 * </p>
 *
 *
 * @since Barcelone (9 déc. 2010)
 */
public final class AndroidSignatureHelperImpl extends SignatureHelperImpl implements AndroidSignatureHelper {

	/** instance of the singleton <em>AndroidSignatureHelperImpl</em>. */
	private static AndroidSignatureHelperImpl _instance;
	
	/**
	 * <p>
	 *  Construct an object <em>AndroidSignatureHelperImpl</em>.
	 * </p>
	 */
	private AndroidSignatureHelperImpl(){
		//Nothing to do
	}
	
	/**
	 * <p>
	 * 	Return an instance of the singleton <em>AndroidSignatureHelperImpl</em>.
	 * </p>
	 * @return the singleton <em>AndroidSignatureHelperImpl</em>
	 */
	public static AndroidSignatureHelperImpl getInstance(){
		if (AndroidSignatureHelperImpl._instance == null){
			AndroidSignatureHelperImpl._instance = new AndroidSignatureHelperImpl();
		}
		return AndroidSignatureHelperImpl._instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path convertFromStringToPath(String p_sSignature) {
		//path résultat
		Path r_oPath = new Path();

		if (p_sSignature != null) {
			final Matcher oLineMatcher = LINE_PATTERN.matcher(p_sSignature);
			Matcher oPointMatcher;
			boolean bFirstPoint = false;
			while (oLineMatcher.find()) {
				bFirstPoint = true;

				oPointMatcher = POINT_PATTERN.matcher(oLineMatcher.group());
				while (oPointMatcher.find()) {
					if (bFirstPoint) {
						try {
							//insertion du premier point
							r_oPath.moveTo(Float.valueOf(oPointMatcher.group(1)),Float.valueOf(oPointMatcher.group(2)));
							bFirstPoint = false;
						}
						catch (NumberFormatException e){
							Application.getInstance().getLog().error(this.getClass().getSimpleName(), "oCoord = "+oPointMatcher.group(1)+" :::: oCoordinates[i+1] = "+oPointMatcher.group(2));
						}
					}
					else {
						//insertion des points intermédiaires et final
						try {
							r_oPath.lineTo(Float.valueOf(oPointMatcher.group(1)),Float.valueOf(oPointMatcher.group(2)));
						}
						catch (NumberFormatException e) {
							Application.getInstance().getLog().error(this.getClass().getSimpleName(), "oCoord = "+oPointMatcher.group(1)+" :::: oCoordinates[i+1] = "+oPointMatcher.group(2));
						}
					}
				}
			}
		}
		return r_oPath;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path convertFromMMPointsToPath(List<List<MMPoint>> p_oListsPoints){
		Path r_oPath = new Path();
		boolean bFirstPoint ;
		//parcours de la liste de traits
		for (List<MMPoint> oLists : p_oListsPoints){
			bFirstPoint = true;
			//parcours des points de chaque trait
			for (MMPoint oPoint : oLists){
				if (bFirstPoint){
					//premier point du trait courant
					r_oPath.moveTo(oPoint.getX(), oPoint.getY());
				}else{
					//point intermédiaire et final
					r_oPath.lineTo(oPoint.getX(), oPoint.getY());
				}
			}
		}
		return r_oPath;
	}	
}
