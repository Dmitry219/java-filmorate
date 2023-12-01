DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Friendship CASCADE;
DROP TABLE IF EXISTS MPA CASCADE;
DROP TABLE IF EXISTS Genres CASCADE;
DROP TABLE IF EXISTS Films CASCADE;
DROP TABLE IF EXISTS Likes CASCADE;
DROP TABLE IF EXISTS Genres_Film CASCADE;
DROP TABLE IF EXISTS Directors CASCADE;
DROP TABLE IF EXISTS Directors_Film CASCADE;
DROP TABLE IF EXISTS Review CASCADE;
DROP TABLE IF EXISTS review_likes CASCADE;

CREATE TABLE Users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(40) NOT NULL,
    login varchar(40) NOT NULL,
    name varchar(40) NOT NULL,
    birthday date
);

CREATE TABLE Friendship (
    id_User INTEGER REFERENCES Users(id) ON DELETE CASCADE,
    id_Friend INTEGER REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE MPA (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE Directors (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE Genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL
);

CREATE TABLE Films (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL,
    description varchar(40) NOT NULL,
    release_Date date,
    duration INTEGER,
    id_MPA INTEGER REFERENCES MPA(id)
);

CREATE TABLE Likes (
    id_Film INTEGER REFERENCES Films(id) ON DELETE CASCADE,
    id_User INTEGER REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE Genres_Film (
    id_Film INTEGER REFERENCES Films(id) ON DELETE CASCADE,
    id_Genre INTEGER REFERENCES Genres(id) ON DELETE CASCADE
);

CREATE TABLE Directors_Film (
    id_Film INTEGER REFERENCES Films(id),
    id_Director INTEGER REFERENCES Directors(id) ON DELETE CASCADE
);

INSERT INTO Genres (name) VALUES ('Комедия');
INSERT INTO Genres (name) VALUES ('Драма');
INSERT INTO Genres (name) VALUES ('Мультфильм');
INSERT INTO Genres (name) VALUES ('Триллер');
INSERT INTO Genres (name) VALUES ('Документальный');
INSERT INTO Genres (name) VALUES ('Боевик');

INSERT INTO MPA (name) VALUES ('G');
INSERT INTO MPA (name) VALUES ('PG');
INSERT INTO MPA (name) VALUES ('PG-13');
INSERT INTO MPA (name) VALUES ('R');
INSERT INTO MPA (name) VALUES ('NC-17');
