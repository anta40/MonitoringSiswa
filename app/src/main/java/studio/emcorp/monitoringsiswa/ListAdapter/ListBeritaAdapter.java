package studio.emcorp.monitoringsiswa.ListAdapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studio.emcorp.monitoringsiswa.R;

public class ListBeritaAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> IDBERITA = new ArrayList<String>();
    List<String> JUDULBERITA = new ArrayList<String>();
    List<String> ISI = new ArrayList<String>();
    List<String> GAMBAR = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();

    public ListBeritaAdapter(Activity context,
                             List<String> IDBERITA, List<String> JUDULBERITA, List<String> ISI, List<String> GAMBAR, List<String> TANGGAL) {
        super(context, R.layout.list_berita, IDBERITA);

        this.context = context;
        this.IDBERITA = IDBERITA;
        this.ISI = ISI;
        this.JUDULBERITA = JUDULBERITA;
        this.GAMBAR = GAMBAR;
        this.TANGGAL = TANGGAL;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_berita, null, true);
        TextView txtJudul = (TextView) rowView.findViewById(R.id.tvJudul);
        TextView txtIsi = (TextView) rowView.findViewById(R.id.tvIsi);
        TextView txtTanggal = (TextView) rowView.findViewById(R.id.tvTanggal);

        txtTanggal.setText(TANGGAL.get(position));
        txtJudul.setText(JUDULBERITA.get(position));
        if(ISI.get(position).length()>=100){
            txtIsi.setText(ISI.get(position).substring(0,100)+" ...");
        }else{
            txtIsi.setText(ISI.get(position)+" ...");
        }

        return rowView;
    }


}