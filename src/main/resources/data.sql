INSERT INTO Dish (id, name, dish_type) VALUES
                                           (1, 'Salade Fraîche', 'START'),
                                           (2, 'Gâteau au chocolat', 'DESSERT');

INSERT INTO Ingredient (name, price, category, id_dish) VALUES
                                                            ('Laitue', 500, 'VEGETABLE', 1),
                                                            ('Tomate', 800, 'VEGETABLE', 1),
                                                            ('Poulet', 5000, 'ANIMAL', NULL),
                                                            ('Chocolat', 3000, 'OTHER', 2),
                                                            ('Beurre', 1500, 'DAIRY', 2);

UPDATE Dish SET price = 2000 WHERE name = 'Salade fraîche'; --
UPDATE Dish SET price = 6000 WHERE name = 'Poulet grillé';  --
UPDATE Dish SET price = NULL WHERE name = 'Riz au légume';   --
UPDATE Dish SET price = NULL WHERE name = 'Gâteau au chocolat'; --
UPDATE Dish SET price = NULL WHERE name = 'Salade de fruit'; --