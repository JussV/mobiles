INSERT INTO CUSTOMER (id, first_name, last_name) VALUES (1000, 'Joana', 'Goshevska');
INSERT INTO CUSTOMER (id, first_name, last_name) VALUES (1001, 'John', 'Doe');
INSERT INTO CUSTOMER (id, first_name, last_name) VALUES (1002, 'Axl', 'Rose');

INSERT INTO MOBILE_SUBSCRIPTION (msisdn, customer_id_owner, customer_id_user, service_type, service_start_date)
  VALUES ('+35677141310', 1000, 1000, 'MOBILE_POSTPAID', DATEDIFF('SECOND', DATE '1970-01-01', CURRENT_TIMESTAMP()));
INSERT INTO MOBILE_SUBSCRIPTION (msisdn, customer_id_owner, customer_id_user, service_type, service_start_date)
  VALUES ('+35677143567', 1001, 1002, 'MOBILE_PREPAID', DATEDIFF('SECOND', DATE '1970-01-01', CURRENT_TIMESTAMP()));