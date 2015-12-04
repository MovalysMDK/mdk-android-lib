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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.adeuza.movalysfwk.mf4jcommons.model.configuration.entity.utils.StringUtils;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.application.Application;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.ui.drawing.MMPoint;

/**
 * <p>
 * This component is used to draw hand signature.
 * It capture the hand gesture to display it on the screen
 * 2 results cans be used :
 * <ul>
 * <li>an image bay calling the {@link MMSignatureDrawing#getImage()}</li>
 * <li>a list of the key points of the signature  {@link MMSignatureDrawing#getPath()}</li>
 * </ul>
 * </p>
 * 
 * 
 */
public class MMSignatureDrawing extends View {
	
	/** the stroke with of the signature */
	public static final float STROKE_WIDTH = 5f;
	
	/** Need to track this so the dirty region can accommodate the stroke. **/
	private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
	
	/** holds the style and color information about how to draw geometries, text and bitmaps. */
	private Paint paint;
	
	/** encapsulates compound (multiple contour) geometric paths consisting of straight line segments, quadratic curves, and cubic curves */
	private Path path;
	
	/** Optimizes painting by invalidating the smallest possible area. */
	private float lastTouchX;
	
	/** Optimizes painting by invalidating the smallest possible area. */
	private float lastTouchY;
	
	/** object for the delimitation of the signature area. */
	private final RectF dirtyRect;
	
	/** 
	 * list of points that compose a line in a signature. 
	 * !!! Must be global for component to work !!!
	 */
	private List<MMPoint> currentLine;
	
	/** list of lines. */
	private List<List<MMPoint>> lines;
	
	
	/**
	 * Construct an object <em>MMSignatureDrawing</em>.
	 * @param p_oContext the application context to use
	 * @param p_oAttrs the parameters to use
	 */
	public MMSignatureDrawing(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		this.paint = new Paint();
		this.dirtyRect = new RectF();
		this.paint.setAntiAlias(true);
		this.paint.setColor(Color.WHITE);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeJoin(Paint.Join.ROUND);
		this.paint.setStrokeWidth(STROKE_WIDTH);
	}

	/**
	 * Erases the signature.
	 */
	public void clear() {
		this.path.reset();
		this.destroyDrawingCache();
		this.setSignatureLines(new ArrayList<List<MMPoint>>());
		// Repaints the entire view.
		this.invalidate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas p_oCanvas) {
		if (p_oCanvas!=null && !isInEditMode()){ 
			p_oCanvas.drawPath(this.path, this.paint);
		}
	}

	/**
	 * Get the image value of the signature.
	 * @return signature as a bitmap
	 */
	public Bitmap getImage() {
		this.buildDrawingCache();
		return this.getDrawingCache();
	}

	/**
	 * <p>
	 * 	Get the definition of the signature. 
	 * 	The Path class encapsulates compound (multiple contour) geometric paths 
	 * 	consisting of straight line segments, quadratic curves, and cubic curves.
	 * </p>
	 * @return The Path of current signature
	 */
	public Path getPath() {
		return this.path;
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
		if (this.path != null) {
			this.path.addPath(p_oPath);
		}else{
			this.path = p_oPath;
		}
		this.invalidate();
	}
	
	/**
	 *	Method that initialize the signature lines.
	 * @param p_oLines new signature in text to display
	 */
	public void setSignatureLines(List<List<MMPoint>> p_oLines){
		this.lines = p_oLines;
	}
	
	/**
	 * <p>Return the object <em>signatureLines</em></p>
	 * @return Objet signatureLines
	 */
	public List<List<MMPoint>> getSignatureLines() {
		return this.lines;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onTouchEvent(MotionEvent p_oEvent) {
		//récupération des coordonnées, attention on ne veux pas avoir de nombres négatifs
		float fEventX = p_oEvent.getX();
		/*if (fEventX < 0){
			fEventX = 0;
			Application.getInstance().getLog().debug(MMSignatureDrawing.class.getSimpleName(), 
					StringUtils.concat("x:'"+p_oEvent.getX(),"' inferieur à zero" , "(y:"+p_oEvent.getY(),")" ));
		}*/
		float fEventY = p_oEvent.getY();
		/*
		if (fEventY < 0){
			fEventY = 0;
			Application.getInstance().getLog().debug(MMSignatureDrawing.class.getSimpleName(), 
					StringUtils.concat("y:'"+p_oEvent.getY(),"' inferieur à zero", "(x:"+fEventX,")" ));
		}*/
		
		boolean bNotIgnoredEvent = true ;
		switch (p_oEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.path.moveTo(fEventX, fEventY);
				this.lastTouchX = fEventX;
				this.lastTouchY = fEventY;
				// There is no end point yet, so don't waste cycles invalidating.
				
				this.currentLine = new ArrayList<>();
				this.currentLine.add(new MMPoint(this.lastTouchX, this.lastTouchY));
				//Application.getInstance().getLog().debug(MMSignatureDrawing.class.getSimpleName(), 
				//		StringUtils.concat("ACTION_DOWN : ", new MMPoint(this.lastTouchX, this.lastTouchY).toString() ));
				return true;
	
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				// Start tracking the dirty region.
				this.resetDirtyRect(fEventX, fEventY);
	
				// When the hardware tracks events faster than they are delivered, the
				// event will contain a history of those skipped points.
				int iHistorySize = p_oEvent.getHistorySize();
				float fHistoricalX ;
				float fHistoricalY ;
				for (int i = 0; i < iHistorySize; i++) {
					fHistoricalX = p_oEvent.getHistoricalX(i);
					fHistoricalY = p_oEvent.getHistoricalY(i);
					this.expandDirtyRect(fHistoricalX, fHistoricalY);
					this.path.lineTo(fHistoricalX, fHistoricalY);
					
					//if (p_oEvent.getAction() == MotionEvent.ACTION_MOVE){
						this.currentLine.add(new MMPoint(fHistoricalX,fHistoricalY));
						//	Application.getInstance().getLog().debug(MMSignatureDrawing.class.getSimpleName(), 
						//		StringUtils.concat("ACTION_MOVE : ", new MMPoint(fHistoricalX,fHistoricalY).toString() ));
					//}
				}
	
				// After replaying history, connect the line to the touch point.
				this.path.lineTo(fEventX, fEventY);
				this.currentLine.add(new MMPoint(fEventX,fEventY));
				if (p_oEvent.getAction() == MotionEvent.ACTION_UP){					
					//Application.getInstance().getLog().debug(MMSignatureDrawing.class.getSimpleName(), 
					//		StringUtils.concat("ACTION_UP : ", this.currentLine.toString() ));
					this.lines.add(this.currentLine);
				}				
				break;	
			default:
				Application.getInstance().getLog().info(MMSignatureDrawing.class.getSimpleName(), 
						StringUtils.concat("Ignored touch event: ",p_oEvent.toString()));
				bNotIgnoredEvent = false;
		}

		if (bNotIgnoredEvent){
			// Include half the stroke width to avoid clipping.
			this.invalidate(
					(int) (this.dirtyRect.left - HALF_STROKE_WIDTH), 
					(int) (this.dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (this.dirtyRect.right + HALF_STROKE_WIDTH), 
					(int) (this.dirtyRect.bottom + HALF_STROKE_WIDTH));
	
			this.lastTouchX = fEventX;
			this.lastTouchY = fEventY;
		}
		return bNotIgnoredEvent;
	}

	/**
	 * Called when replaying history to ensure the dirty region includes all points.
	 * @param p_fHistoricalX
	 * 				X historical position
	 * @param p_fHistoricalY
	 * 				Y historical position
	 */
	private void expandDirtyRect(float p_fHistoricalX, float p_fHistoricalY) {
		if (p_fHistoricalX < this.dirtyRect.left) {
			this.dirtyRect.left = p_fHistoricalX;
		} else if (p_fHistoricalX > this.dirtyRect.right) {
			this.dirtyRect.right = p_fHistoricalX;
		}
		if (p_fHistoricalY < this.dirtyRect.top) {
			this.dirtyRect.top = p_fHistoricalY;
		} else if (p_fHistoricalY > this.dirtyRect.bottom) {
			this.dirtyRect.bottom = p_fHistoricalY;
		}
	}

	/**
	 * Resets the dirty region when the motion event occurs.
	 * @param p_fEventX
	 * 			X position 
	 * @param p_fEventY
	 * 			Y position
	 */
	private void resetDirtyRect(float p_fEventX, float p_fEventY) {

		// The lastTouchX and lastTouchY were set when the ACTION_DOWN motion event occurred.
		this.dirtyRect.left = Math.min(this.lastTouchX, p_fEventX);
		this.dirtyRect.right = Math.max(this.lastTouchX, p_fEventX);
		this.dirtyRect.top = Math.min(this.lastTouchY, p_fEventY);
		this.dirtyRect.bottom = Math.max(this.lastTouchY, p_fEventY);
	}

}
