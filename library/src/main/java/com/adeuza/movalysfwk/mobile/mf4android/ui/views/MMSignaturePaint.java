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
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
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