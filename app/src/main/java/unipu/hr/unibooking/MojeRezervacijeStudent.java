package unipu.hr.unibooking;

import java.io.Serializable;

public class MojeRezervacijeStudent implements Serializable {
    private String Datum;
    private String Vrijeme;
    private String Status;
    private String Razlog;
    private String ID;
    private String Email;

    public MojeRezervacijeStudent(String datum, String vrijeme, String status, String razlog, String ID, String email) {
        this.Datum = datum;
        this.Vrijeme = vrijeme;
        this.Status = status;
        this.Razlog = razlog;
        this.ID = ID;
        this.Email= email;
    }

    public String getEmail() { return Email; }

    public void setEmail(String email) { Email = email; }

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
        this.Razlog = razlog;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
