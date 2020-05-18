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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jasmeen Singh <jasi981@vidalibarraquer.net>
 */
@Entity
@Table(name = "ALIEN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alien.findAll", query = "SELECT a FROM Alien a")
    , @NamedQuery(name = "Alien.findById", query = "SELECT a FROM Alien a WHERE a.id = :id")
    , @NamedQuery(name = "Alien.findByNom", query = "SELECT a FROM Alien a WHERE a.nom = :nom")})
public class Alien implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOM")
    private String nom;
    @JoinTable(name = "CARACTERISTIQUESALIEN", joinColumns = {
        @JoinColumn(name = "CODIALIEN", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CODICARACTERISTICA", referencedColumnName = "ID")})
    @ManyToMany
    private List<Caracteristica> caracteristicaList;
    @JoinColumn(name = "NAU", referencedColumnName = "ID")
    @ManyToOne
    private Nau nau;
    @JoinColumn(name = "PLANETA", referencedColumnName = "ID")
    @ManyToOne
    private Planeta planeta;

    public Alien() {
    }

    public Alien(Integer id) {
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

    @XmlTransient
    public List<Caracteristica> getCaracteristicaList() {
        return caracteristicaList;
    }

    public void setCaracteristicaList(List<Caracteristica> caracteristicaList) {
        this.caracteristicaList = caracteristicaList;
    }

    public Nau getNau() {
        return nau;
    }

    public void setNau(Nau nau) {
        this.nau = nau;
    }

    public Planeta getPlaneta() {
        return planeta;
    }

    public void setPlaneta(Planeta planeta) {
        this.planeta = planeta;
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
        if (!(object instanceof Alien)) {
            return false;
        }
        Alien other = (Alien) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uf6.bd.space.Alien[ id=" + id + " ]";
    }
    
}
