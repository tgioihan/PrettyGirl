package com.bestfunforever.app.prettygirl;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;

import com.android.lib.core.activity.SocialNetWorkActivity;

public abstract class MenuActionActivity extends SocialNetWorkActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.like:
			likePage();
			return true;

		case R.id.more:
			openGooglePlay();
			return true;

		case R.id.contact:
			openEmailApp();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void openEmailApp() {
		final Intent emailIntent = new Intent(Intent.ACTION_SEND);

		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "bestfunforever@gmail.com" });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback " + getString(R.string.app_name));

		/* Send it off to the Activity-Chooser */
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	private void openGooglePlay() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:bestfunforever")));
		} catch (android.content.ActivityNotFoundException anfe) {
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_VIEW);
			intent1.addCategory(Intent.CATEGORY_BROWSABLE);
			intent1.setData(Uri.parse("http://play.google.com/store/search?q=pub:bestfunforever"));
			startActivity(intent1);
		}
	}

	private void likePage() {
		try {
			getPackageManager().getPackageInfo("com.facebook.katana", 0);
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_VIEW);
			intent1.addCategory(Intent.CATEGORY_BROWSABLE);
			intent1.setData(Uri.parse("fb://page/" + Config.page_id[0]));
			startActivity(intent1);
		} catch (Exception e) {
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_VIEW);
			intent1.addCategory(Intent.CATEGORY_BROWSABLE);
			intent1.setData(Uri.parse("https://www.facebook.com/" + Config.page_id[0]));
			startActivity(intent1);
		}

	}

}
