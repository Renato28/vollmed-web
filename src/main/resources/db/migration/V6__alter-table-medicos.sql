alter table consultas drop foreign key fk_consultas_medico_id;

alter table medicos modify id bigint not null;

alter table consultas
add constraint fk_consultas_medico_id
foreign key (medico_id) references medicos(id);