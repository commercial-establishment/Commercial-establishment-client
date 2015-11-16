-- INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'PROVIDER');
INSERT INTO role (id, name) VALUES (3, 'OWNER');
INSERT INTO role (id, name) VALUES (4, 'ACCOUNTANT');
INSERT INTO role (id, name) VALUES (5, 'SELLER');

INSERT INTO gender (id, name) VALUES (1, 'MALE');
INSERT INTO gender (id, name) VALUES (2, 'FEMALE');

INSERT INTO city (id, name) VALUES (1, 'АСТАНА');
INSERT INTO city (id, name) VALUES (2, 'КАРАГАНДА');

INSERT INTO category (id, name) VALUES (1, 'Напитки');

/*TODO change types*/
INSERT INTO type (id, name) VALUES (1, 'A');
INSERT INTO type (id, name) VALUES (2, 'B');

INSERT INTO product (id, BARCODE, is_blocked, name, category_id) VALUES (1, 123123123123, FALSE, 'Coca-Cola', 1);
INSERT INTO product (id, BARCODE, is_blocked, name, category_id) VALUES (2, 321321321321, FALSE, 'NeCoca-Cola', 1);

INSERT INTO shop (id, address, is_blocked, name, city_id, type_id) VALUES (1, 'Туркистан 8/2', FALSE, 'Gal Mart', 1, 1);

INSERT INTO WAREHOUSE (id, shop_id) VALUES (1, 1);

-- INSERT INTO WAREHOUSE_PRODUCT (ID, ARRIVAL, IMPORT_DATE, INITIAL_PRICE, PRICE, RESIDUE, VERSION, PRODUCT_ID, WAREHOUSE_ID)
-- VALUES (1);

INSERT INTO warehouse_product (id, arrival, import_date, residue, product_id, WAREHOUSE_ID, VERSION, INITIAL_PRICE, PRICE)
VALUES (1, 100, NULL, 50, 1, 1, 1, 200, 220);
INSERT INTO warehouse_product (id, arrival, import_date, residue, product_id, WAREHOUSE_ID, VERSION, INITIAL_PRICE, PRICE)
VALUES (2, 100, NULL, 50, 2, 1, 1, 150, 170);


/*TODO product's history*/

INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id)
VALUES (1, 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME', 'PERSON NAME', 'yakov@gmail.com', NULL,
           '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
           'username', 1, 2);

INSERT INTO product_provider (id, is_blocked, product_id, provider_id, amount, price) VALUES (1, FALSE, 1, 1, 10, 100);
INSERT INTO product_provider (id, is_blocked, product_id, provider_id, amount, price) VALUES (2, FALSE, 2, 1, 20, 200);

INSERT INTO EMPLOYEE (ID, IS_BLOCKED, FIRST_NAME, PASSWORD, SURNAME, USERNAME, ROLE_ID, SHOP_ID) VALUES
  (1, FALSE, 'FIRSTNAME', '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', 'surname', 'owner', 3, 1);


