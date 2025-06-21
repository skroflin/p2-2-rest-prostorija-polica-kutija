/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import com.github.javafaker.Faker;
import ffos.skroflin.model.SkroflinProstorija;
import ffos.skroflin.model.dto.SkroflinProstorijaDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class SkroflinProstorijaService extends GlavniService{
        public List<SkroflinProstorija> getAll(){
        return session.createQuery("from skroflin_prostorija", SkroflinProstorija.class).list();
    }
    
    public SkroflinProstorija getBySifra(int sifra){
        return session.get(SkroflinProstorija.class, sifra);
    }
    
    public SkroflinProstorija post(SkroflinProstorijaDTO o){
        SkroflinProstorija skroflinProstorija = new SkroflinProstorija(o.kabinet(), o.naziv());
        session.beginTransaction();
        session.persist(skroflinProstorija);
        session.getTransaction().commit();
        return skroflinProstorija;
    }
    
    public void put(SkroflinProstorijaDTO o, int sifra){
        session.beginTransaction();
        SkroflinProstorija sp = (SkroflinProstorija) session.get(SkroflinProstorija.class, sifra);
        sp.setKabinet(o.kabinet());
        sp.setNaziv(o.naziv());
        session.persist(sp);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(SkroflinProstorija.class, sifra));
        session.getTransaction().commit();
    }
    
    public void masovnoDodavanje(int broj){
        SkroflinProstorija sp;
        Faker f = new Faker();
        session.beginTransaction();
        for (int i = 0; i < broj; i++) {
            sp = new SkroflinProstorija(f.random().nextBoolean(), f.name().name());
        }
        session.getTransaction().commit();
    }
}
