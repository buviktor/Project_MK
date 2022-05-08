Insert into persons (ID, name, password, email, postcode, country, county, city, active) values 
    (1, 'Személy1', '$2b$10$9jINSWgzHPQPndkJ/9wqiuzfz/lsIERnrxEcQANUxTtvCLjeuhDIK', 'személy1@valami.hu', 4032, 'Magyarország', 'Hajdú-Bihar', 'Debrecen', "2022-05-01"),
    (2, 'Személy2', '$2b$10$Nx8udleGnXa8U.MD58mTpu8A4StdV9n6RaDZEcgrJjEbxiX7N2xxS', 'személy2@valami.hu', 1182, 'Magyarország', 'Pest', 'Budapest',"2020-09-12"),
    (3, 'Személy3', '$2b$10$x5.fptzczJiIPwG3Xkf53.QFrt9kYJdSGiWNuA/TwTWalLm4QjzWG', 'személy3@valami.hu', 9700, 'Magyarország', 'Vas', 'Szombathely', "2014-03-05");
	
Insert into categories(ID, denomination) values
    (1, 'Élelmiszer'),
    (2, 'Ruházat'),
    (3, 'Fizetés'),
    (4, 'Szabadidő'),
    (5, 'Számlák'),
    (6, 'Egyéb');
	
Insert into registers (ID, personsID, amount, regAt, categoriesID) values 
    (1, 1, -1000, '2018-01-01', 1),
    (2, 1, -5000, '2018-01-03', 2),
    (3, 1,  500000, '2018-01-04', 3),
    (4, 1,  -2000, '2018-01-01', 1),
    (5, 1,  -3000, '2018-01-02', 1),
    (6, 2, -30000,'2018-01-07', 4),
    (7, 2, -25000, '2018-01-07', 5),
    (8, 2,  30000, '2018-01-07', 6),
    (9, 3, -15000, '2018-01-07', 1),
    (10, 3, -30000, '2018-01-07', 2),
    (11, 3,  2000, '2018-01-07', 3);
  