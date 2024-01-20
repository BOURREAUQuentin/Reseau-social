-- fichier d'insertions de la base de données du projet

-- insertions dans la table CLIENT
INSERT INTO 'CLIENT' (idC, nomUtilisateurC, emailC, mdpC) values
(1, 'quentin', 'quentin@gmail.com', 'adm'),
(2, 'loann', 'loann@gmail.com', 'loann'),
(3, 'leni', 'leni@gmail.com', 'leni');

-- insertions dans la table ABONNE
INSERT INTO 'ABONNE' (abonnementA, abonneA) values
(1, 2), -- quentin est abonné (abonnement) à loann
(2, 3); -- loann est abonné (abonnement) à leni

-- insertions dans la table MESSAGE
INSERT INTO 'MESSAGE' (idM, contenuM, idC) values
(1, "ceci est un test", 1), -- message de quentin
(2, "il fait beau aujourd'hui", 2); -- message de loann

-- insertions dans la table LIKE
INSERT INTO 'LIKE' (idM, idC) values
(1, 2), -- loann a liké le message d'id 1
(2, 3); -- leni a liké le message d'id 2