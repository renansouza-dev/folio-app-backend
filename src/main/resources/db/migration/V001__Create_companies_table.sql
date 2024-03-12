CREATE TABLE companies (
  uuid UUID NOT NULL,
   name VARCHAR(85) NOT NULL,
   cnpj VARCHAR(14) NOT NULL,
   broker BOOLEAN NOT NULL,
   listed BOOLEAN NOT NULL,
   created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   modified TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   CONSTRAINT pk_companies PRIMARY KEY (uuid)
);

ALTER TABLE companies ADD CONSTRAINT UniqueCnpj UNIQUE (cnpj);

CREATE INDEX fn_index ON companies(cnpj);