CREATE TABLE persons (
    ID int NOT NULL AUTO_INCREMENT,
    name  varchar(50) NOT NULL,
	password text NOT NULL,
	email text,
	postcode int(4),
	country varchar(30),
	county varchar(30),
	city varchar(30),
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