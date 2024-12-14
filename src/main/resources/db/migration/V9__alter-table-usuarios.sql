alter table usuarios
add column token varchar(64),
add column expiracao_token timestamp;