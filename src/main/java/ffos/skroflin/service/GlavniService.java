/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.HibernateUtil;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.Session;

/**
 *
 * @author svenk
 */
@MappedSuperclass
public abstract class GlavniService {
    protected Session session;

    public GlavniService() {
        session = HibernateUtil.getSession();
    }
    
}
