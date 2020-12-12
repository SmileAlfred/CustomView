package com.example.customview.catchcrazycat;

import android.app.Activity;
import android.os.Bundle;

public class CatchCrazyCatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new CrazyCat_Playground(this));
	}

}
