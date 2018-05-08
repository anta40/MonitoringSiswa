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

public class ListPembayaranAdapter extends ArrayAdapter<String> {
    private final Activity context;
    List<String> IDPEMBAYARAN = new ArrayList<String>();
    List<String> PERIODEPEMBAYARAN = new ArrayList<String>();
    List<String> JMLPEMBAYARAN = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();

    public ListPembayaranAdapter(Activity context,
                                 List<String> IDPEMBAYARAN, List<String> PERIODEPEMBAYARAN, List<String> JMLPEMBAYARAN, List<String> TANGGAL) {
        super(context, R.layout.list_berita, IDPEMBAYARAN);

        this.context = context;
        this.IDPEMBAYARAN = IDPEMBAYARAN;
        this.JMLPEMBAYARAN = JMLPEMBAYARAN;
        this.PERIODEPEMBAYARAN = PERIODEPEMBAYARAN;
        this.TANGGAL = TANGGAL;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_pengumuman, null, true);
        TextView txtJudul = (TextView) rowView.findViewById(R.id.tvJudul);
        TextView txtIsi = (TextView) rowView.findViewById(R.id.tvIsi);
        TextView txtTanggal = (TextView) rowView.findViewById(R.id.tvTanggal);

        txtTanggal.setText(TANGGAL.get(position));
        txtJudul.setText("PERIODE " + PERIODEPEMBAYARAN.get(position));
        txtIsi.setText("Rp. " + JMLPEMBAYARAN.get(position));

        return rowView;
    }


}