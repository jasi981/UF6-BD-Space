	DROP TABLE caracteristiquesAlien;
	DROP TABLE alien;
	DROP TABLE caracteristica;
	DROP TABLE planeta;
	DROP TABLE nau;

	CREATE TABLE planeta (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, 
	nom VARCHAR(25),
	nomHabitants VARCHAR(25),
	PRIMARY KEY (ID) 
	);

	CREATE TABLE caracteristica (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	descripcio VARCHAR(50),
	PRIMARY KEY (ID) 
	);

	CREATE TABLE alien (
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	nom VARCHAR(25),
	planeta INTEGER REFERENCES Planeta,
	PRIMARY KEY (ID) 
	);

	
	CREATE TABLE caracteristiquesAlien (
	 codiAlien INTEGER  REFERENCES alien,
	 codiCaracteristica  INTEGER REFERENCES caracteristica,
	 PRIMARY KEY(codiAlien,codiCaracteristica)
	);

	CREATE TABLE nau(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
	nom VARCHAR(25),
	longitud INT,
	PRIMARY KEY (ID)
	);
	
	Alter table alien add nau integer;
	Alter table alien add foreign key(nau) references nau(id);

	insert into planeta(nom,nomHabitants) values ('Mart','marcians'); -- id=1
	insert into planeta(nom,nomHabitants) values ('Naboo','Naboos');  -- id=2
	insert into planeta(nom,nomHabitants) values ('Klingon','klingons');  -- id=3
	insert into planeta(nom,nomHabitants) values ('Kripton','kliptonians');  -- id=4
	insert into planeta(nom,nomHabitants) values ('Alderaan','Alderaanians');  -- id=5

	insert into caracteristica(descripcio) values ('humanoide');
	insert into caracteristica(descripcio) values ('té poders');
	insert into caracteristica(descripcio) values ('té tres ulls');
	insert into caracteristica(descripcio) values ('sap volar');
	insert into caracteristica(descripcio) values ('és perillós');
	insert into caracteristica(descripcio) values ('menja rèptils');

	insert into alien(nom,planeta) values ('Clark Ken',4); -- id=1
	insert into caracteristiquesalien(codialien,codicaracteristica) values(1,1); -- Clark Ken és humanoide
	insert into caracteristiquesalien(codialien,codicaracteristica) values(1,2); -- Clark Ken té poders
	insert into caracteristiquesalien(codialien,codicaracteristica) values(1,4); -- Clark Ken sap volar
