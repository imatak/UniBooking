package unipu.hr.unibooking;

import java.io.Serializable;

public class MojeRezervacijeStudent implements Serializable {
    private String Datum;
    private String Vrijeme;
    private String Status;
    private String Razlog;

    public MojeRezervacijeStudent(String datum, String vrijeme, String status, String razlog) {
        this.Datum = datum;
        this.Vrijeme = vrijeme;
        this.Status = status;
        Razlog = razlog;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        this.Datum = datum;
    }

    public String getVrijeme() {
        return Vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.Vrijeme = vrijeme;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getRazlog() {
        return Razlog;
    }

    public void setRazlog(String razlog) {
        Razlog = razlog;
    }
}
