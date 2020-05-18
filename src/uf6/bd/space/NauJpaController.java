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
public class NauJpaController implements Serializable {

    public NauJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nau nau) {
        if (nau.getAlienList() == null) {
            nau.setAlienList(new ArrayList<Alien>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Alien> attachedAlienList = new ArrayList<Alien>();
            for (Alien alienListAlienToAttach : nau.getAlienList()) {
                alienListAlienToAttach = em.getReference(alienListAlienToAttach.getClass(), alienListAlienToAttach.getId());
                attachedAlienList.add(alienListAlienToAttach);
            }
            nau.setAlienList(attachedAlienList);
            em.persist(nau);
            for (Alien alienListAlien : nau.getAlienList()) {
                Nau oldNauOfAlienListAlien = alienListAlien.getNau();
                alienListAlien.setNau(nau);
                alienListAlien = em.merge(alienListAlien);
                if (oldNauOfAlienListAlien != null) {
                    oldNauOfAlienListAlien.getAlienList().remove(alienListAlien);
                    oldNauOfAlienListAlien = em.merge(oldNauOfAlienListAlien);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nau nau) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nau persistentNau = em.find(Nau.class, nau.getId());
            List<Alien> alienListOld = persistentNau.getAlienList();
            List<Alien> alienListNew = nau.getAlienList();
            List<Alien> attachedAlienListNew = new ArrayList<Alien>();
            for (Alien alienListNewAlienToAttach : alienListNew) {
                alienListNewAlienToAttach = em.getReference(alienListNewAlienToAttach.getClass(), alienListNewAlienToAttach.getId());
                attachedAlienListNew.add(alienListNewAlienToAttach);
            }
            alienListNew = attachedAlienListNew;
            nau.setAlienList(alienListNew);
            nau = em.merge(nau);
            for (Alien alienListOldAlien : alienListOld) {
                if (!alienListNew.contains(alienListOldAlien)) {
                    alienListOldAlien.setNau(null);
                    alienListOldAlien = em.merge(alienListOldAlien);
                }
            }
            for (Alien alienListNewAlien : alienListNew) {
                if (!alienListOld.contains(alienListNewAlien)) {
                    Nau oldNauOfAlienListNewAlien = alienListNewAlien.getNau();
                    alienListNewAlien.setNau(nau);
                    alienListNewAlien = em.merge(alienListNewAlien);
                    if (oldNauOfAlienListNewAlien != null && !oldNauOfAlienListNewAlien.equals(nau)) {
                        oldNauOfAlienListNewAlien.getAlienList().remove(alienListNewAlien);
                        oldNauOfAlienListNewAlien = em.merge(oldNauOfAlienListNewAlien);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nau.getId();
                if (findNau(id) == null) {
                    throw new NonexistentEntityException("The nau with id " + id + " no longer exists.");
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
            Nau nau;
            try {
                nau = em.getReference(Nau.class, id);
                nau.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nau with id " + id + " no longer exists.", enfe);
            }
            List<Alien> alienList = nau.getAlienList();
            for (Alien alienListAlien : alienList) {
                alienListAlien.setNau(null);
                alienListAlien = em.merge(alienListAlien);
            }
            em.remove(nau);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nau> findNauEntities() {
        return findNauEntities(true, -1, -1);
    }

    public List<Nau> findNauEntities(int maxResults, int firstResult) {
        return findNauEntities(false, maxResults, firstResult);
    }

    private List<Nau> findNauEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nau.class));
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

    public Nau findNau(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nau.class, id);
        } finally {
            em.close();
        }
    }

    public int getNauCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nau> rt = cq.from(Nau.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
