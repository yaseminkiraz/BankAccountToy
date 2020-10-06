-- account definition
--DROP TABLE if exists test.account;

CREATE TABLE test.account (
	id bigserial NOT NULL,
	balance numeric(19,2) NOT NULL,
	customer_number int8 NOT NULL,
	iban uuid NOT NULL,
	is_withdrawable bool NOT NULL DEFAULT true,
	is_locked bool NOT NULL DEFAULT false,
	"type" varchar(255) NOT NULL,
	CONSTRAINT account_pkey PRIMARY KEY (id)
);

-- transaction_history definition
--DROP TABLE test.transaction_history;

CREATE TABLE test.transaction_history (
	id bigserial NOT NULL,
	amount numeric(19,2) NULL,
	from_account varchar(255) NULL,
	to_account varchar(255) NULL,
	history_type int4 NULL,
	transaction_time varchar(255) NULL,
	owner_account uuid NULL,
	CONSTRAINT transaction_history_pkey PRIMARY KEY (id)
);