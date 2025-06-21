/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

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
        SkroflinPolica sp = (SkroflinPolica) session.get(SkroflinPolica.class, sifra);
        SkroflinProstorija skroflinProstorija = session.get(SkroflinProstorija.class, o.prostorijaSifra());
        sp = new SkroflinPolica(o.duzina(), o.sirina(), o.visina(), skroflinProstorija);
        session.persist(sp);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(SkroflinPolica.class, sifra));
        session.getTransaction().commit();
    }
}
