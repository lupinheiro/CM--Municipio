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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        final Button login = findViewById(R.id.cirLoginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TextInputLayout editTextUsername = (TextInputLayout) findViewById(R.id.textInputUsername);
                String username = editTextUsername.getEditText().getText().toString();

                TextInputLayout editTextPassword = (TextInputLayout) findViewById(R.id.textInputPassword);
                String password = editTextPassword.getEditText().getText().toString();

                if (TextUtils.isEmpty(editTextUsername.getEditText().getText())) {
                    editTextUsername.setError("Username é obrigatório!");
                } else {
                    if (TextUtils.isEmpty(editTextPassword.getEditText().getText())) {
                        editTextPassword.setError("Password é obrigatório!");
                    } else {
                        try {
                            String pw_encrypt = SHA256(password);
                            login(username, pw_encrypt);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }
        public void login(String username, String password){
            String url = "http://192.168.64.2/myslim/api/logar";

            Map<String, String> jsonParams = new HashMap<String, String>();
            jsonParams.put("username", username);
            jsonParams.put("password", password);

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                    new JSONObject(jsonParams),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    Toast.makeText(LoginActivity.this, "sucesso", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(LoginActivity.this, MapaActivity.class);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(LoginActivity.this, "user ou password errado", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException ex) {
                                Log.d("asdasdasd", ex.toString());
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("testeteste", "[" + volleyError.getMessage() + "]");
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

            //Access the request queue singleton
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
        public void viewRegisterClicked (View v){
            TextView Naoconta;
            Naoconta = (TextView) findViewById(R.id.NaoConta);
            Naoconta.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),
                            RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }

    public static String SHA256 (String text) throws NoSuchAlgorithmException{

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, android.util.Base64.DEFAULT);

    }
}
