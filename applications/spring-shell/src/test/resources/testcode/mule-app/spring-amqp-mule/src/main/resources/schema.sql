DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
                               id INT AUTO_INCREMENT  PRIMARY KEY,
                               username VARCHAR(250) NOT NULL,
                               password VARCHAR(250) NOT NULL
);