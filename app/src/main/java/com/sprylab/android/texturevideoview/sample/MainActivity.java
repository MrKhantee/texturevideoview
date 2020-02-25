/*
 * Copyright (C) 2014 sprylab technologies GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sprylab.android.texturevideoview.sample;

import android.app.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity
{

    private static final String TAG = MainActivity.class.getName();
    private TextureVideoView mVideoView;
    ///private Button mCaptureFrameButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (TextureVideoView) findViewById(R.id.video_view);

        initVideoView();
		
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
        switch (item.getItemId())
		{
			case R.id.capture:
				saveCurrentFrame();
				break;
			case R.id.exit:
				finishAndRemoveTask();
        }
        return super.onOptionsItemSelected(item);
    }
	
    @Override
    protected void onDestroy()
	{
        super.onDestroy();

        if (mVideoView != null)
		{
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    private void initVideoView()
	{
        mVideoView.setVideoPath(getVideoPath());
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(final MediaPlayer mp)
				{
					startVideoPlayback();
					///startVideoAnimation();
				}
			});
    }

    private void startVideoPlayback()
	{
        // "forces" anti-aliasing - but increases time for taking frames - so keep it disabled
        // mVideoView.setScaleX(1.00001f);
        mVideoView.start();
    }

    private void startVideoAnimation()
	{
        mVideoView.animate().rotationBy(360.0f).setDuration(mVideoView.getDuration()).start();
    }

    private String getVideoPath()
	{
        return "android.resource://" + getPackageName() + "/" + R.raw.video;
    }

    private void saveCurrentFrame()
	{
        final Bitmap currentFrameBitmap = mVideoView.getBitmap();

        final File currentFrameFile = new File(getExternalFilesDir("frames"), "frame" + System.currentTimeMillis() + ".jpg");
        writeBitmapToFile(currentFrameBitmap, currentFrameFile);

        currentFrameBitmap.recycle();

        Toast.makeText(this, "Frame saved as " + currentFrameFile.getAbsolutePath() + ".", Toast.LENGTH_SHORT).show();
    }

    private void writeBitmapToFile(final Bitmap bitmap, final File file)
	{
        try
		{
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        }
		catch (final IOException e)
		{
            Log.e(TAG, "Error writing bitmap to file.", e);
        }
    }

}
