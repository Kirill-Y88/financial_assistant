drop table  if exists indicators;
create table indicators (id bigserial primary key, id_indices long REFERENCES indices(id), RSI float, MACD float, MACDG float, GARCH float);
commit;