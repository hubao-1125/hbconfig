create table if not exists `configs` (
    `app` varchar(64) not null,
    `env` varchar(64) not null,
    `ns` varchar(64) not null,
    `pkey` varchar(64) not null,
    `pvalue` varchar(128) null
);

insert into `configs` (`app`, `env`, `ns`, `pkey`, `pvalue`) values ('app1', 'dev', 'public', 'hb.a', 'dev100');
insert into `configs` (`app`, `env`, `ns`, `pkey`, `pvalue`) values ('app1', 'dev', 'public', 'hb.b', 'http://localhost:9192');
insert into `configs` (`app`, `env`, `ns`, `pkey`, `pvalue`) values ('app1', 'dev', 'public', 'hb.c', 'cc100');