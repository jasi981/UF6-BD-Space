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
public class PlanetaJpaController implements Serializable {

    public PlanetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Planeta planeta) {
        if (planeta.getAlienList() == null) {
            planeta.setAlienList(new ArrayList<Alien>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Alien> attachedAlienList = new ArrayList<Alien>();
            for (Alien alienListAlienToAttach : planeta.getAlienList()) {
                alienListAlienToAttach = em.getReference(alienListAlienToAttach.getClass(), alienListAlienToAttach.getId());
                attachedAlienList.add(alienListAlienToAttach);
            }
            planeta.setAlienList(attachedAlienList);
            em.persist(planeta);
            for (Alien alienListAlien : planeta.getAlienList()) {
                Planeta oldPlanetaOfAlienListAlien = alienListAlien.getPlaneta();
                alienListAlien.setPlaneta(planeta);
                alienListAlien = em.merge(alienListAlien);
                if (oldPlanetaOfAlienListAlien != null) {
                    oldPlanetaOfAlienListAlien.getAlienList().remove(alienListAlien);
                    oldPlanetaOfAlienListAlien = em.merge(oldPlanetaOfAlienListAlien);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Planeta planeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Planeta persistentPlaneta = em.find(Planeta.class, planeta.getId());
            List<Alien> alienListOld = persistentPlaneta.getAlienList();
            List<Alien> alienListNew = planeta.getAlienList();
            List<Alien> attachedAlienListNew = new ArrayList<Alien>();
            for (Alien alienListNewAlienToAttach : alienListNew) {
                alienListNewAlienToAttach = em.getReference(alienListNewAlienToAttach.getClass(), alienListNewAlienToAttach.getId());
                attachedAlienListNew.add(alienListNewAlienToAttach);
            }
            alienListNew = attachedAlienListNew;
            planeta.setAlienList(alienListNew);
            planeta = em.merge(planeta);
            for (Alien alienListOldAlien : alienListOld) {
                if (!alienListNew.contains(alienListOldAlien)) {
                    alienListOldAlien.setPlaneta(null);
                    alienListOldAlien = em.merge(alienListOldAlien);
                }
            }
            for (Alien alienListNewAlien : alienListNew) {
                if (!alienListOld.contains(alienListNewAlien)) {
                    Planeta oldPlanetaOfAlienListNewAlien = alienListNewAlien.getPlaneta();
                    alienListNewAlien.setPlaneta(planeta);
                    alienListNewAlien = em.merge(alienListNewAlien);
                    if (oldPlanetaOfAlienListNewAlien != null && !oldPlanetaOfAlienListNewAlien.equals(planeta)) {
                        oldPlanetaOfAlienListNewAlien.getAlienList().remove(alienListNewAlien);
                        oldPlanetaOfAlienListNewAlien = em.merge(oldPlanetaOfAlienListNewAlien);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = planeta.getId();
                if (findPlaneta(id) == null) {
                    throw new NonexistentEntityException("The planeta with id " + id + " no longer exists.");
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
            Planeta planeta;
            try {
                planeta = em.getReference(Planeta.class, id);
                planeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The planeta with id " + id + " no longer exists.", enfe);
            }
            List<Alien> alienList = planeta.getAlienList();
            for (Alien alienListAlien : alienList) {
                alienListAlien.setPlaneta(null);
                alienListAlien = em.merge(alienListAlien);
            }
            em.remove(planeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Planeta> findPlanetaEntities() {
        return findPlanetaEntities(true, -1, -1);
    }

    public List<Planeta> findPlanetaEntities(int maxResults, int firstResult) {
        return findPlanetaEntities(false, maxResults, firstResult);
    }

    private List<Planeta> findPlanetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Planeta.class));
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

    public Planeta findPlaneta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Planeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Planeta> rt = cq.from(Planeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
