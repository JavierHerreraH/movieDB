SELECT setval('country_country_id_seq', (SELECT MAX(country_id) FROM country));
SELECT setval('department_department_id_seq', (SELECT MAX(department_id) FROM department));
SELECT setval('language_language_id_seq', (SELECT MAX(language_id) FROM language));
SELECT setval('movie_movie_id_seq', (SELECT MAX(movie_id) FROM movie));
SELECT setval('person_person_id_seq', (SELECT MAX(person_id) FROM person));
SELECT setval('production_company_company_id_seq', (SELECT MAX(company_id) FROM production_company));
