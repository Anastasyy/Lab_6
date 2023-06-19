--1
CREATE TABLE IF NOT EXISTS  University(
                                          PRIMARY KEY (ID),
                                          ID INT NOT NULL ,
                                          name VARCHAR(45) NOT NULL
);

INSERT INTO University(id,name)
VALUES (1,'Йельский');

--2
CREATE TABLE IF NOT EXISTS  GroupResearher(
                                              PRIMARY KEY (ID),
                                              ID INT NOT NULL ,
                                              name VARCHAR(45) NOT NULL,
                                              manager VARCHAR(45) NOT NULL
);
INSERT INTO GroupResearher(id,name,manager)
VALUES (1,'Группа по млекопитающим','Джон Остроном');

INSERT INTO GroupResearher(id,name,manager)
VALUES (2,'Группа по пресмыкающимся','Роберт Бэккер');
--3
CREATE TABLE IF NOT EXISTS Researh(
                                      PRIMARY KEY (ID),
                                      name VARCHAR(45) NOT NULL ,
                                      ID INT NOT NULL ,
                                      University_ID INT REFERENCES University(ID),
                                      GroupResearcher_ID INT REFERENCES GroupResearher(ID)
);

INSERT INTO Researh(id,name,university_id,groupresearcher_id)
VALUES (1,'Леонид Яковлев',1,2);

INSERT INTO Researh(id,name,university_id,groupresearcher_id)
VALUES (2,'Анна Покровская',1,1);
--4
CREATE TABLE IF NOT EXISTS TypeAnimal(
                                         PRIMARY KEY (ID),
                                         name VARCHAR(45) NOT NULL ,
                                         termoregulation BOOLEAN NOT NULL ,
                                         ID INT NOT NULL
);
INSERT INTO TypeAnimal(id,name,termoregulation)
VALUES (1,'Млекопитающие', TRUE );
INSERT INTO TypeAnimal(id,name,termoregulation)
VALUES (2,'Пресмыкающиеся', FALSE );

--5
CREATE TABLE IF NOT EXISTS Animal(
                                     PRIMARY KEY (ID),
                                     description VARCHAR(45) NOT NULL ,
                                     name VARCHAR(45) NOT NULL ,
                                     ID INT NOT NULL ,
                                     TypeAnimal_ID INT REFERENCES TypeAnimal(ID)
);

INSERT INTO Animal(id, description,name,typeanimal_id)
VALUES (1,'Неповоротливыe','Динозавры',1);

INSERT INTO Animal(id, description,name,typeanimal_id)
VALUES (2,'Быстыре','Рептилии',2);

INSERT INTO Animal(id, description,name,typeanimal_id)
VALUES (3,'Xолоднокровные','Динозавры',1);


--6
CREATE TYPE EnumResolutionMethod as ENUM('inductive','diductive');
CREATE TABLE IF NOT EXISTS Resolution(
                                         PRIMARY KEY (ID),
                                         ID INT NOT NULL,
                                         Animal_id INT REFERENCES Animal(id),
                                         resolutionMethod EnumResolutionMethod

);


INSERT INTO Resolution(id, animal_id,resolutionmethod)
VALUES (1,1,'inductive');
INSERT INTO Resolution(id, animal_id,resolutionmethod)
VALUES (2,1,'diductive');





--7
CREATE TABLE IF NOT EXISTS Resolution_Research(
                                                  PRIMARY KEY (ID),
                                                  ID INT NOT NULL ,
                                                  Resolution_ID INT REFERENCES Resolution(ID),
                                                  Researcher_ID INT REFERENCES Researh(ID)
);

INSERT INTO Resolution_Research(id, resolution_id, researcher_id)
VALUES (1,1,1);

INSERT INTO Resolution_Research(id, resolution_id, researcher_id)
VALUES (2,1,1);

INSERT INTO Resolution_Research(id, resolution_id, researcher_id)
VALUES (3,1,2);


