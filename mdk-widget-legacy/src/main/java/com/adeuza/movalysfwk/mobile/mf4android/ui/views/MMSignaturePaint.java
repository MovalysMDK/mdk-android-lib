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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>
 * 	This class is used to re-paint a signature according to its description as a Path.
 * </p>
 *
 *
 * @since Barcelone (13 janv. 2011)
 */
public class MMSignaturePaint extends View {

	/** the stroke with of the signature */
	public static final float STROKE_WIDTH = 5f;
	/** holds the style and color information about how to draw geometries, text and bitmaps. */
	private Paint paint;
	/** encapsulates compound (multiple contour) geometric paths consisting of straight line segments, quadratic curves, and cubic curves */
	private Path path = null;

	/**
	 * <p>
	 *  Construct an object <em>MMSignatureDrawing</em>.
	 * </p>
	 * @param p_oContext
	 * 		the application context to use
	 * @param p_oAttrs
	 * 		the parameters to use
	 */
	public MMSignaturePaint(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		if(!isInEditMode()) {
			this.paint = new Paint();
			this.paint.setAntiAlias(true);
			this.paint.setColor(Color.WHITE);
			this.paint.setStyle(Paint.Style.STROKE);
			this.paint.setStrokeJoin(Paint.Join.ROUND);
			this.paint.setStrokeWidth(STROKE_WIDTH);
		}
	}

	/**
	 * Erases the signature.
	 */
	public void clear() {
		this.path.reset();
		this.destroyDrawingCache();
		this.invalidate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas p_oCanvas) {
		if (this.path != null) {
			p_oCanvas.drawPath(this.path, this.paint);
		}
	}

	/**
	 * <p>
	 * 	Set the definition of the signature.
	 * 	The Path class encapsulates compound (multiple contour) geometric paths 
	 * 	consisting of straight line segments, quadratic curves, and cubic curves.
	 * </p> 
	 * @param p_oPath
	 * 		The Path of current signature
	 */
	public void setPath(Path p_oPath){
		this.path = p_oPath;
		this.invalidate();
	}

	/**
	 * <p>
	 * 	Return the path definition of the signture.
	 * </p>
	 * @return a <em>Path</em> object.
	 */
	/*public Path getPath(){
		return this.path;
	}*/

}
