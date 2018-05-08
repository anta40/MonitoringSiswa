package studio.emcorp.monitoringsiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;


public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtTglLahir;
    ProgressBar pB;
    Button btnLogin;
    private View parent_view;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String login,name,recid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        parent_view = findViewById(android.R.id.content);
        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtTglLahir = (EditText)findViewById(R.id.edtTglLahir);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        pB = (ProgressBar)findViewById(R.id.pB);

        sharedpreferences = getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        login = sharedpreferences.getString(Variable.LOGIN_KEY, null);

        if(login!=null){
            if(login.equals("1")){
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()){
                    cekInfo();
                }else {
                    Toast.makeText(getApplicationContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void cekInfo(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_LOGIN,
                edtUsername.getText().toString(),
                edtTglLahir.getText().toString()
        );
    }

    class CekAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private final String LOGIN_URL = Variable.BASE_URL;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put(Variable.PARAM_KEY, args[0]);
                params.put(Variable.PARAM_FUNCTION, args[1]);
                params.put(Variable.PARAM_NIS, args[2]);
                params.put(Variable.PARAM_TGL, args[3]);
                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            if (json != null) {
//                Toast.makeText(getApplicationContext(),json.toString(),Toast.LENGTH_SHORT).show();

                try {
                    JSONObject parentObject = new JSONObject(json.toString());
                    JSONObject userDetails = parentObject.getJSONObject("hasil");
                    String message = userDetails.getString("message");
                    String success = userDetails.getString("success");
                    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="", tgl_lahir = "", alamat = "";
                    if(success.equals("1")){
                        idKelas = userDetails.getString("id_kelas");
                        idSsiwa = userDetails.getString("id_siswa");
                        nama = userDetails.getString("nama_siswa");
                        nis = userDetails.getString("nis");
                        namaKelas = userDetails.getString("nama_kelas");
                        tgl_lahir = userDetails.getString("tgl_lahir");
                        alamat = userDetails.getString("alamat");

                        editor.remove(Variable.LOGIN_KEY);
                        editor.remove(Variable.IDKELAS_KEY);
                        editor.remove(Variable.IDSISWA_KEY);
                        editor.remove(Variable.NAMA_KEY);
                        editor.remove(Variable.NIS_KEY);
                        editor.remove(Variable.NAMAKELAS_KEY);
                        editor.remove(Variable.TGLLAHIR_KEY);
                        editor.remove(Variable.ALAMAT_KEY);

                        editor.putString(Variable.LOGIN_KEY, "1");
                        editor.putString(Variable.IDKELAS_KEY, idKelas);
                        editor.putString(Variable.IDSISWA_KEY, idSsiwa);
                        editor.putString(Variable.NAMA_KEY, nama);
                        editor.putString(Variable.NIS_KEY, nis);
                        editor.putString(Variable.NAMAKELAS_KEY, namaKelas);
                        editor.putString(Variable.TGLLAHIR_KEY, tgl_lahir);
                        editor.putString(Variable.ALAMAT_KEY, alamat);
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Welcome "+nama,Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
                }



            }
            pB.setVisibility(View.INVISIBLE);
        }

    };

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
