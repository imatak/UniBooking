package unipu.hr.unibooking;

public class Rezervacija {
    private String EmailUsera;
    private String UserTekst;
    private String Datum;
    private String Razlog;
    private String Termin;

    public Rezervacija() {
    }

    public String getEmailUsera() {
        return EmailUsera;
    }

    public void setEmailUsera(String emailUsera) {
        EmailUsera = emailUsera;
    }

    public String getUserTekst() {
        return UserTekst;
    }

    public void setUserTekst(String userTekst) {
        UserTekst = userTekst;
    }

    public String getDatum() {
        return Datum;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    public String getRazlog() {
        return Razlog;
    }

    public void setRazlog(String razlog) {
        Razlog = razlog;
    }

    public String getTermin() {
        return Termin;
    }

    public void setTermin(String termin) {
        Termin = termin;
    }
}
