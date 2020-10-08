package ar.edu.ips.aus.android.multitask.single_thread;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SingleThreadActivity extends Activity {
	private Bitmap mBitmap;
	private ImageView mImageView;
	private int mDelay = 5000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_thread);

		mImageView = (ImageView) findViewById(R.id.imageView);

		final Button button = (Button) findViewById(R.id.loadSingle);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadIconSingleThread();
			}
		});

		final Button button_multi_thread = (Button) findViewById(R.id.loadMulti2);
		button_multi_thread.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadIconMultiThread();
			}
		});

		final Button button_bad_behaviour = (Button) findViewById(R.id.loadMulti1);
		button_bad_behaviour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadIconMultiThreadBadBehaviour();
			}
		});

		final Button button_async_task = (Button) findViewById(R.id.loadAsync);
		button_async_task.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadIconAsyncTask();
			}
		});

		final Button otherButton = (Button) findViewById(R.id.otherButton);
		otherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SingleThreadActivity.this, "I'm Working",
						Toast.LENGTH_SHORT).show();
			}
		});

		final Button clearButton = (Button) findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mImageView.setImageBitmap(null);
			}
		});
	}

	//
	// same thread implementation
	//
	private void loadIconSingleThread() {
		try {
			Thread.sleep(mDelay*5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.android);

		mImageView.setImageBitmap(mBitmap);
	}

	//
	// multi-threaded implementation
	//
	private void loadIconMultiThreadBadBehaviour() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(mDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				mBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.android);

				// updating imageView directly -> error!
				mImageView.setImageBitmap(mBitmap);
			}
		}).start();
	}

	//
	// multi-threaded implementation
	//
	private void loadIconMultiThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(mDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				mBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.android);

				// updating imageView from UI thread
				mImageView.post(new Runnable() {
					@Override
					public void run() {
						mImageView.setImageBitmap(mBitmap);
					}
				});
			}
		}).start();
	}

	//
	// AsyncTask implementation
	//
	private void loadIconAsyncTask() {
		AsyncTask<Void, Void, Bitmap> task = new DownloadImageTask();

		task.execute();
	}


	class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */

		protected Bitmap doInBackground(Void... voids) {

			Log.i("ASYNC_TASK", "ejecutando doInBackground()...");
			try {
				Thread.sleep(mDelay);
			} catch (InterruptedException e) {
				// intentionally left blank
			}
			mBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.android);

			return mBitmap;
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */

		protected void onPostExecute(Bitmap result) {
			Log.i("ASYNC_TASK", "ejecutando onPostExecute()...");
			mImageView.setImageBitmap(result);
		}
	}

}


