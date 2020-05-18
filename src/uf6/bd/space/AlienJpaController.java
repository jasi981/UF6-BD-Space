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
public class AlienJpaController implements Serializable {

    public AlienJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alien alien) {
        if (alien.getCaracteristicaList() == null) {
            alien.setCaracteristicaList(new ArrayList<Caracteristica>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nau nau = alien.getNau();
            if (nau != null) {
                nau = em.getReference(nau.getClass(), nau.getId());
                alien.setNau(nau);
            }
            Planeta planeta = alien.getPlaneta();
            if (planeta != null) {
                planeta = em.getReference(planeta.getClass(), planeta.getId());
                alien.setPlaneta(planeta);
            }
            List<Caracteristica> attachedCaracteristicaList = new ArrayList<Caracteristica>();
            for (Caracteristica caracteristicaListCaracteristicaToAttach : alien.getCaracteristicaList()) {
                caracteristicaListCaracteristicaToAttach = em.getReference(caracteristicaListCaracteristicaToAttach.getClass(), caracteristicaListCaracteristicaToAttach.getId());
                attachedCaracteristicaList.add(caracteristicaListCaracteristicaToAttach);
            }
            alien.setCaracteristicaList(attachedCaracteristicaList);
            em.persist(alien);
            if (nau != null) {
                nau.getAlienList().add(alien);
                nau = em.merge(nau);
            }
            if (planeta != null) {
                planeta.getAlienList().add(alien);
                planeta = em.merge(planeta);
            }
            for (Caracteristica caracteristicaListCaracteristica : alien.getCaracteristicaList()) {
                caracteristicaListCaracteristica.getAlienList().add(alien);
                caracteristicaListCaracteristica = em.merge(caracteristicaListCaracteristica);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alien alien) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alien persistentAlien = em.find(Alien.class, alien.getId());
            Nau nauOld = persistentAlien.getNau();
            Nau nauNew = alien.getNau();
            Planeta planetaOld = persistentAlien.getPlaneta();
            Planeta planetaNew = alien.getPlaneta();
            List<Caracteristica> caracteristicaListOld = persistentAlien.getCaracteristicaList();
            List<Caracteristica> caracteristicaListNew = alien.getCaracteristicaList();
            if (nauNew != null) {
                nauNew = em.getReference(nauNew.getClass(), nauNew.getId());
                alien.setNau(nauNew);
            }
            if (planetaNew != null) {
                planetaNew = em.getReference(planetaNew.getClass(), planetaNew.getId());
                alien.setPlaneta(planetaNew);
            }
            List<Caracteristica> attachedCaracteristicaListNew = new ArrayList<Caracteristica>();
            for (Caracteristica caracteristicaListNewCaracteristicaToAttach : caracteristicaListNew) {
                caracteristicaListNewCaracteristicaToAttach = em.getReference(caracteristicaListNewCaracteristicaToAttach.getClass(), caracteristicaListNewCaracteristicaToAttach.getId());
                attachedCaracteristicaListNew.add(caracteristicaListNewCaracteristicaToAttach);
            }
            caracteristicaListNew = attachedCaracteristicaListNew;
            alien.setCaracteristicaList(caracteristicaListNew);
            alien = em.merge(alien);
            if (nauOld != null && !nauOld.equals(nauNew)) {
                nauOld.getAlienList().remove(alien);
                nauOld = em.merge(nauOld);
            }
            if (nauNew != null && !nauNew.equals(nauOld)) {
                nauNew.getAlienList().add(alien);
                nauNew = em.merge(nauNew);
            }
            if (planetaOld != null && !planetaOld.equals(planetaNew)) {
                planetaOld.getAlienList().remove(alien);
                planetaOld = em.merge(planetaOld);
            }
            if (planetaNew != null && !planetaNew.equals(planetaOld)) {
                planetaNew.getAlienList().add(alien);
                planetaNew = em.merge(planetaNew);
            }
            for (Caracteristica caracteristicaListOldCaracteristica : caracteristicaListOld) {
                if (!caracteristicaListNew.contains(caracteristicaListOldCaracteristica)) {
                    caracteristicaListOldCaracteristica.getAlienList().remove(alien);
                    caracteristicaListOldCaracteristica = em.merge(caracteristicaListOldCaracteristica);
                }
            }
            for (Caracteristica caracteristicaListNewCaracteristica : caracteristicaListNew) {
                if (!caracteristicaListOld.contains(caracteristicaListNewCaracteristica)) {
                    caracteristicaListNewCaracteristica.getAlienList().add(alien);
                    caracteristicaListNewCaracteristica = em.merge(caracteristicaListNewCaracteristica);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alien.getId();
                if (findAlien(id) == null) {
                    throw new NonexistentEntityException("The alien with id " + id + " no longer exists.");
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
            Alien alien;
            try {
                alien = em.getReference(Alien.class, id);
                alien.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alien with id " + id + " no longer exists.", enfe);
            }
            Nau nau = alien.getNau();
            if (nau != null) {
                nau.getAlienList().remove(alien);
                nau = em.merge(nau);
            }
            Planeta planeta = alien.getPlaneta();
            if (planeta != null) {
                planeta.getAlienList().remove(alien);
                planeta = em.merge(planeta);
            }
            List<Caracteristica> caracteristicaList = alien.getCaracteristicaList();
            for (Caracteristica caracteristicaListCaracteristica : caracteristicaList) {
                caracteristicaListCaracteristica.getAlienList().remove(alien);
                caracteristicaListCaracteristica = em.merge(caracteristicaListCaracteristica);
            }
            em.remove(alien);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alien> findAlienEntities() {
        return findAlienEntities(true, -1, -1);
    }

    public List<Alien> findAlienEntities(int maxResults, int firstResult) {
        return findAlienEntities(false, maxResults, firstResult);
    }

    private List<Alien> findAlienEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alien.class));
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

    public Alien findAlien(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alien.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlienCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alien> rt = cq.from(Alien.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
