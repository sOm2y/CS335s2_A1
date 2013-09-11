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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private Button peopleBt;
	private Button courseBt;
	private Button seminarBt;
	private Button newsBt;
	private Button eventsBt;
	Button btnStartProgress;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadImage(
				"http://redsox.tcs.auckland.ac.nz/CSS/CSService.svc/home_image",
				R.id.imageView1);

		peopleBt = (Button) this.findViewById(R.id.button2);
		peopleBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressBar = new ProgressDialog(v.getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("File downloading ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				progressBar.show();

				Intent it = new Intent();
				it.setClass(MainActivity.this, peopleActivity.class);

				startActivity(it);
			}
		});
		courseBt = (Button) this.findViewById(R.id.button1);
		courseBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MainActivity.this, coursesActivity.class);

				startActivity(it);
			}
		});

		seminarBt = (Button) this.findViewById(R.id.button5);
		seminarBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MainActivity.this, seminarFeedRSS.class);

				startActivity(it);
			}
		});
		newsBt = (Button) this.findViewById(R.id.button3);
		newsBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MainActivity.this, newsFeedRSS.class);

				startActivity(it);
			}
		});
		eventsBt = (Button) this.findViewById(R.id.button4);
		eventsBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MainActivity.this, eventsFeedRSS.class);

				startActivity(it);
			}
		});
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
