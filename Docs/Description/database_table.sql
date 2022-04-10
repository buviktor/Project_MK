CREATE TABLE persons (
    ID int NOT NULL AUTO_INCREMENT,
    name  varchar(255) NOT NULL,
	password text NOT NULL,
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
    denomination  varchar(255) NOT NULL,
    amount int NOT NULL,
	dates DATE NOT NULL,
	categoriesID int,
    PRIMARY KEY (ID)
	);
	
ALTER TABLE `registers` ADD FOREIGN KEY (`personsID`) REFERENCES `persons`(`ID`);

ALTER TABLE `registers` ADD FOREIGN KEY (`categoriesID`) REFERENCES `categories`(`ID`);

