package view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import estg.ipvc.cm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    void init(){
        Button profile = findViewById(R.id.btn_profile);
        Button login = findViewById(R.id.btn_login);
        Button sign_up = findViewById(R.id.btn_sign_up);
        PrefManager prefManager = PrefManager.getInstance(MainActivity.this);
        if(prefManager.isLoggedIn()) {
            login.setVisibility(View.GONE);
            sign_up.setVisibility(View.GONE);
            profile.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                }
            });
        } else {
            profile.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });
            sign_up.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                }
            });
        }
    }
}