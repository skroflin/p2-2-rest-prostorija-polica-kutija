/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 *
 * @author svenk
 */
@Entity(name = "skroflin_prostorija")
public class SkroflinProstorija extends Entitet{
    @Column(columnDefinition = "bit")
    private boolean kabinet;
    private String naziv;

    public SkroflinProstorija() {
    }

    public SkroflinProstorija(boolean kabinet, String naziv) {
        this.kabinet = kabinet;
        this.naziv = naziv;
    }

    public boolean isKabinet() {
        return kabinet;
    }

    public void setKabinet(boolean kabinet) {
        this.kabinet = kabinet;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
    
}
