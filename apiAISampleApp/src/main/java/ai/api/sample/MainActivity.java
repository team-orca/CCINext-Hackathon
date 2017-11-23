package ai.api.sample;

/***********************************************************************************************************************
 *
 * API.AI Android SDK -  API.AI libraries usage example
 * =================================================
 *
 * Copyright (C) 2015 by Speaktoit, Inc. (https://www.speaktoit.com)
 * https://www.api.ai
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************/

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getName();
    private ImageView colaBar;
    private ImageView profile;
    private TextView point;
    private int myPoint;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPoint = 125;
        int points=getIntent().getIntExtra("puan",0);
        Log.d("points",points+"");
        myPoint +=points;
        checkIntent(getIntent());

        Switch myswitch = (Switch)findViewById(R.id.switch1);
        final Intent intent = new Intent(MainActivity.this, ChatHeadService.class);
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkDrawOverlayPermission();
                    MainActivity.this.startService(intent);
                } else
                    stopService(intent);
            }
        });
        colaBar = (ImageView)(findViewById(R.id.cola));
        profile = (ImageView)(findViewById(R.id.profile));
        point = (TextView)(findViewById(R.id.puan));
        int [] arr = {R.drawable.bottle_1, R.drawable.bottle_2, R.drawable.bottle_3, R.drawable.bottle_4, R.drawable.bottle_5,
                R.drawable.bottle_6, R.drawable.bottle_7, R.drawable.bottle_8, R.drawable.bottle_9, R.drawable.bottle_10};

        colaBar.setImageResource(arr[createImage(myPoint)]);
        point.setText("Puan : "+myPoint+"ml");
    }
    private int createImage(int point){
        if(point == 0)
            return 0;
        else if(0<point && point<=100)
            return 1;
        else if(100<point && point<=200)
            return 2;
        else if(200<point && point<=300)
            return 3;
        else if(300<point && point<=400)
            return 4;
        else if(400<point && point<=500)
            return 5;
        else if(500<point && point<=600)
            return 6;
        else if(600<point && point<=700)
            return 7;
        else if(700<point && point<=800)
            return 8;
        else if(800<point && point<900)
            return 9;
        else
            return 10;

    }


    public final static int REQUEST_CODE = 65;

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            // ** if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
                    Intent intent = new Intent(MainActivity.this, AITextSampleActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void textSampleClick(final View view) {
        startActivity(AITextSampleActivity.class);
    }

    private void startActivity(Class<?> cls) {
        final Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);
    }
    public void checkIntent(Intent intent) {
        if (intent.hasExtra("click_action")) {
            ClickActionHelper.startActivity(intent.getStringExtra("click_action"), intent.getExtras(), this);
        }
    }
}
