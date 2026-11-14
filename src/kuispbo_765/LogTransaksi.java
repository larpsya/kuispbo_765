package kuispbo_765;

import java.io.Serializable;

public class LogTransaksi implements Serializable {
    private Pelanggan pelanggan;
    private LayananServis layanan;
    private int qty;

    public LogTransaksi(Pelanggan pelanggan, LayananServis layanan, int qty) {
        this.pelanggan = pelanggan;
        this.layanan = layanan;
        this.qty = qty;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public LayananServis getLayanan() {
        return layanan;
    }

    public int getQty() {
        return qty;
    }
}