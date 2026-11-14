package kuispbo_765;
import java.io.Serializable;

public abstract class Entitas implements Serializable {
    protected String id;
    protected String nama;

    public Entitas(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}