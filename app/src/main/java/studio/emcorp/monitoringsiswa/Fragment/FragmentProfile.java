package studio.emcorp.monitoringsiswa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Spinner;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.R;

public class FragmentProfile extends Fragment implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {
    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        return fragment;
    }
    ProgressBar pB;
    ListView list;
    Hashtable<String, String> daftar_id_kelas_1 = new Hashtable<String,String>();
    Hashtable<String, String> daftar_id_kelas_2 = new Hashtable<String,String>();
    TextView tvNama,tvNis,tvIdSiswa,tvKelas,tvIdKelas,tvTglLahir, tvAlamat;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Button btnProfile;
    Button btnProfilePic;
    CropImageView cv;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="", tglLahir = "", alamat="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);
        tvNama = (TextView) view.findViewById(R.id.tvNama);
        tvNis = (TextView) view.findViewById(R.id.tvNis);
        tvIdSiswa = (TextView) view.findViewById(R.id.tvIdSiswa);
        tvKelas = (TextView) view.findViewById(R.id.tvKelas);
        tvIdKelas = (TextView) view.findViewById(R.id.tvIdKelas);
        tvTglLahir = (TextView) view.findViewById(R.id.tvTglLahir);
        tvAlamat = (TextView) view.findViewById(R.id.tvAlamat);
        cv = (CropImageView) view.findViewById(R.id.cropImageView);

        cv.setOnSetImageUriCompleteListener(this);
        cv.setOnCropImageCompleteListener(this);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        btnProfile = (Button) view.findViewById(R.id.btnProfile);
        btnProfilePic = (Button) view.findViewById(R.id.btnProfilePic);

        daftar_id_kelas_1.put("-", "-");
        daftar_id_kelas_1.put("X.1", "1");
        daftar_id_kelas_1.put("X.2", "2");
        daftar_id_kelas_1.put("X.3", "3");
        daftar_id_kelas_1.put("X.4", "4");
        daftar_id_kelas_1.put("X.5", "5");
        daftar_id_kelas_1.put("XI IPA.1", "6");
        daftar_id_kelas_1.put("XI IPA.2", "7");
        daftar_id_kelas_1.put("XI IPA.3", "8");
        daftar_id_kelas_1.put("XI IPS.2", "9");
        daftar_id_kelas_1.put("XI IPS.1", "11");
        daftar_id_kelas_1.put("XII IPA.1", "12");
        daftar_id_kelas_1.put("XII IPA.2", "13");
        daftar_id_kelas_1.put("XII IPA.3", "14");
        daftar_id_kelas_1.put("XII IPS.1", "15");
        daftar_id_kelas_1.put("XII IPS.2", "16");

        daftar_id_kelas_2.put("-", "-");
        daftar_id_kelas_2.put("1", "X.1");
        daftar_id_kelas_2.put("2", "X.2");
        daftar_id_kelas_2.put("3", "X.3");
        daftar_id_kelas_2.put("4", "X.4");
        daftar_id_kelas_2.put("5", "X.5");
        daftar_id_kelas_2.put("6", "XI IPA.1");
        daftar_id_kelas_2.put("7", "XI IPA.2");
        daftar_id_kelas_2.put("8", "XI IPA.3");
        daftar_id_kelas_2.put("9", "XI IPS.2");
        daftar_id_kelas_2.put("11", "XI IPS.1");
        daftar_id_kelas_2.put("12", "XII IPA.1");
        daftar_id_kelas_2.put("13", "XII IPA.2");
        daftar_id_kelas_2.put("14", "XII IPA.3");
        daftar_id_kelas_2.put("15", "XII IPS.1");
        daftar_id_kelas_2.put("16", "XII IPS.2");

        btnProfile.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)     {
                showProfileDialog();
            }
        });

        btnProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .start(getActivity());
            }
        });

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);
        tglLahir = sharedpreferences.getString(Variable.TGLLAHIR_KEY, null);
        alamat = sharedpreferences.getString(Variable.ALAMAT_KEY, null);

        tvNama.setText(nama);
        tvNis.setText(nis);
        tvIdSiswa.setText(idSsiwa);
        tvKelas.setText(namaKelas);
        tvIdKelas.setText(idKelas);
        tvTglLahir.setText(tglLahir);
        tvAlamat.setText(alamat);

        return view;
    }

    protected void showProfileDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.profile_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String alamatBaru = ((EditText) promptView.findViewById(R.id.edtAlamat)).getText().toString();
                        final String kelasBaru = ((Spinner) promptView.findViewById(R.id.spnKelas)).getSelectedItem().toString();
                        final String id_kelas = daftar_id_kelas_1.get(kelasBaru);
                       // Toast.makeText(getContext(), "Alamat: "+alamatBaru+" ,kelas: "+kelasBaru+" ID kelas: "+id_kelas, Toast.LENGTH_LONG).show();

                        new UpdateProfileAsync().execute(
                                Variable.API_KEY,
                                Variable.FUNGSI_UPDATEPROFILE,
                                nis,
                                id_kelas,
                                alamatBaru
                        );
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(getActivity(), "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            //Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(getActivity(), "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }

    private void handleCropResult(CropImageView.CropResult result) {
       Toast.makeText(getActivity(), "Result: "+result.isSuccessful(), Toast.LENGTH_SHORT).show();
    }


    class UpdateProfileAsync extends AsyncTask<String, String, JSONObject>{

        JSONParser jsonParser = new JSONParser();
        private final String UPDATE_PROFILE_URL = Variable.BASE_URL;

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
                params.put(Variable.PARAM_ID_KELAS, args[3]);
                params.put(Variable.PARAM_ALAMAT, args[4]);
                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        UPDATE_PROFILE_URL, "POST", params);

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
            if (json != null){
                try {
                  //  JSONArray jsonArray = json.getJSONArray("hasil");

                    if (json.length() == 0){

                    }
                    else {
                        JSONObject jso = json.getJSONObject("hasil");
                        String res = jso.getString("success");
                        String msg = "";

                        if (res.equalsIgnoreCase("1")){
                            String nAlamat = jso.getString("alamat");
                            String nId = jso.getString("id_kelas");

                           // Toast.makeText(getContext(), nAlamat+" "+nId, Toast.LENGTH_LONG).show();


                            SharedPreferences sharedpreferences = getContext().getSharedPreferences(Variable.PREFERENCES_NAME,
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            if (!nId.isEmpty() && !nId.equals("-")) editor.remove(Variable.IDKELAS_KEY);
                            if (!nId.isEmpty() && !nId.equals("-")) editor.remove(Variable.NAMAKELAS_KEY);
                            if (!nAlamat.isEmpty()) editor.remove(Variable.ALAMAT_KEY);

                            if (!nId.isEmpty() && !nId.equals("-")) editor.putString(Variable.IDKELAS_KEY, nId);
                            if (!nId.isEmpty() && !nId.equals("-")) editor.putString(Variable.NAMAKELAS_KEY, daftar_id_kelas_2.get(nId));
                            if (!nAlamat.isEmpty()) editor.putString(Variable.ALAMAT_KEY, nAlamat);
                            editor.commit();
                        }

                        Toast.makeText(getContext(), jso.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException je){
                   // je.printStackTrace();
                    Toast.makeText(getContext(), je.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
