INSERT INTO role (id, name) VALUES ('00000000-0000-0001-0000-000000000000', 'ADMIN');
INSERT INTO role (id, name) VALUES ('00000000-0000-0002-0000-000000000000', 'PROVIDER');
INSERT INTO role (id, name) VALUES ('00000000-0000-0003-0000-000000000000', 'OWNER');
INSERT INTO role (id, name) VALUES ('00000000-0000-0004-0000-000000000000', 'ACCOUNTANT');
INSERT INTO role (id, name) VALUES ('00000000-0000-0005-0000-000000000000', 'SELLER');

INSERT INTO gender (id, name) VALUES ('00000000-0000-0001-0000-000000000000', 'MALE');
INSERT INTO gender (id, name) VALUES ('00000000-0000-0002-0000-000000000000', 'FEMALE');

INSERT INTO city (id, name) VALUES ('00000000-0000-0001-0000-000000000000', 'АСТАНА');
INSERT INTO city (id, name) VALUES ('00000000-0000-0002-0000-000000000000', 'КАРАГАНДА');

INSERT INTO area (id, name, city_id)
VALUES ('00000000-0000-0001-0000-000000000000', 'Есильский', '00000000-0000-0001-0000-000000000000');
INSERT INTO area (id, name, city_id)
VALUES ('00000000-0000-0002-0000-000000000000', 'Алматинский', '00000000-0000-0001-0000-000000000000');
INSERT INTO area (id, name, city_id)
VALUES ('00000000-0000-0003-0000-000000000000', 'Сарыаркинский', '00000000-0000-0001-0000-000000000000');

INSERT INTO area (id, name, city_id)
VALUES ('00000000-0000-0004-0000-000000000000', 'Офигенный', '00000000-0000-0002-0000-000000000000');

INSERT INTO category (id, name) VALUES ('00000000-0000-0001-0000-000000000000', 'Напитки');
INSERT INTO category (id, name) VALUES ('00000000-0000-0002-0000-000000000000', 'Алкоголь');
INSERT INTO category (id, name) VALUES ('00000000-0000-0003-0000-000000000000', 'Конфеты');

/*TODO change types*/
INSERT INTO type (id, name) VALUES ('00000000-0000-0001-0000-000000000000', 'A');
INSERT INTO type (id, name) VALUES ('00000000-0000-0002-0000-000000000000', 'B');
INSERT INTO type (id, name) VALUES ('00000000-0000-0003-0000-000000000000', 'C');

INSERT INTO unit (id, name, symbol) VALUES ('0000000000000001', 'Килограмм', 'кг');
INSERT INTO unit (id, name, symbol) VALUES ('0000000000000002', 'Литр', 'л');
INSERT INTO unit (id, name, symbol) VALUES ('0000000000000003', 'Штука', 'шт');

-- INSERT INTO product (id, BARCODE, is_blocked, name, category_id, UNIT_ID)
-- VALUES ('0000000000000001', 123123123123, FALSE, 'Coca-Cola', '0000000000000001', '0000000000000001');
-- INSERT INTO product (id, BARCODE, is_blocked, name, category_id, UNIT_ID)
-- VALUES ('0000000000000002', 321321321321, FALSE, 'NeCoca-Cola', '0000000000000002', '0000000000000002');
-- INSERT INTO product (id, BARCODE, is_blocked, name, category_id, UNIT_ID)
-- VALUES ('0000000000000003', 333222111222, FALSE, 'Water', '0000000000000001', '0000000000000001');

INSERT INTO shop (id, address, is_blocked, name, AREA_ID, type_id, IIN)
VALUES ('0000000000000001', 'Туркистан 8/2', FALSE, 'Gal Mart', '0000000000000001', '0000000000000001', 123456789);

INSERT INTO WAREHOUSE (id, shop_id) VALUES ('0000000000000001', '0000000000000001');

-- INSERT INTO warehouse_product (ID, RESIDUE, PRODUCT_ID, WAREHOUSE_ID, VERSION, INITIAL_PRICE, FINAL_PRICE, VAT, MARGIN)
-- VALUES (1, 50, 1, 1, 1, 220, 220, TRUE, 20);
-- INSERT INTO warehouse_product (ID, RESIDUE, PRODUCT_ID, WAREHOUSE_ID, VERSION, INITIAL_PRICE, FINAL_PRICE, VAT, MARGIN)
-- VALUES (3, 25, 3, 1, 1, 220, 220, TRUE, 20);
-- INSERT INTO warehouse_product (ID, RESIDUE, PRODUCT_ID, WAREHOUSE_ID, VERSION, INITIAL_PRICE, FINAL_PRICE, VAT, MARGIN)
-- VALUES (2, 50, 2, 1, 1, 170, 220, TRUE, 20);

-- INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id, iin, bin)
-- VALUES ('0000000000000001', 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME', 'PERSON NAME', 'yakov@gmail.com', NULL,
--                  '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
--                  'username', '0000000000000001', '0000000000000002', 123456789, 987654321);
-- INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id, iin, bin)
-- VALUES ('0000000000000002', 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME TWO', 'PERSON NAME', 'yakov@gmail.com', NULL,
--                  '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
--                  'username', '0000000000000001', '0000000000000002', 123456789, 987654321);
-- INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id, iin, bin)
-- VALUES ('0000000000000003', 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME THREE', 'PERSON NAME', 'yakov@gmail.com', NULL,
--                  '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '2012-09-17 18:47:52.69',
--                  'username', '0000000000000001', '0000000000000002', 123456789, 987654321);

-- INSERT INTO product_provider (id, is_blocked, product_id, provider_id) VALUES (1, FALSE, 1, 1);
-- INSERT INTO product_provider (id, is_blocked, product_id, provider_id) VALUES (2, FALSE, 2, 1);
-- INSERT INTO product_provider (id, is_blocked, product_id, provider_id) VALUES (3, FALSE, 3, 1);

INSERT INTO EMPLOYEE (ID, IS_BLOCKED, FIRST_NAME, PASSWORD, SURNAME, USERNAME, ROLE_ID, SHOP_ID) VALUES
  ('0000000000000001', FALSE, 'FIRSTNAME', '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', 'surname', 'owner',
   '0000000000000003', '0000000000000001');
--
-- INSERT INTO SHOP_PROVIDER (ID, IS_BLOCKED, PROVIDER_ID, SHOP_ID)
-- VALUES ('0000000000000001', FALSE, '0000000000000002', '0000000000000001');

