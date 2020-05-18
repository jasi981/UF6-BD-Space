/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uf6.bd.space;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jasmeen Singh <jasi981@vidalibarraquer.net>
 */
@Entity
@Table(name = "PLANETA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planeta.findAll", query = "SELECT p FROM Planeta p")
    , @NamedQuery(name = "Planeta.findById", query = "SELECT p FROM Planeta p WHERE p.id = :id")
    , @NamedQuery(name = "Planeta.findByNom", query = "SELECT p FROM Planeta p WHERE p.nom = :nom")
    , @NamedQuery(name = "Planeta.findByNomhabitants", query = "SELECT p FROM Planeta p WHERE p.nomhabitants = :nomhabitants")})
public class Planeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOM")
    private String nom;
    @Column(name = "NOMHABITANTS")
    private String nomhabitants;
    @OneToMany(mappedBy = "planeta")
    private List<Alien> alienList;

    public Planeta() {
    }

    public Planeta(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNomhabitants() {
        return nomhabitants;
    }

    public void setNomhabitants(String nomhabitants) {
        this.nomhabitants = nomhabitants;
    }

    @XmlTransient
    public List<Alien> getAlienList() {
        return alienList;
    }

    public void setAlienList(List<Alien> alienList) {
        this.alienList = alienList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planeta)) {
            return false;
        }
        Planeta other = (Planeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uf6.bd.space.Planeta[ id=" + id + " ]";
    }
    
}
