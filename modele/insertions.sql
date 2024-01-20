-- fichier d'insertions de la base de données du projet

-- insertions dans la table UTILISATEUR
INSERT INTO UTILISATEUR (idU, nomUtilisateur, mdpU) values
(1, 'quentin', 'adm'),
(2, 'loann', 'loann'),
(3, 'leni', 'leni');

-- insertions dans la table ABONNE
INSERT INTO ABONNE (abonnementA, abonneA) values
(1, 2), -- quentin est abonné (abonnement) à loann
(2, 3); -- loann est abonné (abonnement) à leni

-- insertions dans la table MESSAGE
INSERT INTO MESSAGE (idM, contenuM, dateM, idU) values
(1, "ceci est un test", '2023-12-24 10:48:00', 1), -- message de quentin
(2, "il fait beau aujourd'hui", '2024-12-26 07:24:00', 2); -- message de loann

-- insertions dans la table LIKES
INSERT INTO LIKES (idM, idU) values
(1, 2), -- loann a liké le message d'id 1
(2, 3); -- leni a liké le message d'id 2