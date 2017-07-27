package tv.letv.com.leplugingradletest;

import android.app.Activity;
import android.os.Bundle;

import com.letv.tv.publiclib.LibTest;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LibTest.
//        LibTest.
        LibTest.output();
    }
}
