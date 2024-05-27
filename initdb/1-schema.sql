CREATE TABLE country (
    country_id SERIAL PRIMARY KEY,
    country_iso_code VARCHAR,
    country_name VARCHAR
);

CREATE TABLE department (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR
);

CREATE TABLE gender (
    gender_id INTEGER PRIMARY KEY,
    gender VARCHAR
);

CREATE TABLE genre (
    genre_id INTEGER PRIMARY KEY,
    genre_name VARCHAR
);

CREATE TABLE keyword (
    keyword_id INTEGER PRIMARY KEY,
    keyword_name VARCHAR
);

CREATE TABLE language (
    language_id SERIAL PRIMARY KEY,
    language_code VARCHAR,
    language_name VARCHAR
);

CREATE TABLE language_role (
    role_id INTEGER PRIMARY KEY,
    language_role VARCHAR
);

CREATE TABLE movie (
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR,
    budget INTEGER,
    homepage VARCHAR,
    overview VARCHAR,
    popularity DECIMAL(12,6),
    release_date DATE,
    revenue BIGINT,
    runtime INTEGER,
    movie_status VARCHAR,
    tagline VARCHAR,
    vote_average DECIMAL(4,2),
    vote_count INTEGER
);

CREATE TABLE person (
    person_id SERIAL PRIMARY KEY,
    person_name VARCHAR
);

CREATE TABLE production_company (
    company_id SERIAL PRIMARY KEY,
    company_name VARCHAR
);

CREATE TABLE movie_cast (
    movie_id INTEGER REFERENCES movie(movie_id),
    person_id INTEGER REFERENCES person(person_id),
    gender_id INTEGER REFERENCES gender(gender_id),
    character_name VARCHAR,
    cast_order INTEGER
);

CREATE TABLE movie_company (
    movie_id INTEGER REFERENCES movie(movie_id),
    company_id INTEGER REFERENCES production_company(company_id)
);

CREATE TABLE movie_crew (
    movie_id INTEGER REFERENCES movie(movie_id),
    person_id INTEGER REFERENCES person(person_id),
    department_id INTEGER REFERENCES department(department_id),
    job VARCHAR
);

CREATE TABLE movie_genres (
    movie_id INTEGER REFERENCES movie(movie_id),
    genre_id INTEGER REFERENCES genre(genre_id)
);

CREATE TABLE movie_keywords (
    movie_id INTEGER REFERENCES movie(movie_id),
    keyword_id INTEGER REFERENCES keyword(keyword_id)
);

CREATE TABLE movie_languages (
    movie_id INTEGER REFERENCES movie(movie_id),
    language_id INTEGER REFERENCES language(language_id),
    language_role_id INTEGER REFERENCES language_role(role_id)
);

CREATE TABLE production_country (
    movie_id INTEGER REFERENCES movie(movie_id),
    country_id INTEGER REFERENCES country(country_id)
);
