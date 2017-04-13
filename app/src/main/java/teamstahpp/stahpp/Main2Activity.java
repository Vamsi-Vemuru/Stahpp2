package teamstahpp.stahpp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ArrayList<String> ab = getIntent().getStringArrayListExtra("packagelist");
        System.out.println(ab.toString());

    }
}
