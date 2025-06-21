/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import com.github.javafaker.Faker;
import ffos.skroflin.model.SkroflinKutija;
import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.dto.SkroflinKutijaDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        SkroflinKutija sk =  session.get(SkroflinKutija.class, sifra);
        SkroflinPolica skroflinPolica = session.get(SkroflinPolica.class, o.policaSifra());
        sk.setDatumPohrane(o.datumPohrane());
        sk.setObujam(o.obujam());
        sk.setOznakaKutije(o.oznakaKutije());
        sk.setSkroflinPolica(skroflinPolica);
        session.persist(sk);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(SkroflinKutija.class, sifra));
        session.getTransaction().commit();
    }
    
    public void masovnoDodavanje(int broj){
        SkroflinKutija sk;
        Faker f = new Faker();
        BigDecimal obujam = BigDecimal.valueOf(f.number().randomDouble(2, 100, 200));
        int maksPolicaSifra = 2;
        Date d = new Date();
        session.beginTransaction();
        for (int i = 0; i < broj; i++) {
            int sifraPolice = f.number().numberBetween(1, maksPolicaSifra + 1);
            SkroflinPolica sp = session.get(SkroflinPolica.class, sifraPolice);
            sk = new SkroflinKutija(d, obujam, "Oznaka:" + " " + UUID.randomUUID(), sp);
            session.persist(sk);
        }
        session.getTransaction().commit();
    }
}
