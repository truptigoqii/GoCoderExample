/**
 *  This is sample code provided by Wowza Media Systems, LLC.  All sample code is intended to be a reference for the
 *  purpose of educating developers, and is not intended to be used in any production environment.
 *
 *  IN NO EVENT SHALL WOWZA MEDIA SYSTEMS, LLC BE LIABLE TO YOU OR ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL,
 *  OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 *  EVEN IF WOWZA MEDIA SYSTEMS, LLC HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  WOWZA MEDIA SYSTEMS, LLC SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. ALL CODE PROVIDED HEREUNDER IS PROVIDED "AS IS".
 *  WOWZA MEDIA SYSTEMS, LLC HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *  © 2015 – 2019 Wowza Media Systems, LLC. All rights reserved.
 */

package com.wowza.gocoder.sdk.sampleapp.graphics;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.wowza.gocoder.sdk.api.android.graphics.WOWZBitmap;
import com.wowza.gocoder.sdk.api.android.graphics.WOWZText;
import com.wowza.gocoder.sdk.api.android.graphics.WOWZTextManager;
import com.wowza.gocoder.sdk.sampleapp.CameraActivityBase;
import com.wowza.gocoder.sdk.sampleapp.R;

import java.util.UUID;

/**
 * This activity class demonstrates use of the WOWZBitmap API to display a bitmap
 * as an overlay within the GoCoder SDK camera preview display
 */
public class BitmapOverlayActivity extends CameraActivityBase {

    private WOWZBitmap mWZBitmapBg = null;
    private WOWZBitmap mWZBitmapIcon = null;
    private ScaleGestureDetector mScaleDetector = null;
    private float mScaleFactor = 1.0f;
    int t=0;
    Handler handler = new Handler();
    WOWZText textObject;
    WOWZText textObjectUnit;
    Bitmap overlayBitmapBg=null;
    Bitmap overlayBitmapIcon=null;
    WOWZTextManager wzTextManager = WOWZTextManager.getInstance();

    int[] images = new int[]{R.drawable.overlay_logo, R.drawable.ic_audio_meter, R.drawable.ic_bluetooth};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);

        mRequiredPermissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round((float) dp * (displayMetrics.xdpi / (float) DisplayMetrics.DENSITY_DEFAULT));
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Create the initial Bitmap
        if (sGoCoderSDK != null && mWZCameraView != null && mWZBitmapBg==null && mWZBitmapIcon==null) {
            try {
                // Read in a PNG file from the app resources as a bitmap
                overlayBitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.rounded_rectangle);
                overlayBitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.blue_streamer_asset);
                // Initialize a bitmap renderer with the bitmap
                mWZBitmapBg = new WOWZBitmap(overlayBitmapBg);
                mWZBitmapIcon = new WOWZBitmap(overlayBitmapIcon);

                // Center the bitmap in the display
                mWZBitmapBg.setPosition(WOWZBitmap.CENTER, WOWZBitmap.CENTER);
                //mWZBitmapIcon.setScale((float) 0.5,-8);
                android.view.Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int height = display.getHeight();
                int width = display.getWidth();


                mWZBitmapIcon.setPosition((int) (width*0.4), WOWZBitmap.CENTER);
                // Scale the bitmap initially to 75% of the display surface width
                //mWZBitmapBg.setScale(0.1f, WOWZBitmap.FRAME_WIDTH);
                //mWZBitmapIcon.setScale(0.75f, WOWZBitmap.FRAME_WIDTH);

                // Register the bitmap renderer with the GoCoder camera preview view as a frame listener
                mWZCameraView.registerFrameRenderer(mWZBitmapIcon);

                mWZCameraView.registerFrameRenderer(mWZBitmapBg);
                Toast.makeText(this, getString(R.string.bitmap_overlay_help), Toast.LENGTH_LONG).show();

                // Get the GoCoder SDK text manager
                // Load a True Type font file from the app's assets folder
                UUID fontId = wzTextManager.loadFont("Robotoregular.ttf", 50, 15, 0);
                UUID fontIdUnit = wzTextManager.loadFont("Robotoregular.ttf", 25, 15, 1);
                // Display the text centered on the screen
                textObject = wzTextManager.createTextObject(fontId, "Become a Wowza Ninja", 0.84f, 0.47f, 0f);
                textObject.setPosition(WOWZText.CENTER, (int) (height*0.52));
                textObject.setAlignment(WOWZText.CENTER);

                textObjectUnit= wzTextManager.createTextObject(fontIdUnit, "Bpm", 0.84f, 0.47f, 0f);
                textObjectUnit.setPosition(WOWZText.CENTER,(int) (height*0.51));
                textObjectUnit.setAlignment(WOWZText.CENTER);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //overlayBitmapBg = BitmapFactory.decodeResource(getResources(), images[t]);
                        t++;
                        textObject.setText(t + "");
                        textObjectUnit.setText("Bmp");
                        //mWZCameraView.unregisterFrameRenderer(mWZBitmap);


                        //mWZBitmapBg.setBitmap(overlayBitmapBg);
                        // Initialize a bitmap renderer with the bitmap
                        /*mWZBitmap = new WOWZBitmap(overlayBitmap);
                        mWZBitmap.setPosition(WOWZBitmap.CENTER, WOWZBitmap.CENTER);

                        // Scale the bitmap initially to 75% of the display surface width
                        mWZBitmap.setScale(0.75f, WOWZBitmap.FRAME_WIDTH);
                        mWZCameraView.registerFrameRenderer(mWZBitmap);*/

                        if (t < 3) {
                            handler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);
            }catch (Exception e){
                Log.e("error",e.getMessage());
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWZCameraView.unregisterFrameRenderer(mWZBitmapBg);
        mWZCameraView.unregisterFrameRenderer(mWZBitmapIcon);
        textObject=null;
        textObjectUnit=null;
       wzTextManager.release();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sGoCoderSDK != null)
            WOWZTextManager.getInstance().clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Send all events to the scale gesture detector
        mScaleDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Get the scale factor and apply it to the bitmap's scale
            mScaleFactor *= detector.getScaleFactor();

            // Cap the scale factor
            mScaleFactor = Math.max(0.3f, Math.min(mScaleFactor, 5.0f));
            if (mWZBitmapBg != null)
                mWZBitmapBg.setScale(mScaleFactor); //, WOWZBitmap.SURFACE_WIDTH);

            return true;
        }
    }

    /**
     * Click handler for the broadcast button
     */
    public void onToggleBroadcast(View v) {
        super.onToggleBroadcast(v);
    }

    /**
     * Click handler for the Settings button
     */
    public void onSettings(View v) {
        super.onToggleBroadcast(v);
    }


}
