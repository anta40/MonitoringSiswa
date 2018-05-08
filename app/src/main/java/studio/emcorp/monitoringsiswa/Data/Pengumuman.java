package studio.emcorp.monitoringsiswa.Data;

/**
 * Created by anta40 on 14-Jun-17.
 */

public class Pengumuman {

    private String judul;
    private String isi;
    private String tanggal;
    private String penulis;

    public Pengumuman(String judul, String isi, String penulis, String tanggal){
        this.judul = judul;
        this.isi = isi;
        this.penulis = penulis;
        this.tanggal = tanggal;
    }

    public Pengumuman(){
        this("","","","");
    }

    public String getJudul(){
        return judul;
    }

    public String getIsi(){
        return isi;
    }

    public String getTanggal(){
        return tanggal;
    }

    public String getPenulis(){
        return penulis;
    }
}
