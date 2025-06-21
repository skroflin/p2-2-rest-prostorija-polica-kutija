/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import com.github.javafaker.Faker;
import ffos.skroflin.model.SkroflinPolica;
import ffos.skroflin.model.SkroflinProstorija;
import ffos.skroflin.model.dto.SkroflinPolicaDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
 @Service
public class SkroflinPolicaService extends GlavniService{
    public List<SkroflinPolica> getAll(){
        return session.createQuery("from skroflin_polica", SkroflinPolica.class).list();
    }
    
    public SkroflinPolica getBySifra(int sifra){
        return session.get(SkroflinPolica.class, sifra);
    }
    
    public SkroflinPolica post(SkroflinPolicaDTO o){
        session.beginTransaction();
        SkroflinProstorija skroflinProstorija = session.get(SkroflinProstorija.class, o.prostorijaSifra());
        SkroflinPolica skroflinPolica = new SkroflinPolica(o.duzina(), o.sirina(), o.visina(), skroflinProstorija);
        session.persist(skroflinPolica);
        session.getTransaction().commit();
        return skroflinPolica;
    }
    
    public void put(SkroflinPolicaDTO o, int sifra){
        session.beginTransaction();
        SkroflinPolica sp = session.get(SkroflinPolica.class, sifra);
        SkroflinProstorija skroflinProstorija = session.get(SkroflinProstorija.class, o.prostorijaSifra());
        sp.setDuzina(o.duzina());
        sp.setSirina(o.sirina());
        sp.setVisina(o.visina());
        sp.setSkroflinProstorija(skroflinProstorija);
        session.persist(sp);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(SkroflinPolica.class, sifra));
        session.getTransaction().commit();
    }
    
    public void masovnoDodavanje(int broj){
        SkroflinPolica sp;
        Faker f = new Faker();
        int maksProstorijaSifra = 3;
        session.beginTransaction();
        for (int i = 0; i < broj; i++) {
            int prostorijaSifra = f.number().numberBetween(1, maksProstorijaSifra + 1);
            SkroflinProstorija spr = session.get(SkroflinProstorija.class, prostorijaSifra);
            sp = new SkroflinPolica(f.number().randomDigit(), f.number().randomDigit(), f.number().randomDigit(), spr);
            session.persist(sp);
        }
        session.getTransaction().commit();
    }
    
    public int getUkupnaSirinaPolice(){
        Long ukupno = session.createQuery(
                "select sum(sirina) from skroflin_polica", Long.class)
                .getSingleResult();
        return ukupno != null ? ukupno.intValue() : 0;
    }
    
    public List<SkroflinPolica> getPoliceUProstoriji(SkroflinProstorija prostorija){
        return session.createQuery(
                "from skroflin_polica sp join sp.skroflinProstorija s where s = :prostorija", SkroflinPolica.class)
                .setParameter("prostorija", prostorija)
                .list();
    }
}
