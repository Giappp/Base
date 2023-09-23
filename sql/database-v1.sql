CREATE SCHEMA logistics;

CREATE  TABLE logistics.product_type ( 
	product_type_id      INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(100)       ,
	description          TEXT       
 ) engine=InnoDB;

CREATE  TABLE logistics.supplier ( 
	supplier_id          INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(100)       ,
	address              VARCHAR(100)       ,
	phone                VARCHAR(12)       ,
	email                VARCHAR(100)       ,
	details              TEXT       
 ) engine=InnoDB;

CREATE  TABLE logistics.product ( 
	product_id           INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	supplier_id          INT       ,
	product_type_id      INT       ,
	name                 VARCHAR(100)       ,
	description          TEXT       ,
	CONSTRAINT fk_product_supplier FOREIGN KEY ( supplier_id ) REFERENCES logistics.supplier( supplier_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE  TABLE logistics.product_storage ( 
	product_id           INT  NOT NULL     PRIMARY KEY,
	amount               BIGINT       ,
	CONSTRAINT fk_product_storage_product FOREIGN KEY ( product_id ) REFERENCES logistics.product( product_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE  TABLE logistics.seller_storage ( 
	seller_id            INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	product_id           INT  NOT NULL     ,
	amount               BIGINT       ,
	CONSTRAINT fk_seller_storage_product FOREIGN KEY ( product_id ) REFERENCES logistics.product( product_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE  TABLE logistics.seller ( 
	seller_id            INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(100)       ,
	address              VARCHAR(100)       ,
	phone                VARCHAR(12)       ,
	email                VARCHAR(100)       ,
	details              TEXT       ,
	CONSTRAINT fk_seller_seller_storage FOREIGN KEY ( seller_id ) REFERENCES logistics.seller_storage( seller_id ) ON DELETE CASCADE ON UPDATE CASCADE
 ) engine=InnoDB;

CREATE  TABLE logistics.`order` ( 
	order_id             INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	seller_id            INT       ,
	order_date           DATE       ,
	description          TEXT       ,
	`status`             CHAR(1)       ,
	CONSTRAINT fk_order_seller FOREIGN KEY ( seller_id ) REFERENCES logistics.seller( seller_id ) ON DELETE CASCADE ON UPDATE CASCADE
 ) engine=InnoDB;

CREATE  TABLE logistics.order_item ( 
	order_item_id        INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	order_id             INT       ,
	product_id           INT       ,
	amount               BIGINT       ,
	CONSTRAINT fk_order_item_order FOREIGN KEY ( order_id ) REFERENCES logistics.`order`( order_id ) ON DELETE CASCADE ON UPDATE CASCADE
 ) engine=InnoDB;

CREATE  TABLE logistics.transportation ( 
	trasport_id          INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	order_id             INT       ,
	order_item_id        INT       ,
	`status`             CHAR(1)       ,
	transport_date       DATE       ,
	description          TEXT       ,
	CONSTRAINT fk_transportation_order_item FOREIGN KEY ( order_id ) REFERENCES logistics.`order`( order_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;
