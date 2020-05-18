package uf6.bd.space;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import uf6.bd.space.exceptions.NonexistentEntityException;

public class UF6BDSpace {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("UF6-BD-SpacePU");

        // Controladors de cada una de les entitats
        AlienJpaController aliensControl = new AlienJpaController(factory);
        PlanetaJpaController planetesControl = new PlanetaJpaController(factory);
        CaracteristicaJpaController caracteristiquesControl = new CaracteristicaJpaController(factory);
        NauJpaController nauscontrol = new NauJpaController(factory);
        
        
        
        
        
        
        
        //El punto 4.1 esta en archivo Instane_Of_Controllers.java

        //4.2 Llistat de planetes
        llistat_de_planetes_4_2(planetesControl);

        System.out.println("----------------------------------------------------------");

        //4.3 Llistat de catacterístiques
        llistat_de_caracteristiques_4_3(caracteristiquesControl);

        System.out.println("----------------------------------------------------------");

        //4.4. Obtenir un planeta en concret fent ús del seu id
        planet_by_id_4_4(2, planetesControl);

        System.out.println("----------------------------------------------------------");

        //4.5 Creació d’un nou planeta
        create_planet_4_5("Terra", "terrícoles",planetesControl);

        System.out.println("----------------------------------------------------------");

        //4.6 Destrucció (eliminació) d’un planeta fent ús del seu id
        destruct_planet_4_6(1,planetesControl);

        System.out.println("----------------------------------------------------------");

        //4.7 Actualització de les dades d’un planeta
        update_planet_4_7(2, "Naboonians",planetesControl);

        System.out.println("----------------------------------------------------------");

        //4.8 Gestió d’un objecte Alien: recuperem un alien i mostrem el nom 
        //sel seu planeta i les diverses característiques d’aquest alien. 
        //A Superman li apliquen Kriptonita!
        update_alien_superman_4_8(aliensControl);

        System.out.println("----------------------------------------------------------");

        //4.9 Creem un nou alien, li assignem planeta i definim les seves característiques. 
        create_new_alien_4_9(caracteristiquesControl, aliensControl, planetesControl);

        System.out.println("----------------------------------------------------------");

        //5.1 CREAR TAULA NAU
        //    CREATE TABLE nau(
        //  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
        //  nom VARCHAR(25),
        //  longitud INT,
        //  PRIMARY KEY (ID,alien)
        //  );
        // Una vez creada la tabla hay que agregar la columna de nau al alien y poner referenciar la columna con la de tabla nau
        //  Alter table alien add nau integer;
        //  Alter table alien add foreign key(nau) references nau(id);
        //5.2 Codi per Insertar 3 naus nous
        insert_3_naus_5_2(nauscontrol);
        
        llistat_de_naus(nauscontrol);

        System.out.println("----------------------------------------------------------");

        //5.3 assignar naus a aliens
        assign_naus_to_aliens_5_3(nauscontrol, aliensControl);

        System.out.println("----------------------------------------------------------");

        //5.4 intentar eliminar nau
        try_del_nau(nauscontrol, 1);
        // si se ha podido esborrar la nave 
        // aunque no entiendo porque lo elimina, creo que no deberia eliminar ya-
        // que existe como clave forana en otra tabla
        
    }
        
        
        
        
    
    public static List<Planeta> llistat_de_planetes_4_2(PlanetaJpaController planetesControl) {
        // Ontenim la llista de tots els Planetes de la BD
        System.out.println("Llista planetes");
        List<Planeta> planetesList = planetesControl.findPlanetaEntities();
        for (int i = 0; i < planetesList.size(); i++) {
            Planeta p = planetesList.get(i);
            System.out.println(p.getId() + " " + p.getNom() + " " + p.getNomhabitants());
        }
        System.out.println("Tenim " + planetesControl.getPlanetaCount() + " planetes");
        return planetesList;
    }

    public static List<Caracteristica> llistat_de_caracteristiques_4_3(CaracteristicaJpaController caracteristiquesControl) {
        // Ontenim la llista de característiques alienígenes
        System.out.println("Llista característiques");
        List<Caracteristica> caracsList = caracteristiquesControl.findCaracteristicaEntities();
        for (int i = 0; i < caracsList.size(); i++) {
            Caracteristica carac = caracsList.get(i);
            System.out.println(carac.getId() + " " + carac.getDescripcio());
        }
        return caracsList;
    }

    public static Planeta planet_by_id_4_4(int id, PlanetaJpaController planetesControl) {
        // Recuperem informació d'un planeta amb id
        Planeta planeta = planetesControl.findPlaneta(id);
        System.out.println("Informació planeta amb id(" + id + "): " + planeta.getNom());
        return planeta;
    }

    public static void create_planet_4_5(String nomPlaneta, String nomHabitants, PlanetaJpaController planetesControl) {
        // Afegim un nou planeta
        // Creem l'objecte i li posem valors
        Planeta nouPlaneta = new Planeta();
        // L'id és automàtic, posem valors a nom i nom dels habitants
        nouPlaneta.setNom(nomPlaneta);
        nouPlaneta.setNomhabitants(nomHabitants);
        // El guardem a la BD
        planetesControl.create(nouPlaneta);
        System.out.println("S'ha creat el planeta " + nouPlaneta.getNom() + " amb id=" + nouPlaneta.getId());
    }

    public static void destruct_planet_4_6(int id, PlanetaJpaController planetesControl) {
        try {
            planetesControl.destroy(id);
            System.out.println("S'ha esborrat el planeta " + id);
        } catch (NonexistentEntityException ex) {
            System.out.println("No s'ha trobat el planeta amb id " + id);;
        }
    }

    public static void update_planet_4_7(int id, String nomHabitants,PlanetaJpaController planetesControl) {
        try {
            Planeta planetaNaboo = planetesControl.findPlaneta(id); // recupero el planeta amb id="id"
            planetaNaboo.setNomhabitants(nomHabitants);
            planetesControl.edit(planetaNaboo);
            System.out.println("S'ha modificat informació del planeta");
        } catch (Exception ex) {
            System.out.println("No s'ha trobat el planeta amb aquest id!");;
        }
    }

    public static void update_alien_superman_4_8(AlienJpaController aliensControl) {
        // Operem amb un objecte més complexe
        Alien superman = aliensControl.findAlien(1);
        System.out.println("L'alien es diu :" + superman.getNom());
        System.out.println("i es originari de: " + superman.getPlaneta().getNom());
        System.out.println("i és:");
        List<Caracteristica> superList = superman.getCaracteristicaList();
        for (int i = 0; i < superList.size(); i++) {
            Caracteristica carac = superList.get(i);
            System.out.println(carac.getId() + " " + carac.getDescripcio());
        }

        try {
            superman.getCaracteristicaList().remove(2);
            aliensControl.edit(superman);
            System.out.println("Superman ya no pot volar!");
        } catch (Exception ex) {
            System.out.println("Superman no pot volar!");
        }
    }

    public static void create_new_alien_4_9(CaracteristicaJpaController caracteristiquesControl, AlienJpaController aliensControl, PlanetaJpaController planetesControl) {
        // Crear un nou Alien
        Alien yoda = new Alien();
        yoda.setNom("Yoda");

        // viu al planeta ...
        yoda.setPlaneta(llistat_de_planetes_4_2(planetesControl).get(1));

        Caracteristica jedi = new Caracteristica();
        jedi.setDescripcio("és un Jedi");
        caracteristiquesControl.create(jedi);

        Caracteristica masculi = new Caracteristica();
        masculi.setDescripcio("és masculí");
        caracteristiquesControl.create(masculi);

        List<Caracteristica> yodaListCaracts = new ArrayList();
        yodaListCaracts.add(jedi);
        yodaListCaracts.add(masculi);
        yoda.setCaracteristicaList(yodaListCaracts);

        aliensControl.create(yoda);
    }

    //5.1 CREAR TAULA NAU
    //    CREATE TABLE nau(
    //  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    //  nom VARCHAR(25),
    //  longitud INT,
    //  PRIMARY KEY (ID,alien)
    //  );
    // Una vez creada la tabla hay que agregar la columna de nau al alien y poner referenciar la columna con la de tabla nau
    //  Alter table alien add nau integer;
    //  Alter table alien add foreign key(nau) references nau(id);
    //5.2 Codi per Insertar 3 naus nous
    public static void insert_3_naus_5_2(NauJpaController nauscontrol) {
        Nau nau1 = new Nau();
        Nau nau2 = new Nau();
        Nau nau3 = new Nau();

        nau1.setLongitud(50);
        nau1.setNom("Kal-El's Ship");

        nau2.setLongitud(55);
        nau2.setNom("Her Majesty's");

        nau3.setLongitud(60);
        nau3.setNom("Imperial Spaceship");

        nauscontrol.create(nau1);
        nauscontrol.create(nau2);
        nauscontrol.create(nau3);
    }

    public static List<Nau> llistat_de_naus(NauJpaController nauscontrol) {
        // Ontenim la llista de tots els Planetes de la BD
        System.out.println("Llista naus");
        List<Nau> nauList = nauscontrol.findNauEntities();
        for (int i = 0; i < nauList.size(); i++) {
            Nau p = nauList.get(i);
            System.out.println(p.getId() + " " + p.getNom());
        }
        System.out.println("Tenim " + nauscontrol.getNauCount() + " naus");
        return nauList;
    }

    //5.3 
    public static void assign_naus_to_aliens_5_3(NauJpaController nauscontrol, AlienJpaController aliensControl) {
        try {
            List<Alien> aliens = aliensControl.findAlienEntities();
            aliens.get(0).setNau(nauscontrol.findNauEntities().get(0));
            aliens.get(1).setNau(nauscontrol.findNauEntities().get(1));
            aliens.get(2).setNau(nauscontrol.findNauEntities().get(2));
            aliensControl.edit(aliens.get(0));
            aliensControl.edit(aliens.get(1));
            aliensControl.edit(aliens.get(2));
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public static void try_del_nau(NauJpaController nauscontrol, int id) {
        try {
            nauscontrol.destroy(id);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


}
