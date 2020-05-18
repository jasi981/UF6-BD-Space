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
@Table(name = "NAU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nau.findAll", query = "SELECT n FROM Nau n")
    , @NamedQuery(name = "Nau.findById", query = "SELECT n FROM Nau n WHERE n.id = :id")
    , @NamedQuery(name = "Nau.findByNom", query = "SELECT n FROM Nau n WHERE n.nom = :nom")
    , @NamedQuery(name = "Nau.findByLongitud", query = "SELECT n FROM Nau n WHERE n.longitud = :longitud")})
public class Nau implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOM")
    private String nom;
    @Column(name = "LONGITUD")
    private Integer longitud;
    @OneToMany(mappedBy = "nau")
    private List<Alien> alienList;

    public Nau() {
    }

    public Nau(Integer id) {
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

    public Integer getLongitud() {
        return longitud;
    }

    public void setLongitud(Integer longitud) {
        this.longitud = longitud;
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
        if (!(object instanceof Nau)) {
            return false;
        }
        Nau other = (Nau) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "uf6.bd.space.Nau[ id=" + id + " ]";
    }
    
}
