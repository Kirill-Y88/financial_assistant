drop table  if exists companies;
create table companies (id bigserial primary key, em long, name VARCHAR,  code VARCHAR);
insert into companies (em, name,  code) values
                (2718374,	'Huntington',	'MOEX.HBAN-RM:FQBR'),
                (2216506,	'АСКО ао',	    'MOEX.ACKO:TQBR');
commit;






