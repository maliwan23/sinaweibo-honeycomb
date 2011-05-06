package com.lenovo.dll.SinaWeibo;

import java.net.URL;
import weibo4andriod.User;
import weibo4andriod.Weibo;
import weibo4andriod.WeiboException;
import weibo4andriod.http.AccessToken;
import weibo4andriod.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener,
		OnGestureListener {

	private GestureDetector mGestureDetector;
	private ImageLoader imageLoader;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.main_fragment);
			setEnabled(false);

			imageLoader = new ImageLoader(this.getApplicationContext());

			Button btnCaptureVideo = (Button) findViewById(R.id.btnCaptureVideo);
			btnCaptureVideo.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					captureVideo();
				}
			});

			mGestureDetector = new GestureDetector(this);

			Button btnWeibo = (Button) findViewById(R.id.btnWeibo);
			btnWeibo.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						FloatIcon fi = (FloatIcon) getFragmentManager()
								.findFragmentById(R.id.float_fragment);
						FloatPanel fp = (FloatPanel) getFragmentManager()
								.findFragmentById(R.id.float_panel);
						fi.hide();
						fp.show();
					} catch (Exception ex) {
						Log.e(this.toString(), ex.toString());
					}
				}
			});

			try {
				FloatPanel f = (FloatPanel) getFragmentManager()
						.findFragmentById(R.id.float_panel);
				f.hide();
			} catch (Exception ex) {
				Log.e(this.toString(), ex.toString());
			}

			View touchView = (View) findViewById(R.id.float_panel);
			touchView.setLongClickable(true);
			touchView.setOnTouchListener(this);
		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		try {
			VideoFragment video = (VideoFragment) getFragmentManager()
					.findFragmentById(R.id.video_fragment);
			if (video == null)
				return;

			uri = intent.getData();
			if (uri == null) {
				video.resumeVideo();
				return;
			}

			String scheme = uri.getScheme();
			String host = uri.getHost();
			if (scheme.matches("weibo4andriod")) {
				if (host.matches("OAuthActivity")) {
					postLogin(uri);
					// video.resumeVideo();
				} else if (host.matches("SkipLogin")) {
					skipLogin();
					// video.resumeVideo();
				} else if (host.matches("UpdateHomepage")) {
					// RadioGroup rg = (RadioGroup)
					// findViewById(R.id.main_radio);
					// if (rg != null)
					// rg.check(R.id.radioHomepage);

					WeiboHomePage homepage = (WeiboHomePage) getFragmentManager()
							.findFragmentById(R.id.homepage_fragment);
					if (homepage != null) {
						homepage.updateAsync();
					}
				} else if (host.matches("NewMessage")) {
					alertNewMessage();
				}
			} else if (scheme.matches("video")) {
				String path = uri.getPath();
				video.startVideo(path);
			}
		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}

	private void alertNewMessage() {
		try {
			FloatIcon fi = (FloatIcon) getFragmentManager().findFragmentById(
					R.id.float_fragment);
			fi.alert(true);
		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}

	private void captureVideo() {
		try {
			VideoFragment video = (VideoFragment) getFragmentManager()
					.findFragmentById(R.id.video_fragment);

			if (video != null) {
				video.captureVideo();

				final ImageView img = (ImageView) findViewById(R.id.imgSnapshot);
				img.setImageBitmap(video.getSnapshot());

				final Animation animation = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.scale);
				final Animation animation2 = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.translate);

				img.startAnimation(animation);

				animation.setAnimationListener(new AnimationListener() {

					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						//
						img.startAnimation(animation2);
						animation2
								.setAnimationListener(new AnimationListener() {

									public void onAnimationStart(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									public void onAnimationRepeat(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									public void onAnimationEnd(
											Animation animation) {

										img.setVisibility(View.INVISIBLE);
										VideoFragment video = (VideoFragment) getFragmentManager()
												.findFragmentById(
														R.id.video_fragment);
										ImageView img2 = (ImageView) findViewById(R.id.imgSnapshot2);
										img2.setImageBitmap(video.getSnapshot());
									}
								});
					}
				});
			}
		} catch (Exception ex) {
			Log.e(this.toString(), ex.toString());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			try {
				FloatIcon ff = (FloatIcon) getFragmentManager()
						.findFragmentById(R.id.float_fragment);
				MainFragment mf = (MainFragment) getFragmentManager()
						.findFragmentById(R.id.main_fragment);
				if (mf.isHidden()) {
					ff.hide();
					mf.show();
				} else {
					mf.hide();
					ff.show();
				}
			} catch (Exception ex) {
				Log.e(this.toString(), ex.toString());
			}
			return true;
		}
		return super.onKeyDown(keyCode, msg);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// Toast.makeText(this, event.toString(), Toast.LENGTH_LONG).show();
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > 50 && velocityX > 100) {
			try {
				FloatIcon ff = (FloatIcon) getFragmentManager()
						.findFragmentById(R.id.float_fragment);
				FloatPanel fp = (FloatPanel) getFragmentManager()
						.findFragmentById(R.id.float_panel);
				if (!fp.isHidden()) {
					fp.hide();
					ff.show();
				}
			} catch (Exception ex) {
				Log.e(this.toString(), ex.toString());
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	private void setEnabled(boolean enabled) {
		RadioGroup rg = (RadioGroup) findViewById(R.id.main_radio);
		if (rg != null) {
			rg.setEnabled(enabled);
			for (int i = 0; i < rg.getChildCount(); i++) {
				rg.getChildAt(i).setEnabled(enabled);
			}
		}
	}

	private RequestToken requestToken;
	private AccessToken accessToken;
	private Uri uri;
	protected InitTask mInitTask;

	protected class InitTask extends AsyncTask<Boolean, Integer, Boolean> {

		@Override
		// params[0]: true - normal login; false - skip login
		protected Boolean doInBackground(Boolean... params) {

			try {
				if (params != null && params.length > 0 && !params[0]) {
					OAuthConstant.getInstance().init();
					if (!OAuthConstant.getInstance().loadSettings(MainActivity.this)) {
						Toast.makeText(MainActivity.this, "Failed to skip login!", Toast.LENGTH_LONG).show();
						return false;
					}
				} else {
					requestToken = OAuthConstant.getInstance().getRequestToken();
					accessToken = requestToken.getAccessToken(uri
							.getQueryParameter("oauth_verifier"));
					OAuthConstant.getInstance().setAccessToken(accessToken);
					OAuthConstant.getInstance().saveSettings(MainActivity.this);
				}

				Weibo weibo = OAuthConstant.getInstance().getWeibo();
				weibo.setToken(OAuthConstant.getInstance().getToken(),
						OAuthConstant.getInstance().getTokenSecret());

				user = weibo.verifyCredentials();

			} catch (WeiboException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}

		// -- gets called just before thread begins
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

		}

		// -- called if the cancel button is pressed
		@Override
		protected void onCancelled() {
			super.onCancelled();

		}

		// -- called as soon as doInBackground method completes
		// -- notice that the third param gets passed to this method
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			try {

				TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
				if (txtUserName != null)
					txtUserName.setText(user.getScreenName());

				TextView txtUserStatus = (TextView) findViewById(R.id.txtUserStatus);
				if (txtUserStatus != null)
					txtUserStatus.setText(user.getDescription());

				TextView txtUserInfo = (TextView) findViewById(R.id.txtUserInfo);
				if (txtUserInfo != null)
					txtUserInfo.setText(String.format("关注 %d　　粉丝 %d　　微博%d",
							user.getFriendsCount(), user.getFollowersCount(),
							user.getStatusesCount()));

				ImageView imgAvator = (ImageView) findViewById(R.id.imgAvator);
				URL url = user.getProfileImageURL();
				if (imgAvator != null && url != null) {
					imgAvator.setTag(url.toString());
					imageLoader.DisplayImage(url.toString(), MainActivity.this,
							imgAvator);
				}

				OAuthFragment of = (OAuthFragment) getFragmentManager()
						.findFragmentById(R.id.oauth_fragment);
				if (!of.isHidden()) {
					of.hide();
				}

				setEnabled(true);
				RadioGroup rg = (RadioGroup) findViewById(R.id.main_radio);

				if (rg != null) {
					rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup arg0, int arg1) {
							WeiboHomePage hf = (WeiboHomePage) getFragmentManager()
									.findFragmentById(R.id.homepage_fragment);
							if (!hf.isHidden())
								hf.hide();

							WeiboAtPage af = (WeiboAtPage) getFragmentManager()
									.findFragmentById(R.id.atpage_fragment);
							if (!af.isHidden())
								af.hide();

							WeiboPostMessage pf = (WeiboPostMessage) getFragmentManager()
									.findFragmentById(R.id.post_fragment);
							if (!pf.isHidden())
								pf.hide();

							switch (arg1) {
							case R.id.radioHomepage:
								hf.show();
								break;

							case R.id.radioAtPage:
								af.show();
								break;

							case R.id.radioPostMessage:
								pf.show();
								break;
							}
						}
					});
					rg.check(R.id.radioHomepage);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}

	}

	private void postLogin(Uri uri) {

		mInitTask = new InitTask();
		mInitTask.execute(true);

	}

	private void skipLogin() {

		mInitTask = new InitTask();
		mInitTask.execute(false);

	}
}