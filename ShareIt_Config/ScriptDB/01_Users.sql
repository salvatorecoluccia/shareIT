DROP TABLE IF EXISTS 'USERS';


CREATE TABLE Users (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
firstname VARCHAR(30) NOT NULL,
lastname VARCHAR(30) NOT NULL,
email VARCHAR(50) NOT NULL,
userName VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(20) NOT NULL,
credit DECIMAL(7,2) NOT NULL DEFAULT 0,00,
active SMALLINT(1) UNSIGNED NOT NULL,
admin SMALLINT(1) UNSIGNED NOT NULL,
reg_date TIMESTAMP,
picture VARCHAR(200)
)