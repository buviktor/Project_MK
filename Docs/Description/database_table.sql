CREATE TABLE persons (
    ID int NOT NULL AUTO_INCREMENT,
    name  varchar(50) NOT NULL,
	password text NOT NULL,
	email text NOT NULL,
	postcode int(4) NOT NULL,
	country varchar(30) NOT NULL,
	county varchar(30) NOT NULL,
	city varchar(30) NOT NULL,
	active boolean DEFAULT true,
    PRIMARY KEY (ID)
	);

CREATE TABLE categories (
    ID int NOT NULL AUTO_INCREMENT,
    denomination  varchar(255) NOT NULL,
    PRIMARY KEY (ID)
	);
	
CREATE TABLE registers (
    ID int NOT NULL AUTO_INCREMENT,
    personsID int NOT NULL,
    amount int NOT NULL,
	dates DATE NOT NULL,
	categoriesID int,
    PRIMARY KEY (ID),
	FOREIGN KEY (`personsID`) REFERENCES `persons`(`ID`) ON DELETE CASCADE,
	FOREIGN KEY (`categoriesID`) REFERENCES `categories`(`ID`) ON DELETE CASCADE
	);