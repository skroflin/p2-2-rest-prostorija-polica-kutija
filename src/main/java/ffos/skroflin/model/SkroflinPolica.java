/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author svenk
 */
@Entity(name = "skroflin_polica")
public class SkroflinPolica extends Entitet{
    @Column(nullable = false)
    private int duzina;
    @Column(nullable = false)
    private int sirina;
    @Column(nullable = false)
    private int visina;
    @ManyToOne
    private SkroflinProstorija skroflinProstorija;

    public SkroflinPolica() {
    }

    public SkroflinPolica(int duzina, int sirina, int visina, SkroflinProstorija skroflinProstorija) {
        this.duzina = duzina;
        this.sirina = sirina;
        this.visina = visina;
        this.skroflinProstorija = skroflinProstorija;
    }

    public int getDuzina() {
        return duzina;
    }

    public void setDuzina(int duzina) {
        this.duzina = duzina;
    }

    public int getSirina() {
        return sirina;
    }

    public void setSirina(int sirina) {
        this.sirina = sirina;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
    }

    public SkroflinProstorija getSkroflinProstorija() {
        return skroflinProstorija;
    }

    public void setSkroflinProstorija(SkroflinProstorija skroflinProstorija) {
        this.skroflinProstorija = skroflinProstorija;
    }
}
