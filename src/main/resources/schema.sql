CREATE TYPE category_enum AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TYPE category_enum AS ENUM ('START', 'MAIN', 'DESSERT');

CREATE TABLE Dish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dish_type dish_type_enum NOT NULL
);

CREATE TABLE Indredient(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category category_enum NOT NULL,
    id_dish INT,
    CONSTRAINT fk_dish FOREIGN KEY (id_dish) REFERENCES Dish(id) ON DELETE SET NULL
),