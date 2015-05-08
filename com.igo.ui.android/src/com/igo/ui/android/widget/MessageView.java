package com.igo.ui.android.widget;

import com.igo.ui.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageView extends RelativeLayout {

	private TextView textView = null;
	private ImageView imageView = null;

	public MessageView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// textView = (TextView) findViewById(R.id.oneTextView);
		init();

		addView(imageView);
	}

	private void init() {
		if (imageView == null) {
			imageView = new ImageView(getContext());
			imageView.setImageResource(R.drawable.ic_launcher);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new GridView.LayoutParams(120, 110));
		}
	}
}
