CREATE SCHEMA pos;

CREATE  TABLE pos.customer ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	name                 VARCHAR(255)       ,
	address              VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.product_category ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	name                 VARCHAR(255)       ,
	description          TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.supplier ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	name                 VARCHAR(255)       ,
	address              VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	details              TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.users ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	username             VARCHAR(255)       ,
	password             VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	details              TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE pos.goods_import ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	product_id           INT       ,
	quantity             INT       ,
	unit_price           DOUBLE       ,
	total_price          DOUBLE       ,
	date_imported        DATE       ,
	user_id              INT       ,
	CONSTRAINT goods_import_ibfk_2 FOREIGN KEY ( user_id ) REFERENCES pos.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON pos.goods_import ( user_id );

CREATE  TABLE pos.`order` ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	customer_id          INT       ,
	user_id              INT       ,
	date_recorded        DATE       ,
	`status`             INT       ,
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
	discount_percentage  FLOAT       ,
	description          TEXT       ,
	`status`             VARCHAR(45)       ,
	image                VARCHAR(255)       ,
	CONSTRAINT product_ibfk_1 FOREIGN KEY ( product_type_id ) REFERENCES pos.product_category( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT product_ibfk_2 FOREIGN KEY ( supplier_id ) REFERENCES pos.supplier( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX product_type_id ON pos.product ( product_type_id );

CREATE INDEX supplier_id ON pos.product ( supplier_id );

CREATE  TABLE pos.product_in_order ( 
	order_id             INT       ,
	product_id           INT       ,
	quantity             INT       ,
	CONSTRAINT product_in_order_ibfk_1 FOREIGN KEY ( order_id ) REFERENCES pos.`order`( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT product_in_order_ibfk_2 FOREIGN KEY ( product_id ) REFERENCES pos.product( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX order_id ON pos.product_in_order ( order_id );

CREATE INDEX product_id ON pos.product_in_order ( product_id );

CREATE  TABLE pos.invoice ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	customer_id          INT       ,
	user_id              INT       ,
	order_id             INT       ,
	payment_type         INT       ,
	total_price          DOUBLE       ,
	total_paid           DOUBLE       ,
	date_recorded        DATE       ,
	CONSTRAINT invoice_ibfk_1 FOREIGN KEY ( customer_id ) REFERENCES pos.customer( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT invoice_ibfk_2 FOREIGN KEY ( user_id ) REFERENCES pos.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT invoice_ibfk_3 FOREIGN KEY ( order_id ) REFERENCES pos.`order`( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX customer_id ON pos.invoice ( customer_id );

CREATE INDEX user_id ON pos.invoice ( user_id );

CREATE INDEX order_id ON pos.invoice ( order_id );

CREATE TRIGGER pos.goods_import_AFTER_INSERT AFTER INSERT ON goods_import FOR EACH ROW BEGIN
	IF NEW.product_id not in (
		select p.id from product p
        where (NEW.product_id = p.id)
        )THEN
        Insert into product(`id`,`quantity_in_stock`) VALUES(NEW.product_id,NEW.quantity);
	ELSE
		UPDATE product 
        SET `quantity_in_stock` = `quantity_in_stock` + NEW.quantity WHERE id = NEW.product_id;
	END IF;
END;

CREATE TRIGGER pos.goods_import_BEFORE_INSERT BEFORE INSERT ON goods_import FOR EACH ROW BEGIN
IF NEW.product_id not in (
		select p.id from product p
        where (NEW.product_id = p.id)
        )THEN
        Insert into product(`id`,`quantity_in_stock`) VALUES(NEW.product_id,NEW.quantity);
	ELSE
		UPDATE product 
        SET `quantity_in_stock` = `quantity_in_stock` + NEW.quantity WHERE id = NEW.product_id;
	END IF;
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

CREATE TRIGGER pos.product_in_order_BEFORE_INSERT BEFORE INSERT ON product_in_order FOR EACH ROW BEGIN
	DECLARE storage_amount INT;
    DECLARE order_quantity INT;
    -- Get the current amount in storage for the product
    SELECT `amount` INTO storage_amount FROM product WHERE product_id = NEW.product_id;
    -- Get the quantity for the product_order being inserted
    SET order_quantity = NEW.quantity;
    -- Check if storage amount is less than order quantity
    IF storage_amount < order_quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insufficient quantity in storage';
    END IF;
END;

INSERT INTO pos.product_category( id, name, description ) VALUES ( 1, 'testValue', 'abc');
INSERT INTO pos.supplier( id, name, address, phone, email, details ) VALUES ( 1, 'Test', null, null, null, null);
INSERT INTO pos.users( id, username, password, phone, email, details ) VALUES ( 1, 'admin', 'admin', '0939306888', null, 'abc');
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 1, 1, 200, 2.5, 500.0, '2023-09-30', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 6, 2, 100, 3.0, 300.0, '2023-09-30', 1);
INSERT INTO pos.goods_import( id, product_id, quantity, unit_price, total_price, date_imported, user_id ) VALUES ( 7, 2, 200, 3.0, 600.0, '2023-09-30', 1);
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, discount_percentage, description, `status`, image ) VALUES ( 1, 'test', 1, 1, 1200, 3.5, 0.0, 'abc', 'Ready', 'F:/Workspace/Base/src/main/resources/controller/images/order1.png');
INSERT INTO pos.product( id, name, supplier_id, product_type_id, quantity_in_stock, sale_price, discount_percentage, description, `status`, image ) VALUES ( 2, null, null, null, 600, null, null, null, null, null);