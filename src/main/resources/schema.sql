create table if not exists run (
  primary key(id),
  id int not null,
  title varchar(250) not null,
  started_on timestamp not null,
  completed_on timestamp not null,
  kilometers int not null,
  location varchar(10) not null
);