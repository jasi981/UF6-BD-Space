/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uf6.bd.space;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import uf6.bd.space.exceptions.NonexistentEntityException;

/**
 *
 * @author Jasmeen Singh <jasi981@vidalibarraquer.net>
 */
public class CaracteristicaJpaController implements Serializable {

    public CaracteristicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caracteristica caracteristica) {
        if (caracteristica.getAlienList() == null) {
            caracteristica.setAlienList(new ArrayList<Alien>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Alien> attachedAlienList = new ArrayList<Alien>();
            for (Alien alienListAlienToAttach : caracteristica.getAlienList()) {
                alienListAlienToAttach = em.getReference(alienListAlienToAttach.getClass(), alienListAlienToAttach.getId());
                attachedAlienList.add(alienListAlienToAttach);
            }
            caracteristica.setAlienList(attachedAlienList);
            em.persist(caracteristica);
            for (Alien alienListAlien : caracteristica.getAlienList()) {
                alienListAlien.getCaracteristicaList().add(caracteristica);
                alienListAlien = em.merge(alienListAlien);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caracteristica caracteristica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caracteristica persistentCaracteristica = em.find(Caracteristica.class, caracteristica.getId());
            List<Alien> alienListOld = persistentCaracteristica.getAlienList();
            List<Alien> alienListNew = caracteristica.getAlienList();
            List<Alien> attachedAlienListNew = new ArrayList<Alien>();
            for (Alien alienListNewAlienToAttach : alienListNew) {
                alienListNewAlienToAttach = em.getReference(alienListNewAlienToAttach.getClass(), alienListNewAlienToAttach.getId());
                attachedAlienListNew.add(alienListNewAlienToAttach);
            }
            alienListNew = attachedAlienListNew;
            caracteristica.setAlienList(alienListNew);
            caracteristica = em.merge(caracteristica);
            for (Alien alienListOldAlien : alienListOld) {
                if (!alienListNew.contains(alienListOldAlien)) {
                    alienListOldAlien.getCaracteristicaList().remove(caracteristica);
                    alienListOldAlien = em.merge(alienListOldAlien);
                }
            }
            for (Alien alienListNewAlien : alienListNew) {
                if (!alienListOld.contains(alienListNewAlien)) {
                    alienListNewAlien.getCaracteristicaList().add(caracteristica);
                    alienListNewAlien = em.merge(alienListNewAlien);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = caracteristica.getId();
                if (findCaracteristica(id) == null) {
                    throw new NonexistentEntityException("The caracteristica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caracteristica caracteristica;
            try {
                caracteristica = em.getReference(Caracteristica.class, id);
                caracteristica.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caracteristica with id " + id + " no longer exists.", enfe);
            }
            List<Alien> alienList = caracteristica.getAlienList();
            for (Alien alienListAlien : alienList) {
                alienListAlien.getCaracteristicaList().remove(caracteristica);
                alienListAlien = em.merge(alienListAlien);
            }
            em.remove(caracteristica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caracteristica> findCaracteristicaEntities() {
        return findCaracteristicaEntities(true, -1, -1);
    }

    public List<Caracteristica> findCaracteristicaEntities(int maxResults, int firstResult) {
        return findCaracteristicaEntities(false, maxResults, firstResult);
    }

    private List<Caracteristica> findCaracteristicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caracteristica.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Caracteristica findCaracteristica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Caracteristica.class, id);
        } finally {
            em.close();
        }
    }

    public int getCaracteristicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caracteristica> rt = cq.from(Caracteristica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
