package kuispbo_765;

public class LayananServis extends Entitas {
    private int kuota;

    public LayananServis(String id, String nama, int kuota) {
        super(id, nama);
        this.kuota = kuota;
    }

    public int getKuota() {
        return kuota;
    }

    public void kurangiKuota(int jumlah) throws TransaksiException {
        if (this.kuota < jumlah) {
            throw new TransaksiException("Kuota/Stok tidak mencukupi untuk layanan: " + nama);
        }
        this.kuota -= jumlah;
    }
}