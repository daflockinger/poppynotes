insert into user (id, auth_email, crypt_key, name, pin_code, recovery_email, status, unlock_code) values(1, "flo@gmail.com", "qewr8762y7bcvn65kjhl76klh7343", "flo", "9999", "flo@gmx.net", "ACTIVE", null);
insert into user_roles (user_id, roles) values (1, "ADMIN");
insert into user_roles (user_id, roles) values (1, "AUTHOR");
insert into user (id, auth_email, crypt_key, name, pin_code, recovery_email, status, unlock_code) values(2, "sep@gmail.com", "89768iksgd", "sepp", "4321", "sep@gmx.net", "ACTIVE", null);
insert into user_roles (user_id, roles) values (2, "AUTHOR");
insert into user (id, auth_email, crypt_key, name, pin_code, recovery_email, status, unlock_code) values(3, "hons@gmail.com", "kjhs89j3", "hons", null, "hons@gmx.net", "ACTIVE", null);
insert into user_roles (user_id, roles) values (3, "AUTHOR");
insert into user (id, auth_email, crypt_key, name, pin_code, recovery_email, status, unlock_code) values(4, "depp@gmail.com", "32dfghdp", "depp", "0000", "depp@gmx.net", "LOCKED", "s887q");
insert into user_roles (user_id, roles) values (4, "AUTHOR");