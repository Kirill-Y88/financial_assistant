drop table  if exists indices;
create table indices (id bigserial primary key, id_company long REFERENCES companies(id), per long, date date, time time, price_open float, price_high float, price_low float, price_close float, vol long);
/*insert into indices (id_company, per, date, time, price_open, price_high, price_low, price_close, vol) VALUES
( 1, 60, 20210914,110000,112,117.6,110.2,111.3,226030),
(1,60,20210915,110000,109.8,110.2,109.3,109.6,9550),
(2,60,20210917, 190000, 106.5, 107, 106.5, 106.5, 8610);*/
commit;