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
package com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial transparency outside it, as well as the
 * laser scanner animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class ViewfinderView extends View implements ViewfinderViewable {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
	private static final long ANIMATION_DELAY = 80L;
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int MAX_RESULT_POINTS = 20;
	private static final int POINT_SIZE = 6;
	private static final int NB_RESULT_POINTS = 5;
	
	private CameraManager cameraManager;
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private List<ResultPoint> possibleResultPoints;
	private List<ResultPoint> lastPossibleResultPoints;

	/**
	 * This constructor is used when the class is built from an XML resource.
	 * 
	 * @param context context of application
	 * @param attrs
	 */
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every time in onDraw().
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new ArrayList<>(NB_RESULT_POINTS);
		lastPossibleResultPoints = null;
	}

	/**
	 * Modifier of camera manager attribute
	 * 
	 * @param p_oCameraManager new manager camera
	 */
	@Override
	public void setCameraManager(CameraManager p_oCameraManager) {
		this.cameraManager = p_oCameraManager;
	}

	/**
	 * Draw the overlay of the scanning zone
	 * 
	 * @param canvas zone to draw with the new transparent zone
	 */
	@Override
	public void onDraw(Canvas canvas) {
		if (cameraManager == null) {
			return; // not ready yet, early draw before done configuring
		}
		Rect frame = cameraManager.getFramingRect();
		if (frame == null) {
			return;
		}

		// Draw the exterior (i.e. outside the framing rect) darkened
		this.drawTopTransparentOverlay(canvas, frame);
		this.drawLeftTransparentOverlay(canvas, frame);
		this.drawRightTransparentOverlay(canvas, frame);
		this.drawBottomTransparentOverlay(canvas, frame);
		
		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, frame, paint);
		} else {
			this.drawScannerOverlay(canvas, frame);
			
			this.drawAllResultsPoints(canvas, frame);
			
			// Request another update at the animation interval, but only repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE, frame.top - POINT_SIZE, frame.right + POINT_SIZE, frame.bottom
					+ POINT_SIZE);
		}
	}
	/**
	 * Draw a rectangle on the top of the frame containing the surface view
	 * @param canvas component rectangle of the view
	 * @param frame frame of the preview image given by camera
	 */
	public void drawTopTransparentOverlay(Canvas canvas , Rect frame ) {
		if(resultBitmap != null){
			paint.setColor(resultColor);
		}
		else {
			paint.setColor(maskColor);
		}
		canvas.drawRect(0, 0, canvas.getWidth() , frame.top, paint);
	}
	/**
	 * Draw a rectangle on the bottom of the frame containing the surface view
	 * @param canvas component rectangle of the view
	 * @param frame frame of the preview image given by camera
	 */
	public void drawBottomTransparentOverlay(Canvas canvas , Rect frame) {
		if(resultBitmap != null){
			paint.setColor(resultColor);
		}
		else {
			paint.setColor(maskColor);
		}
		canvas.drawRect(0, frame.bottom, canvas.getWidth(), canvas.getHeight(), paint);
	}
	/**
	 * Draw a rectangle on the left of the frame containing the surface view
	 * @param p_oCanvas component rectangle of the view
	 * @param p_oFrame frame of the preview image given by camera
	 */
	public void drawLeftTransparentOverlay(Canvas p_oCanvas , Rect p_oFrame) {
		if(resultBitmap != null){
			paint.setColor(resultColor);
		}
		else {
			paint.setColor(maskColor);
		}
		p_oCanvas.drawRect(0, p_oFrame.top, p_oFrame.left, p_oFrame.bottom, paint);
	}
	/**
	 * Draw a rectangle on the right of the frame containing the surface view
	 * @param p_oCanvas component rectangle of the view
	 * @param p_oFrame frame of the preview image given by camera
	 */
	public void drawRightTransparentOverlay(Canvas p_oCanvas , Rect p_oFrame) {
		if(resultBitmap != null){
			paint.setColor(resultColor);
		}
		else {
			paint.setColor(maskColor);
		}
		p_oCanvas.drawRect(p_oFrame.right, p_oFrame.top, p_oCanvas.getWidth(), p_oFrame.bottom, paint);
	}
	/**
	 * Draw the scanner overlay laser. Last drawing added to the view
	 * @param p_oCanvas component rectangle of the view
	 * @param p_oFrame frame of the preview image given by camera
	 */
	public void drawScannerOverlay(Canvas p_oCanvas , Rect p_oFrame ) {
		// Draw "laser scanner" box through the middle to show decoding is active
		paint.setColor(laserColor);
		paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
		scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;

	    int middle = p_oFrame.height() / 2 + p_oFrame.top;
	    p_oCanvas.drawRect(p_oFrame.left + 2, middle - 1, p_oFrame.right - 1, middle + 2, paint);

		/* draw the box in the scanning zone 
		canvas.drawRect(frame.left , frame.top, frame.right, frame.top + 2, paint); // top line 
		canvas.drawRect(frame.right - 2, frame.top, frame.right, frame.bottom, paint); // right line 
		canvas.drawRect(frame.left , frame.bottom - 2, frame.right, frame.bottom  , paint); // bottom line
		canvas.drawRect(frame.left , frame.top, frame.left+2, frame.bottom, paint); // left line
		*/
	}
	/**
	 * Draw the points previewed by the camera and decoded. Use the resultPointColor
	 * @param p_oCanvas component rectangle of the view
	 * @param p_oFrame frame of the preview image given by camera
	 */
	public void drawAllResultsPoints(Canvas p_oCanvas , Rect p_oFrame ){
		Rect previewFrame = cameraManager.getFramingRectInPreview();
		float scaleX = p_oFrame.width() / (float) previewFrame.width();
		float scaleY = p_oFrame.height() / (float) previewFrame.height();

		List<ResultPoint> currentPossible = possibleResultPoints;
		List<ResultPoint> currentLast = lastPossibleResultPoints;
		int frameLeft = p_oFrame.left;
		int frameTop = p_oFrame.top;
		if (currentPossible.isEmpty()) {
			lastPossibleResultPoints = null;
		} else {
			possibleResultPoints = new ArrayList<>(NB_RESULT_POINTS);
			lastPossibleResultPoints = currentPossible;
			paint.setAlpha(CURRENT_POINT_OPACITY);
			paint.setColor(resultPointColor);
			synchronized (currentPossible) {
				for (ResultPoint point : currentPossible) {
					p_oCanvas.drawCircle(frameLeft + (int) (point.getX() * scaleX), frameTop + (int) (point.getY() * scaleY), POINT_SIZE,
							paint);
				}
			}
		}
		if (currentLast != null) {
			paint.setAlpha(CURRENT_POINT_OPACITY / 2);
			paint.setColor(resultPointColor);
			synchronized (currentLast) {
				float radius = POINT_SIZE / 2.0f;
				for (ResultPoint point : currentLast) {
					p_oCanvas.drawCircle(frameLeft + (int) (point.getX() * scaleX), frameTop + (int) (point.getY() * scaleY), radius,
							paint);
				}
			}
		}
	}

	/**
	 * Redraw the zone view and cancel the old bitmap result
	 */
	@Override
	public void drawViewfinder() {
		Bitmap oResultBitmap = this.resultBitmap;
		this.resultBitmap = null;
		if (oResultBitmap != null) {
			oResultBitmap.recycle();
		}
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live scanning display.
	 * 
	 * @param p_oBarcode An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap p_oBarcode) {
		resultBitmap = p_oBarcode;
		invalidate();
	}

	/**
	 * Add a point decoded by the scanner
	 * 
	 * @param p_oPoint new point found by the scanner
	 */
	@Override
	public void addPossibleResultPoint(ResultPoint p_oPoint) {
		List<ResultPoint> points = possibleResultPoints;
		synchronized (points) {
			points.add(p_oPoint);
			int size = points.size();
			if (size > MAX_RESULT_POINTS) {
				// trim it
				points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
			}
		}
	}
}
