-- fichier de création de la base de données du projet
CREATE TABLE CLIENT (
    idC INT NOT NULL,
    nomUtilisateurC VARCHAR(100),
    emailC VARCHAR(100) UNIQUE,
    mdpC VARCHAR(100) UNIQUE,
    PRIMARY KEY (idC)
);

CREATE TABLE ABONNE (
    abonnementA INT NOT NULL,
    abonneA INT NOT NULL,
    FOREIGN KEY (abonnementA) REFERENCES CLIENT(idC),
    FOREIGN KEY (abonneA) REFERENCES CLIENT(idC),
    PRIMARY KEY (abonnementA, abonneA)
);

CREATE TABLE MESSAGE (
    idM INT NOT NULL,
    contenuM VARCHAR(150) NOT NULL,
    idC INT NOT NULL,
    date DATETIME,
    FOREIGN KEY (idX) REFERENCES CLIENT(idC),
    PRIMARY KEY (idM)
);

CREATE TABLE LIKE (
    idM INT NOT NULL,
    idC INT NOT NULL,
    FOREIGN KEY (idM) REFERENCES MESSAGE(idM),
    FOREIGN KEY (idC) REFERENCES CLIENT(idC),
    PRIMARY KEY (idM, idC)
);