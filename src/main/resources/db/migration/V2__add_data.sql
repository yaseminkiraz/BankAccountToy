INSERT INTO test.account
(balance, customer_number, iban, is_withdrawable, is_locked, "type")
VALUES(1000, 1, uuid_generate_v4() , true, false, 'CHECKING');

INSERT INTO test.account
(balance, customer_number, iban, is_withdrawable, is_locked, "type")
VALUES(1000, 1, uuid_generate_v4() , true, false, 'SAVINGS');

INSERT INTO test.account
(balance, customer_number, iban, is_withdrawable, is_locked, "type")
VALUES(1000, 1,uuid_generate_v4() , false, false, 'PRIVATE_LOAN');
