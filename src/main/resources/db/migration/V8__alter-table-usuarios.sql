alter table usuarios
add column perfil ENUM('ATENDENTE', 'MEDICO', 'PACIENTE')
NOT NULL;