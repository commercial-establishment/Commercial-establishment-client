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

INSERT INTO shop (id, address, is_blocked, name, AREA_ID, type_id, IIN)
VALUES ('0000000000000001', 'Туркистан 8/2', FALSE, 'Gal Mart', '0000000000000001', '0000000000000001', 123456789);

INSERT INTO WAREHOUSE (id, shop_id) VALUES ('0000000000000001', '0000000000000001');

INSERT INTO EMPLOYEE (ID, IS_BLOCKED, FIRST_NAME, PASSWORD, SURNAME, USERNAME, ROLE_ID, SHOP_ID) VALUES
  ('0000000000000001', FALSE, 'FIRSTNAME', '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', 'surname', 'owner',
   '0000000000000003', '0000000000000001');