CREATE SCHEMA pos;

CREATE  TABLE pos.customer ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	name                 VARCHAR(255)       ,
	address              VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.invoice ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	customer_id          INT       ,
	user_id              INT       ,
	payment_type         INT       ,
	total_price          DOUBLE       ,
	total_paid           DOUBLE       ,
	date_recorded        DATE       ,
	order_id             INT       ,
	CONSTRAINT unq_invoice UNIQUE ( order_id ) ,
	CONSTRAINT invoice_ibfk_1 FOREIGN KEY ( customer_id ) REFERENCES pos.customer( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX customer_id ON pos.invoice ( customer_id );

CREATE INDEX user_id ON pos.invoice ( user_id );

CREATE  TABLE pos.product_category ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	name                 VARCHAR(255)       ,
	description          TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.supplier ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	address              VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	details              TEXT       
 ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.users ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	username             VARCHAR(255)       ,
	password             VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	details              TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.`order` ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	customer_id          INT       ,
	user_id              INT       ,
	date_recorded        DATE       ,
	`status`             TINYINT       ,
	CONSTRAINT fk_order_invoice FOREIGN KEY ( id ) REFERENCES pos.invoice( order_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT order_ibfk_1 FOREIGN KEY ( customer_id ) REFERENCES pos.customer( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT order_ibfk_2 FOREIGN KEY ( user_id ) REFERENCES pos.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX customer_id ON pos.`order` ( customer_id );

CREATE INDEX user_id ON pos.`order` ( user_id );

CREATE  TABLE pos.product ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	supplier_id          INT       ,
	product_type_id      INT       ,
	quantity_in_stock    INT       ,
	sale_price           FLOAT       ,
	imported_price       FLOAT       ,
	discount_percentage  FLOAT       ,
	description          TEXT       ,
	`status`             VARCHAR(45)       ,
	image                VARCHAR(255)       ,
	CONSTRAINT product_ibfk_1 FOREIGN KEY ( product_type_id ) REFERENCES pos.product_category( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT product_ibfk_2 FOREIGN KEY ( supplier_id ) REFERENCES pos.supplier( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX product_type_id ON pos.product ( product_type_id );

CREATE INDEX id ON pos.product ( id );

CREATE INDEX product_ibfk_2_idx ON pos.product ( supplier_id );

CREATE  TABLE pos.product_in_order ( 
	order_id             INT       ,
	product_id           INT       ,
	quantity             INT       ,
	CONSTRAINT product_in_order_ibfk_1 FOREIGN KEY ( order_id ) REFERENCES pos.`order`( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT product_in_order_ibfk_2 FOREIGN KEY ( product_id ) REFERENCES pos.product( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX order_id ON pos.product_in_order ( order_id );

CREATE INDEX product_id ON pos.product_in_order ( product_id );

CREATE  TABLE pos.goods_import ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	product_id           INT       ,
	quantity             INT       ,
	unit_price           DOUBLE       ,
	total_price          DOUBLE       ,
	date_imported        DATE       ,
	user_id              INT       ,
	CONSTRAINT goods_import_ibfk_2 FOREIGN KEY ( user_id ) REFERENCES pos.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT goods_import_ibfk_3 FOREIGN KEY ( product_id ) REFERENCES pos.product( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON pos.goods_import ( user_id );

CREATE INDEX goods_import_ibfk_3_idx ON pos.goods_import ( product_id );

CREATE TRIGGER pos.goods_import_AFTER_INSERT AFTER INSERT ON goods_import FOR EACH ROW BEGIN
	UPDATE product 
	SET `quantity_in_stock` = `quantity_in_stock` + NEW.quantity, `status` = 1
	WHERE id = NEW.product_id;
END;

CREATE TRIGGER pos.product_in_order_AFTER_DELETE AFTER DELETE ON product_in_order FOR EACH ROW BEGIN
	UPDATE product
    SET `amount` = `amount` + (
        SELECT `quantity` FROM product_in_order WHERE product_id = OLD.product_id
    )
    WHERE product_id = OLD.product_id;
END;

CREATE TRIGGER pos.product_in_order_AFTER_INSERT AFTER INSERT ON product_in_order FOR EACH ROW BEGIN
    UPDATE product
    SET `amount` = `amount` - (
        SELECT `quantity` FROM product_in_order WHERE product_id = NEW.product_id
    )
    WHERE product_id = NEW.product_id;
END;

INSERT INTO pos.product_category( id, name, description ) VALUES ( 1, 'testValue', 'abc');
INSERT INTO pos.supplier( id, name, address, phone, email, details ) VALUES ( 1, 'Test', 'Ung Hoa Ha Noi Viet Nam', '0123456789', '1232abcxyz@gmail.com', null);
INSERT INTO pos.supplier( id, name, address, phone, email, details ) VALUES ( 2, 'Cung cap 123', 'Ha Dong Ha Noi Viet Nam', '0123456789', 'abc12345@gmail.com', null);
INSERT INTO pos.users( id, username, password, phone, email, details ) VALUES ( 1, 'admin', 'admin', '0939306888', null, 'abc');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 1, 'testabc', 1, 1, 1600, 0.0, 0.0, null, null, null, 'F:/Workspace/Base/src/main/resources/controller/images/order1.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 2, 'abc', 1, 1, 1000, 0.0, 0.0, null, null, null, 'F:/Workspace/Base/src/main/resources/controller/images/order1.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 4, '123', 1, 1, 300, 5.0, 0.0, null, null, null, 'F:/Workspace/Base/src/main/resources/controller/images/order1.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 9, '123', 1, 1, 0, 2.0, 0.0, null, null, '0', 'F:/Workspace/Base/src/main/resources/controller/images/order1.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 10, '23', 1, 1, 0, 4.0, 0.0, null, null, '1', 'file:/F:/Workspace/Base/src/main/resources/controller/images/product.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 11, '12324', 1, 1, 0, 5.0, 0.0, null, null, '1', 'file:/F:/Workspace/Base/src/main/resources/controller/images/right-arrow.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 12, 'ImportedPriceTest', 1, 1, 0, 7.0, 3.0, null, null, '0', 'file:/F:/Workspace/Base/src/main/resources/controller/images/right-arrow.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 13, '2225', 1, 1, 1500, 2.0, 5.0, null, null, '0', 'file:/F:/Workspace/Base/src/main/resources/controller/images/order1.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 19, 'ád', 1, 1, 0, 34.0, 5.0, null, null, '1', 'file:/F:/Workspace/Base/src/main/resources/controller/images/right-arrow.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 20, '222', 1, 1, 0, 22.0, 0.0, null, null, '1', 'file:/F:/Workspace/Base/src/main/resources/controller/images/right-arrow.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 23, '555', 1, 1, 0, 23.0, 6.0, null, null, '1', 'file:/F:/Workspace/Base/src/main/resources/controller/images/right-arrow.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, imported_price, discount_percentage, description, `status`, image ) VALUES ( 24, '2551', 1, 1, 0, 25.0, 2.0, null, null, '0', 'F:\\Workspace\\Base\\src\\main\\resources\\controller\\images\\default.jpg');
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 1, 1, 200, 2.5, 500.0, '2023-09-30', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 6, 2, 100, 3.0, 300.0, '2023-09-30', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 7, 2, 200, 3.0, 600.0, '2023-09-30', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 8, 12, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 9, 12, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 10, 12, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 11, 12, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 12, 1, 200, 2.5, 500.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 13, 12, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 14, 12, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 15, 1, 200, 2.5, 500.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 16, 1, 200, 2.5, 500.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 17, 2, 200, 3.0, 600.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 18, 4, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 19, 4, 100, 3.0, 300.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 20, 13, 500, 5.0, 2500.0, '2023-10-03', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 21, 13, 500, 5.0, 2500.0, '2023-10-03', null);