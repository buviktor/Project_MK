Insert into persons (ID, name, password, email, postcode, country, county, city, active) values 
    (1, 'teszt1', 'teszt1', 'teszt1@valami.hu', 4032, 'Magyarország', 'Hajdú-Bihar', 'Debrecen', true),
    (2, 'teszt2', 'teszt2', 'teszt2@valami.hu', 1182, 'Magyarország', 'Pest', 'Budapest', true);
	
Insert into categories(ID, denomination) values
    (1, 'élelmiszer'),
    (2, 'alkohol'),
    (3, 'ruházat'),
    (4, 'tárgy'),
    (5, 'szolgáltatás'),
    (6, 'édesség/nassolnivaló'),
	(7, 'egyéb');
	
Insert into registers (ID, personsID, amount, dates, categoriesID) values 
    (1, 1, 100000, '2018-01-01', NULL),
    (2, 1, -250, '2018-01-03', 6),
    (3, 1, -5000, '2018-01-04', 1),
    (4, 1, -15432, '2018-01-07', 3),
    (5, 1, -657, '2018-01-14', 2),
    (6, 1, -988, '2018-01-18', 5),
    (7, 1, 155000, '2018-01-20', NULL),
    (8, 1, -324, '2018-02-02', 1),
    (9, 1, -3200, '2018-02-09', 1),
    (10, 1, -320, '2018-02-16', 2),
    (11, 2, 200000, '2018-01-02', NULL),
    (12, 2, -65000, '2018-01-03', 4),
    (13, 2, -980, '2018-01-05', 1),
    (14, 2, -7845, '2018-01-09', 3),
    (15, 2, -450, '2018-01-15', 6),
    (16, 2, -555, '2018-01-27', 7),
    (17, 2, 35000, '2018-02-02', NULL),
    (18, 2, -10345, '2018-02-08', 5),
    (19, 2, -670, '2018-02-13', 1),
    (20, 2, -871, '2018-02-19', 7);