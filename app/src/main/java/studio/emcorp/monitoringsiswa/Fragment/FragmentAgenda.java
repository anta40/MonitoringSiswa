package studio.emcorp.monitoringsiswa.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import studio.emcorp.monitoringsiswa.Library.JSONParser;
import studio.emcorp.monitoringsiswa.Library.Variable;
import studio.emcorp.monitoringsiswa.ListAdapter.ListAgendaAdapter;
import studio.emcorp.monitoringsiswa.R;

public class FragmentAgenda extends Fragment {
    public static FragmentAgenda newInstance(String param1, String param2) {
        FragmentAgenda fragment = new FragmentAgenda();
        return fragment;
    }
    ProgressBar pB;
    ListView list;
    List<String> IDAGENDA = new ArrayList<String>();
    List<String> TEMAAGENDA = new ArrayList<String>();
    List<String> ISI = new ArrayList<String>();
    List<String> TANGGALMULAI = new ArrayList<String>();
    List<String> TANGGALSELESAI = new ArrayList<String>();
    List<String> TANGGALPOSTING = new ArrayList<String>();
    List<String> TEMPAT = new ArrayList<String>();
    List<String> JAM = new ArrayList<String>();
    List<String> KETERANGAN = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String idKelas = "",idSsiwa = "",nama = "", nis="", namaKelas="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        pB = (ProgressBar)view.findViewById(R.id.pB);
        list = (ListView)view.findViewById(R.id.listView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Agenda");
        sharedpreferences = getActivity().getSharedPreferences(Variable.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        idKelas = sharedpreferences.getString(Variable.IDKELAS_KEY, null);
        idSsiwa = sharedpreferences.getString(Variable.IDSISWA_KEY, null);
        nama = sharedpreferences.getString(Variable.NAMA_KEY, null);
        nis = sharedpreferences.getString(Variable.NIS_KEY, null);
        namaKelas = sharedpreferences.getString(Variable.NAMAKELAS_KEY, null);

        if(isNetworkConnected()){
            cekAgenda();
        }else {
            Toast.makeText(getContext(),"Internet tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void cekAgenda(){
        pB.setVisibility(View.VISIBLE);
        new CekAsync().execute(
                Variable.API_KEY,
                Variable.FUNGSI_AGENDA
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
//                Toast.makeText(getContext(),json.toString(),Toast.LENGTH_SHORT).show();
                try {
                    IDAGENDA.clear();
                    TEMAAGENDA.clear();
                    ISI.clear();
                    TANGGALMULAI.clear();
                    TANGGALSELESAI.clear();
                    TANGGALPOSTING.clear();
                    TEMPAT.clear();
                    JAM.clear();
                    KETERANGAN.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if(jsonArray.length()==0){
                        Toast.makeText(getContext(),"Data tidak ada", Toast.LENGTH_SHORT).show();
                        list.setAdapter(null);
                    }else{
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String id_agenda = isiArray.getString("id_agenda");
                            String tema_agenda = isiArray.getString("tema_agenda");
                            String isi = isiArray.getString("isi");
                            String tgl_mulai = isiArray.getString("tgl_mulai");
                            String tgl_selesai = isiArray.getString("tgl_selesai");
                            String tgl_posting = isiArray.getString("tgl_posting");
                            String tempat = isiArray.getString("tempat");
                            String jam = isiArray.getString("jam");
                            String keterangan = isiArray.getString("keterangan");
                            IDAGENDA.add(id_agenda);
                            TEMAAGENDA.add(tema_agenda);
                            ISI.add(isi);
                            TANGGALMULAI.add(tgl_mulai);
                            TANGGALSELESAI.add(tgl_selesai);
                            TANGGALPOSTING.add(tgl_posting);
                            TEMPAT.add(tempat);
                            JAM.add(jam);
                            KETERANGAN.add(keterangan);
                        }
                        getAllData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            pB.setVisibility(View.INVISIBLE);
        }

    };

    public void getAllData(){
        list.setAdapter(null);
        ListAgendaAdapter adapter = new ListAgendaAdapter(getActivity(), IDAGENDA,TEMAAGENDA,ISI,TEMPAT,TANGGALPOSTING);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getActivity(), AgendaActivity.class);
//                i.putExtra("judul",TEMAAGENDA.get(position));
//                i.putExtra("isi",ISI.get(position));
//                i.putExtra("tgl_mulai",TANGGALMULAI.get(position));
//                i.putExtra("tgl_selesai",TANGGALSELESAI.get(position));
//                i.putExtra("tgl_posting",TANGGALPOSTING.get(position));
//                i.putExtra("tempat",TEMPAT.get(position));
//                i.putExtra("jam",JAM.get(position));
//                i.putExtra("keterangan",KETERANGAN.get(position));
//                i.putExtra("menu","Agenda");
//                startActivity(i);
//                getActivity().finish();
                showDetail(position);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });
    }

    public void showDetail(int pos){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.activity_agenda);
        dialog.setTitle(null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        String judul = "",isi = "",tgl_mulai = "",tgl_selesai = "",tgl_posting = "",tempat = "",jam = "",keterangan = "",menu = "";
        TextView tvJudul,tvIsi,tvTglMulai,tvTglSelesai,tvTglPosting,tvTglTempat,tvTglJam,tvTglKeterangan;

        tvJudul = (TextView)dialog.findViewById(R.id.tvJudul);
        tvIsi = (TextView)dialog.findViewById(R.id.tvIsi);
        tvTglMulai = (TextView)dialog.findViewById(R.id.tvTglMulai);
        tvTglSelesai = (TextView)dialog.findViewById(R.id.tvTglSelesai);
        tvTglPosting = (TextView)dialog.findViewById(R.id.tvTglPosting);
        tvTglTempat = (TextView)dialog.findViewById(R.id.tvTempat);
        tvTglJam = (TextView)dialog.findViewById(R.id.tvJam);
        tvTglKeterangan = (TextView)dialog.findViewById(R.id.tvKeterangan);

        judul = TEMAAGENDA.get(pos);
        isi = ISI.get(pos);
        tgl_mulai = TANGGALMULAI.get(pos);
        tgl_selesai = TANGGALSELESAI.get(pos);
        tgl_posting = TANGGALPOSTING.get(pos);
        tempat = TEMPAT.get(pos);
        jam = JAM.get(pos);
        keterangan = KETERANGAN.get(pos);

        tvJudul.setText(judul);
        tvIsi.setText(isi);
        tvTglMulai.setText(tgl_mulai);
        tvTglSelesai.setText(tgl_selesai);
        tvTglPosting.setText(tgl_posting);
        tvTglTempat.setText(tempat);
        tvTglJam.setText(jam);
        tvTglKeterangan.setText(keterangan);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
