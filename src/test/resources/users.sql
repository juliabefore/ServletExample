CREATE SCHEMA `servlet` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
CREATE TABLE `servlet`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `lastName` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `salary` INT(100) NULL,
  `dateOfBirth` DATE NOT NULL,
  PRIMARY KEY (`id`));

insert into servlet.users (lastName, firstName, salary, dateOfBirth)
values('LastName4', 'FirstName4', 400, date'2012-05-24');

insert into servlet.users (lastName, firstName, salary, dateOfBirth)
values('LastName5', 'FirstName5', 300, date'2012-05-25');

select * from users;