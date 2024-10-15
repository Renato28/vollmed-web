create table consultas(

    id bigint not null auto_increment,
    medico_id bigint not null,
    paciente varchar(100) not null,
    data datetime not null,

    primary key(id),
    constraint fk_consultas_medico_id foreign key(medico_id) references medicos(id)

);