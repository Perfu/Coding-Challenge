CREATE TABLE account
(
  uuid uuid NOT NULL,
  country text,
  email text,
  name text,
  phone_number text,
  edition text NOT NULL,
  "maxUsers" integer NOT NULL,
  website text,
  CONSTRAINT account_pkey PRIMARY KEY (uuid)
)

CREATE SEQUENCE "USER_id_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  CREATE TABLE "user"
(
  id integer NOT NULL DEFAULT nextval('"USER_id_seq"'::regclass),
  account uuid NOT NULL,
  firstname text NOT NULL,
  language text NOT NULL,
  lastname text NOT NULL,
  openid text NOT NULL,
  uuid uuid,
  CONSTRAINT "USER_pkey" PRIMARY KEY (id),
  CONSTRAINT account_fk FOREIGN KEY (account)
      REFERENCES account (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)