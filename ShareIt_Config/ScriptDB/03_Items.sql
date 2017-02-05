DROP TABLE IF EXISTS 'ITEMS';


CREATE TABLE Items (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(30) NOT NULL,
description VARCHAR(500) ,
image_uri VARCHAR(350),
price_credit DECIMAL(7,2) NOT NULL,
category_code INT(6) UNSIGNED NOT NULL,
active SMALLINT(1) UNSIGNED NOT NULL,
owner_id INT(6) UNSIGNED,
creation_date DATE NOT NULL,
publish_date DATE,
sell_date DATE,
longitude DECIMAL(16,8) NOT NULL DEFAULT 0,
latitude DECIMAL(16,8) NOT NULL DEFAULT 0,
FOREIGN KEY (category_code) REFERENCES categories(id),
FOREIGN KEY (owner_id) REFERENCES users(id)
);