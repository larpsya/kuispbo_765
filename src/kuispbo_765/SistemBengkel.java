package kuispbo_765;
import java.io.*;
import java.util.*;

public class SistemBengkel {
    private String namaSistem;
    private HashMap<String, Pelanggan> mapPelanggan;
    private HashMap<String, LayananServis> mapLayanan;
    private ArrayList<LogTransaksi> daftarTransaksi;

    public SistemBengkel(String namaSistem) {
        this.namaSistem = namaSistem;
        this.mapPelanggan = new HashMap<>();
        this.mapLayanan = new HashMap<>();
        this.daftarTransaksi = new ArrayList<>();
    }

    public String getNamaSistem() {
        return namaSistem;
    }

    public void tambahPelanggan(String id, String nama) {
        mapPelanggan.put(id, new Pelanggan(id, nama));
    }

    public void tambahLayananServis(String id, String nama, int kuota) {
        mapLayanan.put(id, new LayananServis(id, nama, kuota));
    }

    public void jalankanTransaksiDariFile(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + filename + " tidak ditemukan.");
        }

        Scanner scanner = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine().trim());
        }
        scanner.close();

        String isiFile = sb.toString();
        if (isiFile.isEmpty()) return;

        String[] arrayTransaksi = isiFile.split("\\|");

        for (String data : arrayTransaksi) {
            if (data.trim().isEmpty()) continue;

            String[] atribut = data.split("\\.");
            String idPelanggan = null;
            String idLayanan = null;
            int jml = 0;

            for (String attr : atribut) {
                if (attr.startsWith("ID>")) {
                    idPelanggan = attr.substring(3);
                } else if (attr.startsWith("KD>")) {
                    idLayanan = attr.substring(3);
                } else if (attr.startsWith("JML>")) {
                    jml = Integer.parseInt(attr.substring(4));
                }
            }

            if (!mapPelanggan.containsKey(idPelanggan)) {
                throw new TransaksiException("Pelanggan " + idPelanggan + " tidak terdaftar di sistem.");
            }
            if (!mapLayanan.containsKey(idLayanan)) {
                throw new TransaksiException("Layanan Servis " + idLayanan + " tidak tersedia.");
            }

            Pelanggan p = mapPelanggan.get(idPelanggan);
            LayananServis l = mapLayanan.get(idLayanan);

            l.kurangiKuota(jml);
            daftarTransaksi.add(new LogTransaksi(p, l, jml));
        }
    }

    public void simpanLaporanAkhir() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_master.bak"))) {
            oos.writeObject(mapPelanggan);
            oos.writeObject(mapLayanan);
            oos.writeObject(daftarTransaksi);
            System.out.println("[INFO] Status memori aplikasi telah diserialisasi ke data_master.bak");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal melakukan serialisasi: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter("laporan.txt"))) {
            for (LogTransaksi log : daftarTransaksi) {
                pw.printf("%s,%s,%d\n", log.getPelanggan().getId(), log.getLayanan().getId(), log.getQty());
            }
            System.out.println("[INFO] Laporan berhasil diekspor berformat CSV ke laporan.txt");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan teks: " + e.getMessage());
        }
    }
}