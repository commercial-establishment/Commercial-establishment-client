INSERT INTO role (id, name) VALUES (1, 'ADMIN');
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

INSERT INTO admin (id, is_blocked, email, end_work_date, name, password, patronymic, start_work_date, surname, username, role_id, gender_id)
VALUES
  (1, FALSE, 'yakovshmidt93@gmail.com', NULL, 'Yakov', '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa',
   'Vladimirovich', '11-11-2011', 'Shmidt', 'yakov11', 1, 1);

INSERT INTO product (id, is_blocked, name, category_id) VALUES (1, FALSE, 'Coca-Cola', 1);
INSERT INTO product (id, is_blocked, name, category_id) VALUES (2, FALSE, 'NeCoca-Cola', 1);

INSERT INTO warehouse (id, arrival, import_date, residue, produt_id) VALUES (1, 100, NULL, 50, 1);
INSERT INTO warehouse (id, arrival, import_date, residue, produt_id) VALUES (2, 100, NULL, 50, 2);

INSERT INTO shop (id, address, is_blocked, name, city_id, type_id) VALUES (1, 'Туркистан 8/2', false, 'Gal Mart', 1, 1);
/*TODO product's history*/

INSERT INTO provider (id, address, is_blocked, company_name, contact_person, email, end_work_date, password, start_work_date, username, city_id, role_id)
VALUES (1, 'ул. Ташенова 5/2', FALSE, 'COMPANY NAME', 'PERSON NAME', 'yakov@gmail.com', NULL,
        '$2a$10$5/0IiLwNrICc3Dmq/7AWKO08qK13AKH2tWIytGe9a2.WZHIj2WwPa', '11-11-20110',
        'username', 1, 2);

INSERT INTO product_provider (id, is_blocked, product_id, provider_id, amount, price) VALUES (1, FALSE, 1, 1, 10, 100);
INSERT INTO product_provider (id, is_blocked, product_id, provider_id, amount, price) VALUES (2, FALSE, 2, 1, 20, 200);

-- INSERT INTO shop (id, is_blocked, full_name, password, username, type_id, warehouse_id) VALUES (1, FALSE, 'Super Магаз', 'super', 'super', 1, 1);