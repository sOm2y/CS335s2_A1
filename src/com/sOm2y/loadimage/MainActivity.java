package com.sOm2y.loadimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
//	String imageUrl = "http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/home_image";
//	Bitmap bmImage;
//	ImageView imView;
//	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 loadImage("http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/home_image",
		 R.id.imageView1);
//		imView = (ImageView) findViewById(R.id.imageView1);
//		imView.setImageBitmap(returnBitMap(imageUrl));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		executorService.shutdown();
		super.onDestroy();
	}

//	public Bitmap returnBitMap(String url) {
//
//		URL myFileUrl = null;
//		Bitmap bitmap = null;
//		try {
//			myFileUrl = new URL(url);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		try {
//			HttpURLConnection conn = (HttpURLConnection) myFileUrl
//					.openConnection();
//			conn.setDoInput(true);
//			conn.connect();
//			InputStream is = conn.getInputStream();
//			bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return bitmap;
//	}
	private void loadImage(final String url, final int id) {
		final Handler handler = new Handler();
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					final Drawable drawable = Drawable.createFromStream(
							new URL(url).openStream(), "image.png");
					handler.post(new Runnable() {

						@Override
						public void run() {
							((ImageView) MainActivity.this.findViewById(id))
									.setImageDrawable(drawable);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

}
