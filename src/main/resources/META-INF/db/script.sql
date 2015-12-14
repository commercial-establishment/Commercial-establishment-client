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
INSERT INTO category (id, name) VALUES (2, 'НеНапитки');

/*TODO change types*/
INSERT INTO type (id, name) VALUES (1, 'A');
INSERT INTO type (id, name) VALUES (2, 'B');

INSERT INTO unit (id, name, symbol) VALUES (1, 'Килограмм', 'кг');
INSERT INTO unit (id, name, symbol) VALUES (2, 'Литр', 'л');

INSERT INTO product (id, BARCODE, is_blocked, name, category_id, UNIT_ID)
VALUES (1, 123123123123, FALSE, 'Coca-Cola', 1, 1);
INSERT INTO product (id, BARCODE, is_blocked, name, category_id, UNIT_ID)
VALUES (2, 321321321321, FALSE, 'NeCoca-Cola', 2, 2);
INSERT INTO product (id, BARCODE, is_blocked, name, category_id, UNIT_ID)
VALUES (3, 333222111222, FALSE, 'Water', 1, 1);

INSERT INTO shop (id, address, is_blocked, name, city_id, type_id, IIN) VALUES (1, 'Туркистан 8/2', FALSE, 'Gal Mart', 1, 1, 123456789);

INSERT INTO WAREHOUSE (id, shop_id) VALUES (1, 1);

INSERT INTO warehouse_product (ID, ARRIVAL, RESIDUE, PRODUCT_ID, WAREHOUSE_ID, VERSION, INITIAL_PRICE, FINAL_PRICE, VAT, MARGIN)
VALUES (1, 100, 50, 1, 1, 1, 220, 220, TRUE, 20);
INSERT INTO warehouse_product (ID, ARRIVAL, RESIDUE, PRODUCT_ID, WAREHOUSE_ID, VERSION, INITIAL_PRICE, FINAL_PRICE, VAT, MARGIN)
VALUES (3, 30, 25, 3, 1, 1, 220, 220, TRUE, 20);
INSERT INTO warehouse_product (ID, ARRIVAL, RESIDUE, PRODUCT_ID, WAREHOUSE_ID, VERSION, INITIAL_PRICE, FINAL_PRICE, VAT, MARGIN)
VALUES (2, 100, 50, 2, 1, 1, 170, 220, TRUE, 20);

INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id, iin, bin)
VALUES (1, 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME', 'PERSON NAME', 'yakov@gmail.com', NULL,
        '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
        'username', 1, 2, 123456789, 987654321);
INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id, iin, bin)
VALUES (2, 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME TWO', 'PERSON NAME', 'yakov@gmail.com', NULL,
        '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
        'username', 1, 2, 123456789, 987654321);
INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id, iin, bin)
VALUES (3, 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME THREE', 'PERSON NAME', 'yakov@gmail.com', NULL,
        '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
        'username', 1, 2, 123456789, 987654321);

INSERT INTO product_provider (id, is_blocked, product_id, provider_id) VALUES (1, FALSE, 1, 1);
INSERT INTO product_provider (id, is_blocked, product_id, provider_id) VALUES (2, FALSE, 2, 1);
INSERT INTO product_provider (id, is_blocked, product_id, provider_id) VALUES (3, FALSE, 3, 1);

INSERT INTO EMPLOYEE (ID, IS_BLOCKED, FIRST_NAME, PASSWORD, SURNAME, USERNAME, ROLE_ID, SHOP_ID) VALUES
  (1, FALSE, 'FIRSTNAME', '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', 'surname', 'owner', 3, 1);

INSERT INTO SHOP_PROVIDER (ID, IS_BLOCKED, PROVIDER_ID, SHOP_ID) VALUES (1, FALSE, 1, 1);

