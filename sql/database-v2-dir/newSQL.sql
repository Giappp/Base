
CREATE  TABLE `POS`.customer ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	address              VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE `POS`.invoice ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	orderId             INT       ,
	customerId          INT       ,
	userId              INT       ,
	paymentType         INT       ,
	totalPrice          DOUBLE       ,
	totalPaid           DOUBLE       ,
	dateRecorded        DATE       ,
	CONSTRAINT unq_invoice UNIQUE ( orderId )
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX customerId ON `POS`.invoice ( customerId );

CREATE INDEX userId ON `POS`.invoice ( userId );

CREATE  TABLE `POS`.product_category ( 
	id                   INT  NOT NULL     PRIMARY KEY,
	name                 VARCHAR(255)       ,
	description          TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE `POS`.supplier ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	address              VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	details              TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE `POS`.users ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	username             VARCHAR(255)       ,
	password             VARCHAR(255)       ,
	phone                VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	details              TEXT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE `POS`.`order` ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	customerId          INT       ,
	userId              INT       ,
	dateRecorded        DATE       ,
	`status`             TINYINT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX customerId ON `POS`.`order` ( customerId );

CREATE INDEX userId ON `POS`.`order` ( userId );

CREATE  TABLE `POS`.product ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	supplierId          INT       ,
	productTypeId      INT       ,
	quantityInStock    INT       ,
	salePrice           FLOAT       ,
	importedPrice       FLOAT       ,
	discountPercentage  FLOAT       ,
	description          TEXT       ,
	`status`             TINYINT       ,
	image                VARCHAR(255)       
 ) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX productTypeId ON `POS`.product ( productTypeId );

CREATE INDEX supplierId ON `POS`.product ( supplierId );

CREATE INDEX id ON `POS`.product ( id );

CREATE  TABLE `POS`.product_in_order ( 
	orderId             INT       ,
	productId           INT       ,
	quantity             INT       
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX orderId ON `POS`.product_in_order ( orderId );

CREATE INDEX productId ON `POS`.product_in_order ( productId );

CREATE  TABLE `POS`.goods_import ( 
	id                   INT  NOT NULL   AUTO_INCREMENT  PRIMARY KEY,
	productId           INT       ,
	quantity             INT       ,
	unitPrice           DOUBLE       ,
	totalPrice          DOUBLE       ,
	dateImported        DATE       ,
	userId              INT
 ) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO pos.product_category (id, name, description) VALUES
                                                             (1, 'Rectifier Diode', 'Linh kiện điện tử Diode'),
                                                             (2, 'Capacitor', 'Linh kiện điện tử Capacitor'),
                                                             (3, 'Transistors', 'Linh kiện điện tử Transistors'),
                                                             (4, 'IC - Microchip', 'Linh kiện điện tử IC - Microchip'),
                                                             (5, 'Inductor', 'Linh kiện điện tử Inductor'),
                                                             (6, 'Resistors', 'Linh kiện điện tử Resistors'),
                                                             (7, 'Relays', 'Linh kiện điện tử Relays'),
                                                             (8, 'MOSFET', 'Linh kiện điện tử MOSFET');

INSERT INTO pos.product (id, name, supplierId, productTypeId, quantityInStock, salePrice, importedPrice, `status`, image)
VALUES
    (1, 'Rectifier Diode', 1, 1, 100, 1.5, 1.0, 1, 'image_path_1.png'),
    (2, 'Schottky Diode', 1, 1, 200, 1.8, 1.2, 1, 'image_path_2.png'),
    (3, 'Switching Diode', 1, 1, 120, 1.2, 1.0, 1, 'image_path_3.png'),
    (4, 'LED', 1, 1, 80, 2.0, 1.5, 1, 'image_path_4.png'),
    (5, 'Zener Diode', 1, 1, 60, 1.4, 1.0, 1, 'image_path_5.png'),
    (6, 'ESD Diode', 1, 1, 40, 1.0, 0.7, 1, 'image_path_6.png'),
    (7, 'Tunnel Diode', 1, 1, 30, 1.2, 1.0, 1, 'image_path_7.png'),
    (8, 'Varicap Diode', 1, 1, 25, 1.5, 1.2, 1, 'image_path_8.png'),
    (9, 'Photodiode', 1, 1, 70, 2.5, 2.0, 1, 'image_path_9.png'),
    (10, 'Laser Diode', 1, 1, 60, 3.0, 2.5, 1, 'image_path_10.png'),
    (11, 'Line Diode', 1, 1, 50, 2.8, 2.3, 1, 'image_path_11.png'),

    (12, 'Polarized Capacitor', 2, 2, 200, 1.0, 0.8, 1, 'image_path_12.png'),
    (13, 'Non-polar Capacitor', 2, 2, 150, 0.8, 0.6, 1, 'image_path_13.png'),

    (14, 'BJT Transistor', 3, 3, 100, 1.0, 0.8, 1, 'image_path_14.png'),
    (15, 'FET Transistor', 3, 3, 90, 2.0, 1.7, 1, 'image_path_15.png'),
    (16, 'Unipolar Transistor', 3, 3, 85, 1.7, 1.4, 1, 'image_path_16.png'),

    (17, 'Opamp IC', 2, 4, 65, 3.2, 2.7, 1, 'image_path_17.png'),
    (18, 'Opto-Isolator IC', 2, 4, 55, 3.0, 2.6, 1, 'image_path_18.png'),

    (19, 'Air Core Inductor', 1, 5, 70, 2.5, 2.0, 1, 'image_path_19.png'),
    (20, 'Dust Iron Core Inductor', 1, 5, 70, 3.2, 2.7, 1, 'image_path_20.png'),
    (21, 'Leaf Iron Core Inductor', 1, 5, 55, 3.0, 2.6, 1, 'image_path_21.png'),

    (22, 'Linear Resistor', 3, 6, 500, 0.5, 0.3, 1, 'image_path_22.png'),
    (23, 'Nonlinear Resistor', 3, 6, 400, 0.9, 0.7, 1, 'image_path_23.png'),
    (24, 'Fixed Resistor', 3, 6, 800, 0.3, 0.2, 1, 'image_path_24.png'),
    (25, 'Variable Resistor', 3, 6, 500, 1.2, 1.0, 1, 'image_path_25.png'),
    (26, 'Thermistor', 3, 6, 250, 1.7, 1.5, 1, 'image_path_26.png'),
    (27, 'Photo Resistor LDR', 3, 6, 350, 1.4, 1.2, 1, 'image_path_27.png'),
    (28, 'Varistor Resistor', 3, 6, 600, 2.2, 2.0, 1, 'image_path_28.png'),
    (29, 'Surface Mount (SMD) Resistor', 3, 6, 800, 0.6, 0.4, 1, 'image_path_29.png'),
    (30, 'Potentiometer', 3, 6, 450, 3.2, 2.7, 1, 'image_path_30.png'),
    (31, 'Rheostat', 3, 6, 250, 4.2, 3.7, 1, 'image_path_31.png'),
    (32, 'Trimmer', 3, 6, 350, 2.2, 2.0, 1, 'image_path_32.png'),
    (33, 'Carbon Composition Resistor', 3, 6, 200, 1.2, 1.0, 1, 'image_path_33.png'),
    (34, 'Wire-Wound Resistor', 3, 6, 450, 2.7, 2.2, 1, 'image_path_34.png'),
    (35, 'Thick Film Resistor', 3, 6, 350, 0.6, 0.4, 1, 'image_path_35.png'),
    (36, 'Fusible Resistor', 3, 6, 250, 1.2, 1.0, 1, 'image_path_36.png'),
    (37, 'Cermet Film Resistor', 3, 6, 450, 0.6, 0.4, 1, 'image_path_37.png'),
    (38, 'Metal Oxide Resistor', 3, 6, 350, 0.9, 0.7, 1, 'image_path_38.png'),
    (39, 'Carbon Film Resistor', 3, 6, 200, 1.2, 1.0, 1, 'image_path_39.png'),
    (40, 'Metal Film Resistor', 3, 6, 400, 2.5, 2.0, 1, 'image_path_40.png'),

    (41, 'Electronic Relay', 2, 7, 500, 5.0, 4.0, 1, 'image_path_41.png'),
    (42, 'Voltage Protection Relay', 2, 7, 400, 6.0, 5.0, 1, 'image_path_42.png'),
    (43, 'Line Protection Relay', 2, 7, 600, 7.0, 6.0, 1, 'image_path_43.png'),
    (44, 'Magnetic Latching Relay', 2, 7, 300, 8.0, 7.0, 1, 'image_path_44.png'),
    (45, 'Semiconductor Relay', 2, 7, 350, 9.0, 8.0, 1, 'image_path_45.png'),
    (46, 'Thermal Relay', 2, 7, 450, 10.0, 9.0, 1, 'image_path_46.png'),
    (47, 'Time Relay', 2, 7, 550, 11.0, 10.0, 1, 'image_path_47.png'),

    (48, 'N-MOSFET', 1, 8, 80, 1.5, 1.2, 1, 'image_path_48.png'),
    (49, 'P-MOSFET', 1, 8, 600, 0.6, 0.4, 1, 'image_path_49.png');

INSERT INTO pos.goods_import (productId, userId, quantity, unitPrice, totalPrice, dateImported) VALUES
                                                                                                         (1, 1, 200, 1.0, 200.0, '2023-10-05'),
                                                                                                         (2, 2, 300, 1.2, 360.0, '2023-10-05'),
                                                                                                         (3, 3, 150, 0.8, 120.0, '2023-10-05'),
                                                                                                         (4, 2, 180, 0.9, 162.0, '2023-10-05');

INSERT INTO pos.users (id, username, password, phone, email, details) VALUES
                                                                          (1, 'thanh', '1', '0912345678', 'thanh@gmail.com', 'Thanh details'),
                                                                          (2, 'admin', 'admin', '0939306888', 'admin@gmail.com', 'Admin details'),
                                                                          (3, 'jame', 'jame123', '0953750413', 'jame@gmail.com', 'Jame details'),
                                                                          (4, 'alice', 'alice101', '0987654321', 'alice@gmail.com', 'Alice details');

INSERT INTO pos.customer (id, name, address, phone, email) VALUES
                                                               (1, 'swain', '123 Main St', '0123456789', 'swain@gmail.com'),
                                                               (2, 'teemo', '456 Elm St', '0987654321', 'teemo@gmail.com'),
                                                               (3, ' urgot', '789 Oak St', '0369696969', 'urgotC@gmail.com');

INSERT INTO pos.supplier (id, name, address, phone, email, details) VALUES
                                                                        (1, 'Glasc Industries', '789 Supplier Rd', '0123456789', 'renata-glasc@gmail.com', 'Glasc Industries details'),
                                                                        (2, 'ZAIA Enterprise', '456 Supplier Ave', '0987654321', '1000percent@gmail.com', 'ZAIA Enterprise details'),
                                                                        (3, 'Hiden Intelligence', '123 Supplier Blvd', '0369696969', 'zero-oneZ@gmail.com', 'Hiden Intelligence details');

INSERT INTO pos.`order` (id, customerId, userId, dateRecorded, `status`)
VALUES
    (1, 1, 1, '2023-10-05', 1),
    (2,2, 2, '2023-10-05', 1),
    (3,3, 2, '2023-10-06', 1),
    (4,2, 1, '2023-10-06', 1),
    (5,1, 3, '2023-10-07', 1);

INSERT INTO pos.invoice (orderId, customerId, userId, paymentType, totalPrice, totalPaid, dateRecorded)
VALUES
    (1, 1, 1, 1, 500.0, 500.0, '2023-10-05'),
    (2, 2, 1, 2, 600.0, 600.0, '2023-10-05'),
    (3, 3, 2, 1, 700.0, 700.0, '2023-10-06'),
    (4, 1, 3, 2, 800.0, 800.0, '2023-10-06'),
    (5, 3, 1, 1, 900.0, 900.0, '2023-10-07');

CREATE INDEX userId ON `POS`.goods_import ( userId );

CREATE INDEX goods_import_ibfk_3_idx ON `POS`.goods_import ( productId );

ALTER TABLE `POS`.goods_import ADD CONSTRAINT goods_import_ibfk_2 FOREIGN KEY ( userId ) REFERENCES `POS`.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.goods_import ADD CONSTRAINT goods_import_ibfk_3 FOREIGN KEY ( productId ) REFERENCES `POS`.product( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.invoice ADD CONSTRAINT invoice_ibfk_1 FOREIGN KEY ( customerId ) REFERENCES `POS`.customer( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.`order` ADD CONSTRAINT order_ibfk_1 FOREIGN KEY ( customerId ) REFERENCES `POS`.customer( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.`order` ADD CONSTRAINT order_ibfk_2 FOREIGN KEY ( userId ) REFERENCES `POS`.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.`order` ADD CONSTRAINT fk_order_invoice FOREIGN KEY ( id ) REFERENCES `POS`.invoice( orderId ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.product ADD CONSTRAINT product_ibfk_1 FOREIGN KEY ( productTypeId ) REFERENCES `POS`.product_category( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.product ADD CONSTRAINT product_ibfk_2 FOREIGN KEY ( supplierId ) REFERENCES `POS`.supplier( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.product_in_order ADD CONSTRAINT product_in_order_ibfk_1 FOREIGN KEY ( orderId ) REFERENCES `POS`.`order`( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `POS`.product_in_order ADD CONSTRAINT product_in_order_ibfk_2 FOREIGN KEY ( productId ) REFERENCES `POS`.product( id ) ON DELETE NO ACTION ON UPDATE NO ACTION;

DELIMITER //

CREATE TRIGGER pos.goods_import_AFTER_INSERT AFTER INSERT ON goods_import FOR EACH ROW
BEGIN
    DECLARE product_count INT;

    SELECT COUNT(*) INTO product_count FROM product WHERE id = NEW.productId;

    IF product_count = 0 THEN
        INSERT INTO product (`id`, `quantityInStock`) VALUES (NEW.productId, NEW.quantity);
    ELSE
        UPDATE product SET `quantityInStock` = `quantityInStock` + NEW.quantity WHERE id = NEW.productId;
    END IF;
END;
//

DELIMITER ;


DELIMITER //

CREATE TRIGGER pos.product_in_order_AFTER_DELETE AFTER DELETE ON product_in_order FOR EACH ROW
BEGIN
    DECLARE quantity_to_add INT;

    SELECT `quantity` INTO quantity_to_add FROM product_in_order WHERE productId = OLD.productId;

    UPDATE product SET `amount` = `amount` + quantity_to_add WHERE id = OLD.productId;
END;
//

DELIMITER ;


DELIMITER //

CREATE TRIGGER pos.product_in_order_AFTER_INSERT AFTER INSERT ON product_in_order FOR EACH ROW
BEGIN
    DECLARE quantity_to_subtract INT;

    SELECT `quantity` INTO quantity_to_subtract FROM product_in_order WHERE productId = NEW.productId;

    UPDATE product SET `amount` = `amount` - quantity_to_subtract WHERE id = NEW.productId;
END;
//

DELIMITER ;