CREATE TABLE szemelyek (
    ID int NOT NULL AUTO_INCREMENT,
    nev  varchar(255) NOT NULL
    PRIMARY KEY (ID)
	);

CREATE TABLE bevetelek (
    ID int NOT NULL AUTO_INCREMENT,
    szemelyekID int NOT NULL,
    megnevezes  varchar(255) NOT NULL,
    osszeg int NOT NULL,
	datum DATE NOT NULL,
    PRIMARY KEY (ID),
	FOREIGN KEY (szemelyekID) REFERENCES szemelyek(ID)
	);

CREATE TABLE kategoriak (
    ID int NOT NULL AUTO_INCREMENT,
    megnevezes  varchar(255) NOT NULL
    PRIMARY KEY (ID)
	);
	
CREATE TABLE kiadasok (
    ID int NOT NULL AUTO_INCREMENT,
    szemelyekID int NOT NULL,
    megnevezes  varchar(255) NOT NULL,
    osszeg int NOT NULL,
	datum DATE NOT NULL,
	kategoriakID int NOT NULL
    PRIMARY KEY (ID),
	FOREIGN KEY (szemelyekID) REFERENCES szemelyek(ID)
	FOREIGN KEY (kategoriakID) REFERENCES kategoriak(ID)
	);

