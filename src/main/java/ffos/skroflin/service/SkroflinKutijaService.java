/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.SkroflinKutija;
import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.dto.SkroflinKutijaDTO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class SkroflinKutijaService extends GlavniService{
    public List<SkroflinKutija> getAll(){
        return session.createQuery("from skroflin_kutija", SkroflinKutija.class).list();
    }
    
    public SkroflinKutija getBySifra(int sifra){
        return session.get(SkroflinKutija.class, sifra);
    }
    
    public SkroflinKutija post(SkroflinKutijaDTO o){
        session.beginTransaction();
        SkroflinKutija sk;
        SkroflinPolica skroflinPolica = session.get(SkroflinPolica.class, o.policaSifra());
        sk = new SkroflinKutija(o.datumPohrane(), o.obujam(), o.oznakaKutije(), skroflinPolica);
        session.persist(sk);
        session.getTransaction().commit();
        return sk;
    }
    
    public void put(SkroflinKutijaDTO o, int sifra){
        session.beginTransaction();
        SkroflinKutija sk = (SkroflinKutija) session.get(SkroflinKutija.class, sifra);
        SkroflinPolica skroflinPolica = session.get(SkroflinPolica.class, o.policaSifra());
        sk = new SkroflinKutija(o.datumPohrane(), o.obujam(), o.oznakaKutije(), skroflinPolica);
        session.persist(sk);
        session.getTransaction().commit();
    }
}
