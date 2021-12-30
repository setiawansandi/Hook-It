package sp.com.p2020358assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION  = 1000 * 3;

    private Animation topAnim, bottomAnim;
    private ImageView image;
    private TextView name, studentid, schclass, appname;
    private GPSTracker gpsTracker;
    private WeakReference<Context> contextRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Objects.requireNonNull(getSupportActionBar()).hide();   // hide action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // hide status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.secondary));  // change status bar colour
        setContentView(R.layout.activity_main);

        // animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bot_anim);

        // hooks
        image = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        studentid = findViewById(R.id.studentid);
        schclass = findViewById(R.id.schclass);
        appname = findViewById(R.id.appname);

        // this values will be passed to fetchAPIdata
        gpsTracker = new GPSTracker(MainActivity.this);
        contextRef = new WeakReference<Context>(MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gpsTracker.canGetLocation()) {
            image.setAnimation(topAnim);
            name.setAnimation(bottomAnim);
            studentid.setAnimation(bottomAnim);
            schclass.setAnimation(bottomAnim);
            appname.setAnimation(bottomAnim);
            // fetchAPIdata constructor for splash screen only with weak ref and splash duration params
            new fetchAPIdata(contextRef, SPLASH_DURATION).execute(gpsTracker);
        }
    }


}