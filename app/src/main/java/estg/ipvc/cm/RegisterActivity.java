package estg.ipvc.cm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import estg.ipvc.cm.utils.MySingleton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout editUsername = findViewById(R.id.textUsername);
                String username = editUsername.getEditText().getText().toString();

                TextInputLayout editEmail = findViewById(R.id.textEmail);
                String email = editEmail.getEditText().getText().toString();

                TextInputLayout editPassword = findViewById(R.id.textPassword);
                String password = editPassword.getEditText().getText().toString();

                if (TextUtils.isEmpty(editUsername.getEditText().getText())) {
                    editUsername.setError("Username é necessário!");
                } else {
                    if (TextUtils.isEmpty(editEmail.getEditText().getText())) {
                        editEmail.setError("Email é necessário!");
                    } else {
                        if (TextUtils.isEmpty(editPassword.getEditText().getText())) {
                            editPassword.setError("Password é necessário!");
                        } else {
                            try {
                                String pw_encrypt = SHA256(password);
                                registo(username, email, pw_encrypt);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        });

    }

    public void registo(String username, String email, String password) {
        String url = "http://192.168.64.2/myslim/api/registar";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("username", username);
        jsonParams.put("email", email);
        jsonParams.put("password", password);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "registo nao efetuado", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException ex) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegisterActivity.this, "registo nao efetuado2", Toast.LENGTH_LONG).show();
                        Log.d("tagconvertstr", "[" + volleyError.getMessage() + "]");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(postRequest);


        Button botaoNotas;
        botaoNotas = (Button) findViewById(R.id.botaoNotas);

        botaoNotas.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void viewRegisterClicked(View v) {
        TextView Jaconta;
        Jaconta = (TextView) findViewById(R.id.JaConta);
        Jaconta.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public static String SHA256(String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, android.util.Base64.DEFAULT);
    }
}




