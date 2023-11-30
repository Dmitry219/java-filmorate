DROP TABLE Users IF EXISTS CASCADE;
DROP TABLE Friendship IF EXISTS CASCADE;
DROP TABLE MPA IF EXISTS CASCADE;
DROP TABLE Genres IF EXISTS CASCADE;
DROP TABLE Films IF EXISTS CASCADE;
DROP TABLE Likes IF EXISTS CASCADE;
DROP TABLE Genres_Film IF EXISTS CASCADE;
DROP TABLE Directors IF EXISTS CASCADE;
DROP TABLE Directors_Film IF EXISTS CASCADE;

CREATE TABLE Users (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        email varchar(40) NOT NULL,
        login varchar(40) NOT NULL,
        name varchar(40) NOT NULL,
        birthday date
);

CREATE TABLE Friendship (
        id_User INTEGER REFERENCES Users(id),
        id_Friend INTEGER REFERENCES Users(id)
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
        id_Film INTEGER REFERENCES Films(id),
        id_User INTEGER REFERENCES Users(id)
);

CREATE TABLE Genres_Film (
        id_Film INTEGER REFERENCES Films(id),
        id_Genre INTEGER REFERENCES Genres(id)
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










