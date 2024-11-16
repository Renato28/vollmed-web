alter table consultas drop column paciente;

alter table consultas
add column paciente_id bigint not null,
add constraint fk_consultas_paciente_id foreign key(paciente_id) references pacientes(id);