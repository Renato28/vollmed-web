alter table consultas drop foreign key fk_consultas_paciente_id;

alter table pacientes modify id bigint not null;

alter table consultas add constraint fk_consultas_paciente_id

foreign key (paciente_id) references pacientes(id);