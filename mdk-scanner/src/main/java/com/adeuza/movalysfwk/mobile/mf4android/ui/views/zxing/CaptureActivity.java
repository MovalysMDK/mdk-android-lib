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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.adeuza.movalysfwk.mobile.mf4android.ui.views.zxing.camera.CameraManager;
import com.adeuza.movalysfwk.mobile.mf4mjcommons.utils.NumericConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

/**
 * This component opens the camera and does the actual scanning on a background thread. It draws a viewfinder to help the user place the
 * barcode correctly, shows feedback as the image processing is happening, and then overlays the results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public abstract class CaptureActivity extends FrameLayout implements SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	// private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;
	// private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	// private static final String PACKAGE_NAME = "com.google.zxing.client.android";
	// private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
	// private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
	// private static final String[] ZXING_URLS = { "http://zxing.appspot.com/scan", "zxing://scan/" };

	//public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	// private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
	// EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
	// ResultMetadataType.SUGGESTED_PRICE,
	// ResultMetadataType.ERROR_CORRECTION_LEVEL,
	// ResultMetadataType.POSSIBLE_COUNTRY);

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	// private Result savedResultToShow;
	private ViewfinderViewable viewfinderView;
	// private TextView statusView;
	// private View resultView;
	// private Result lastResult;
	private boolean hasSurface;
	// private boolean copyToClipboard;
	// private IntentSource source;
	// private String sourceUrl;
	// private ScanFromWebPageManager scanFromWebPageManager;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet;

	// private HistoryManager historyManager;
	// private InactivityTimer inactivityTimer;
	// private BeepManager beepManager;
	// private AmbientLightManager ambientLightManager;
	/**
	 * Constructs a new CaptureActivity
	 * 
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @param p_oDefStyle the style to use
	 * @see FrameLayout#FrameLayout(Context, AttributeSet, int)
	 */
	public CaptureActivity(Context p_oContext, AttributeSet p_oAttrs, int p_iDefStyle) {
		super(p_oContext, p_oAttrs, p_iDefStyle);
		this.initialize(p_oContext);
	}

	/**
	 * Constructs a new CaptureActivity
	 * 
	 * @param p_oContext the context to use
	 * @param p_oAttrs the attributes to use
	 * @see FrameLayout#FrameLayout(Context, AttributeSet)
	 */
	public CaptureActivity(Context p_oContext, AttributeSet p_oAttrs) {
		super(p_oContext, p_oAttrs);
		this.initialize(p_oContext);
	}

	/**
	 * Constructs a new MMBarcodeScanner
	 * 
	 * @param p_oContext the context to use
	 */
	public CaptureActivity(Context p_oContext) {
		super(p_oContext);
		this.initialize(p_oContext);
	}

	/**
	 * Accessor of the view finder
	 * 
	 * @return the attribute view finder
	 */
	protected final ViewfinderViewable getViewfinderView() {
		return this.viewfinderView;
	}

	/**
	 * Accessor of the information handler
	 * 
	 * @return the attribute containing information handler
	 */
	@Override
	public CaptureActivityHandler getHandler() {
		return this.handler;
	}

	/**
	 * Accessor of the camera manager
	 * 
	 * @return the attribute containing camera manager
	 */
	public final CameraManager getCameraManager() {
		return this.cameraManager;
	}
	
	/**
	 * Initialize the object by inflating the component and create the camera manager
	 * @param p_oContext context to use
	 */
	private final void initialize(Context p_oContext) {
		LayoutInflater oInflater = ((Activity) p_oContext).getLayoutInflater();
		oInflater.inflate(R.layout.fwk_component__barcodescanner, this);

		this.cameraManager = new CameraManager(p_oContext);

		this.viewfinderView = (ViewfinderViewable) findViewById(R.id.viewfinder_view);
		this.viewfinderView.setCameraManager(cameraManager);

		// resultView = findViewById(R.id.result_view);
		// statusView = (TextView) findViewById(R.id.status_view);

		this.handler = null;
		// lastResult = null;

		// resetStatusView();
		this.hasSurface = false;
	}

	/**
	 * Creation the surface view and the callback prepare the action of decoding
	 */
	public final void init() {
		SurfaceView oSurfaceView = new SurfaceView(this.getContext()); 
		oSurfaceView.setId(R.id.preview_view);
		this.customizeUiSurfaceView(oSurfaceView);
		this.addView(oSurfaceView, 0);
		
		/* the layout method does'nt display the 
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		if (surfaceView == null) {
			inflate(getContext(), R.layout.fwk_component__barcodescanner_surfaceview, this);
			surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		}
		this.removeView(surfaceView);
		this.addView(surfaceView, 0);
		*/
		SurfaceHolder surfaceHolder = oSurfaceView.getHolder();
		if (this.hasSurface) {
			// The activity was paused but not stopped, so the surface still exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		// ambientLightManager.start(cameraManager);
		// inactivityTimer.onResume();
		Intent intent = getIntent();
		
		this.decodeFormats = null;
		if (intent != null) {

			if ( intent.getExtras() != null && intent.getExtras().containsKey("hideLaserZone") 
					&& intent.getExtras().getBoolean("hideLaserZone") ) {
				this.viewfinderView.setVisibility(GONE);
			}
			
			String action = intent.getAction();
			if (Intents.Scan.ACTION.equals(action)) {
				// Scan the formats the intent requested, and return the result to the calling activity.
				this.decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
				this.decodeHints = DecodeHintManager.parseDecodeHints(intent);

				if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
					int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
					int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
					if (width > 0 && height > 0) {
						this.cameraManager.setManualFramingRect(width, height);
					}
				}
			} 
		}
	}	

	/**
	 * Clean properly the context : remove the callbacks , surface view and stop camera manager
	 */
	public void release() {
		if (this.handler != null) {
			this.handler.quitSynchronously();
			this.handler = null;
		}
		// inactivityTimer.onPause();
		// ambientLightManager.stop();
		if (this.cameraManager != null) {
			this.cameraManager.closeDriver();
		}

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		if (surfaceView != null) {
			if (!this.hasSurface) {
				SurfaceHolder surfaceHolder = surfaceView.getHolder();
				surfaceHolder.removeCallback(this);
			}
			this.removeView(surfaceView);
		}
		// super.onPause();
	}

	/**
	 * Init camera when the surface is created
	 * @param p_oHolder object who create the surface
	 */
	@Override
	public void surfaceCreated(SurfaceHolder p_oHolder) {
		Log.e(TAG, "surfaceCreated");
		if (p_oHolder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!this.hasSurface) {
			this.hasSurface = true;
			initCamera(p_oHolder);
		}
	}
	/**
	 * Remove tag when surface is destroyed
	 * @param p_oHolder object who has his surface destroyed
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder p_oHolder) {
		this.hasSurface = false;
	}

	/**
	 * Modify the preview size the the surface is modified
	 * @param p_oHolder object who change of surface
	 * @param p_iFormat format of th surface
	 * @param p_iWidth  new width the surface 
	 * @param p_iHeight new height the surface 
	 */
	@Override
	public void surfaceChanged(SurfaceHolder p_oHolder, int p_iFormat, int p_iWidth, int p_iHeight) {
		Log.v(TAG, "surfaceChanged old w " + p_iWidth + " h " + p_iHeight );
		
		Camera oCamera = this.cameraManager.getCamera();
		if (oCamera != null) {
			Camera.Parameters parameters = this.cameraManager.getCamera().getParameters();
			List<Size> sizes = parameters.getSupportedPreviewSizes();
			Size optimalSize = getOptimalPreviewSize(sizes, p_iWidth, p_iHeight);
			Log.v(TAG, "surfaceChanged new w" + optimalSize.width +" h" +optimalSize.height );
			parameters.setPreviewSize(optimalSize.width, optimalSize.height);
		}
	}

	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 * 
	 * @param p_oRawResult The contents of the barcode.
	 * @param p_iScaleFactor amount by which thumbnail was scaled
	 * @param p_oBarcode A greyscale bitmap of the camera data which was decoded.
	 */
	protected void handleDecode(Result p_oRawResult, Bitmap p_oBarcode, float p_iScaleFactor) {
			// inactivityTimer.onActivity();
		// lastResult = rawResult;
		// ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
		// codet
		boolean fromLiveScan = p_oBarcode != null;
		if (fromLiveScan) {
		// historyManager.addHistoryItem(rawResult, resultHandler);
		// // Then not from history, so beep/vibrate and we have an image to draw on
		// beepManager.playBeepSoundAndVibrate();
			drawResultPoints(p_oBarcode, p_iScaleFactor, p_oRawResult);
		}
		
		//if (orientation != null) {
			// intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
			// }
		//
		// switch (source) {
		// case NATIVE_APP_INTENT:
		// case PRODUCT_SEARCH_LINK:
		// handleDecodeExternally(rawResult, resultHandler, barcode);
		// break;
		// case ZXING_LINK:
		// if (scanFromWebPageManager == null || !scanFromWebPageManager.isScanFromWebPage()) {
		// handleDecodeInternally(rawResult, resultHandler, barcode);
		// } else {
		// handleDecodeExternally(rawResult, resultHandler, barcode);
		// }
		// break;
		// case NONE:
		// SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// if (fromLiveScan && prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
		// String message = getResources().getString(R.string.msg_bulk_mode_scanned)
		// + " (" + rawResult.getText() + ')';
		// Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		// // Wait a moment or else it will scan the same barcode continuously about 3 times
		// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
		// } else {
		// handleDecodeInternally(rawResult, resultHandler, barcode);
		// }
		// break;
		// }
	}

	 /**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
	 *
	 * @param p_oBarcode A bitmap of the captured image.
	 * @param p_iScaleFactor amount by which thumbnail was scaled
	 * @param p_oRawResult The decoded results which contains the points to draw.
	 */
	
	private void drawResultPoints(Bitmap p_oBarcode, float p_iScaleFactor, Result p_oRawResult) {
		ResultPoint[] points = p_oRawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(p_oBarcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(NumericConstants.PIXELS_4);
				drawLine(canvas, paint, points[0], points[1], p_iScaleFactor);
				} else if (points.length == 4
					&& (p_oRawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || p_oRawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and metadata
				drawLine(canvas, paint, points[0], points[1], p_iScaleFactor);
				drawLine(canvas, paint, points[2], points[3], p_iScaleFactor);
			} else {
				paint.setStrokeWidth(NumericConstants.PIXELS_10);
				for (ResultPoint point : points) {
					canvas.drawPoint(p_iScaleFactor * point.getX(), p_iScaleFactor * point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas p_oCanvas, Paint p_oPaint, ResultPoint p_oAPoint, ResultPoint p_oBPoint, float p_fScaleFactor) {
		if (p_oAPoint != null && p_oBPoint != null) {
			p_oCanvas.drawLine(p_fScaleFactor * p_oAPoint.getX(), p_fScaleFactor * p_oAPoint.getY(), p_fScaleFactor * p_oBPoint.getX(), p_fScaleFactor * p_oBPoint.getY(), p_oPaint);
		}
	}

	//
	// // Put up our own UI for how to handle the decoded contents.
	// private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
	// statusView.setVisibility(View.GONE);
	// viewfinderView.setVisibility(View.GONE);
	// resultView.setVisibility(View.VISIBLE);
	//
	// ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
	// if (barcode == null) {
	// barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
	// R.drawable.launcher_icon));
	// } else {
	// barcodeImageView.setImageBitmap(barcode);
	// }
	//
	// TextView formatTextView = (TextView) findViewById(R.id.format_text_view);
	// formatTextView.setText(rawResult.getBarcodeFormat().toString());
	//
	// TextView typeTextView = (TextView) findViewById(R.id.type_text_view);
	// typeTextView.setText(resultHandler.getType().toString());
	//
	// DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
	// String formattedTime = formatter.format(new Date(rawResult.getTimestamp()));
	// TextView timeTextView = (TextView) findViewById(R.id.time_text_view);
	// timeTextView.setText(formattedTime);
	//
	//
	// TextView metaTextView = (TextView) findViewById(R.id.meta_text_view);
	// View metaTextViewLabel = findViewById(R.id.meta_text_view_label);
	// metaTextView.setVisibility(View.GONE);
	// metaTextViewLabel.setVisibility(View.GONE);
	// Map<ResultMetadataType,Object> metadata = rawResult.getResultMetadata();
	// if (metadata != null) {
	// StringBuilder metadataText = new StringBuilder(20);
	// for (Map.Entry<ResultMetadataType,Object> entry : metadata.entrySet()) {
	// if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
	// metadataText.append(entry.getValue()).append('\n');
	// }
	// }
	// if (metadataText.length() > 0) {
	// metadataText.setLength(metadataText.length() - 1);
	// metaTextView.setText(metadataText);
	// metaTextView.setVisibility(View.VISIBLE);
	// metaTextViewLabel.setVisibility(View.VISIBLE);
	// }
	// }
	//
	// TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
	// CharSequence displayContents = resultHandler.getDisplayContents();
	// contentsTextView.setText(displayContents);
	// // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
	// int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
	// contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);
	//
	// TextView supplementTextView = (TextView) findViewById(R.id.contents_supplement_text_view);
	// supplementTextView.setText("");
	// supplementTextView.setOnClickListener(null);
	// if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
	// PreferencesActivity.KEY_SUPPLEMENTAL, true)) {
	// SupplementalInfoRetriever.maybeInvokeRetrieval(supplementTextView,
	// resultHandler.getResult(),
	// historyManager,
	// this);
	// }
	//
	// int buttonCount = resultHandler.getButtonCount();
	// ViewGroup buttonView = (ViewGroup) findViewById(R.id.result_button_view);
	// buttonView.requestFocus();
	// for (int x = 0; x < ResultHandler.MAX_BUTTON_COUNT; x++) {
	// TextView button = (TextView) buttonView.getChildAt(x);
	// if (x < buttonCount) {
	// button.setVisibility(View.VISIBLE);
	// button.setText(resultHandler.getButtonText(x));
	// button.setOnClickListener(new ResultButtonListener(resultHandler, x));
	// } else {
	// button.setVisibility(View.GONE);
	// }
	// }
	//
	// if (copyToClipboard && !resultHandler.areContentsSecure()) {
	// ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	// if (displayContents != null) {
	// try {
	// clipboard.setText(displayContents);
	// } catch (NullPointerException npe) {
	// // Some kind of bug inside the clipboard implementation, not due to null input
	// Log.w(TAG, "Clipboard bug", npe);
	// }
	// }
	// }
	// }
	//
	// // Briefly show the contents of the barcode, then handle the result outside Barcode Scanner.
	// private void handleDecodeExternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
	//
	// if (barcode != null) {
	// viewfinderView.drawResultBitmap(barcode);
	// }
	//
	// long resultDurationMS;
	// if (getIntent() == null) {
	// resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
	// } else {
	// resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS,
	// DEFAULT_INTENT_RESULT_DURATION_MS);
	// }
	//
	// if (resultDurationMS > 0) {
	// String rawResultString = String.valueOf(rawResult);
	// if (rawResultString.length() > 32) {
	// rawResultString = rawResultString.substring(0, 32) + " ...";
	// }
	// statusView.setText(getString(resultHandler.getDisplayTitle()) + " : " + rawResultString);
	// }
	//
	// if (copyToClipboard && !resultHandler.areContentsSecure()) {
	// ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	// CharSequence text = resultHandler.getDisplayContents();
	// if (text != null) {
	// try {
	// clipboard.setText(text);
	// } catch (NullPointerException npe) {
	// // Some kind of bug inside the clipboard implementation, not due to null input
	// Log.w(TAG, "Clipboard bug", npe);
	// }
	// }
	// }
	//
	// if (source == IntentSource.NATIVE_APP_INTENT) {
	//
	// // Hand back whatever action they requested - this can be changed to Intents.Scan.ACTION when
	// // the deprecated intent is retired.
	// Intent intent = new Intent(getIntent().getAction());
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	// intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
	// intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
	// byte[] rawBytes = rawResult.getRawBytes();
	// if (rawBytes != null && rawBytes.length > 0) {
	// intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
	// }
	// Map<ResultMetadataType,?> metadata = rawResult.getResultMetadata();
	// if (metadata != null) {
	// if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
	// intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION,
	// metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
	// }
	// Integer orientation = (Integer) metadata.get(ResultMetadataType.ORIENTATION);
	// if (orientation != null) {
	// intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
	// }
	// String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
	// if (ecLevel != null) {
	// intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
	// }
	// Iterable<byte[]> byteSegments = (Iterable<byte[]>) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
	// if (byteSegments != null) {
	// int i = 0;
	// for (byte[] byteSegment : byteSegments) {
	// intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
	// i++;
	// }
	// }
	// }
	// sendReplyMessage(R.id.return_scan_result, intent, resultDurationMS);
	//
	// } else if (source == IntentSource.PRODUCT_SEARCH_LINK) {
	//
	// // Reformulate the URL which triggered us into a query, so that the request goes to the same
	// // TLD as the scan URL.
	// int end = sourceUrl.lastIndexOf("/scan");
	// String replyURL = sourceUrl.substring(0, end) + "?q=" + resultHandler.getDisplayContents() + "&source=zxing";
	// sendReplyMessage(R.id.launch_product_query, replyURL, resultDurationMS);
	//
	// } else if (source == IntentSource.ZXING_LINK) {
	//
	// if (scanFromWebPageManager != null && scanFromWebPageManager.isScanFromWebPage()) {
	// String replyURL = scanFromWebPageManager.buildReplyURL(rawResult, resultHandler);
	// sendReplyMessage(R.id.launch_product_query, replyURL, resultDurationMS);
	// }
	//
	// }
	// }
	//
	// private void sendReplyMessage(int id, Object arg, long delayMS) {
	// Message message = Message.obtain(handler, id, arg);
	// if (delayMS > 0L) {
	// handler.sendMessageDelayed(message, delayMS);
	// } else {
	// handler.sendMessage(message);
	// }
	// }
	//
	// /**
	// * We want the help screen to be shown automatically the first time a new version of the app is
	// * run. The easiest way to do this is to check android:versionCode from the manifest, and compare
	// * it to a value stored as a preference.
	// */
	// private boolean showHelpOnFirstLaunch() {
	// try {
	// PackageInfo info = getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
	// int currentVersion = info.versionCode;
	// SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	// int lastVersion = prefs.getInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN, 0);
	// if (currentVersion > lastVersion) {
	// prefs.edit().putInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN, currentVersion).commit();
	// Intent intent = new Intent(this, HelpActivity.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	// // Show the default page on a clean install, and the what's new page on an upgrade.
	// String page = lastVersion == 0 ? HelpActivity.DEFAULT_PAGE : HelpActivity.WHATS_NEW_PAGE;
	// intent.putExtra(HelpActivity.REQUESTED_PAGE_KEY, page);
	// startActivity(intent);
	// return true;
	// }
	// } catch (PackageManager.NameNotFoundException e) {
	// Log.w(TAG, e);
	// }
	// return false;
	// }

	private void initCamera(SurfaceHolder p_oSurfaceHolder) {
		if (p_oSurfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (this.cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			this.cameraManager.openDriver(p_oSurfaceHolder);
			// Creating the handler starts the preview, which can also throw a RuntimeException.
			if (this.handler == null) {
				this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.decodeHints, this.characterSet, this.cameraManager);
			}
			// decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			// displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
		}
	}
	
	/**
	 * Restart the camera preview after delay parameterized
	 * @param p_iDelayMS delay in ms
	 */
	public void restartPreviewAfterDelay(long p_iDelayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, p_iDelayMS);
		}
	}

	/**
	 * Draw the view finder above the preview of the camera
	 */
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
	/**
	 * Return the intent of the decoding ZXing action
	 * @return  the intent of the decoding ZXingaction
	 */
	protected abstract Intent getIntent();
	/**
	 * Access to the surface view to customize the UI
	 * @param p_oSurfaceView surface view created for the camera
	 */
	protected abstract void customizeUiSurfaceView(SurfaceView p_oSurfaceView );
	
	private static final double ASPECT_TOLERANCE = 0.05;

	private Size getOptimalPreviewSize(List<Size> p_oSizes, int p_iWidth, int p_iHeight) {
		double targetRatio = (double) p_iWidth / p_iHeight;
		if (p_oSizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = p_iHeight;

		// Try to find an size match aspect ratio and size
		for (Size size : p_oSizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : p_oSizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
}
