-- fichier de création de la base de données du projet
CREATE TABLE IF NOT EXISTS UTILISATEUR (
    idU INT NOT NULL,
    nomUtilisateur VARCHAR(100) UNIQUE NOT NULL,
    mdpU VARCHAR(100) UNIQUE NOT NULL,
    PRIMARY KEY (idU)
);

CREATE TABLE IF NOT EXISTS ABONNE (
    abonnementA INT NOT NULL,
    abonneA INT NOT NULL,
    FOREIGN KEY (abonnementA) REFERENCES UTILISATEUR(idU),
    FOREIGN KEY (abonneA) REFERENCES UTILISATEUR(idU),
    PRIMARY KEY (abonnementA, abonneA)
);

CREATE TABLE IF NOT EXISTS MESSAGE (
    idM INT NOT NULL,
    contenuM VARCHAR(150) NOT NULL,
    idU INT NOT NULL,
    dateM DATETIME NOT NULL,
    FOREIGN KEY (idU) REFERENCES UTILISATEUR(idU),
    PRIMARY KEY (idM)
);

CREATE TABLE IF NOT EXISTS LIKES (
    idM INT NOT NULL,
    idU INT NOT NULL,
    FOREIGN KEY (idM) REFERENCES MESSAGE(idM),
    FOREIGN KEY (idU) REFERENCES UTILISATEUR(idU),
    PRIMARY KEY (idM, idU)
);