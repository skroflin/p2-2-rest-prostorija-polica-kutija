/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author svenk
 */
@Entity(name = "skroflin_kutija")
public class SkroflinKutija extends Entitet{
    private Date datumPohrane;
    @Column(columnDefinition = "float")
    private BigDecimal obujam;
    private String oznakaKutije;
    @ManyToOne
    private SkroflinPolica skroflinPolica;

    public SkroflinKutija() {
    }

    public SkroflinKutija(Date datumPohrane, BigDecimal obujam, String oznakaKutije, SkroflinPolica skroflinPolica) {
        this.datumPohrane = datumPohrane;
        this.obujam = obujam;
        this.oznakaKutije = oznakaKutije;
        this.skroflinPolica = skroflinPolica;
    }

    public Date getDatumPohrane() {
        return datumPohrane;
    }

    public void setDatumPohrane(Date datumPohrane) {
        this.datumPohrane = datumPohrane;
    }

    public BigDecimal getObujam() {
        return obujam;
    }

    public void setObujam(BigDecimal obujam) {
        this.obujam = obujam;
    }

    public String getOznakaKutije() {
        return oznakaKutije;
    }

    public void setOznakaKutije(String oznakaKutije) {
        this.oznakaKutije = oznakaKutije;
    }

    public SkroflinPolica getSkroflinPolica() {
        return skroflinPolica;
    }

    public void setSkroflinPolica(SkroflinPolica skroflinPolica) {
        this.skroflinPolica = skroflinPolica;
    }
}
