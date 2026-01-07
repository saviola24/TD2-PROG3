package hei.school;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialisation
        DBConnection db = new DBConnection();
        DataRetriever retriever = new DataRetriever(db);

        try {
            System.out.println("--- DÉBUT DES TESTS DE VALIDATION ---");


            Dish dish1 = retriever.findDishById(1);
            System.out.println("7-a) Plat: " + dish1.getName() + " | Ingrédients: " + dish1.getIngredients().size());


            try {
                retriever.findDishById(999);
            } catch (RuntimeException e) {
                System.out.println("7-b) OK: Exception levée pour l'ID 999");
            }


            List<Ingredient> p2 = retriever.findIngredients(2, 2);
            System.out.println("7-c) Ingrédients p2: " + p2.get(0).getName() + ", " + p2.get(1).getName());


            List<Dish> dishesEur = retriever.findDishsByIngredientName("eur");
            System.out.println("7-e) Plat trouvé: " + dishesEur.get(0).getName());

            try {
                List<Ingredient> failList = List.of(
                        new Ingredient(null, "Carotte", 2000.0, CategoryEnum.VEGETABLE),
                        new Ingredient(null, "Laitue", 2000.0, CategoryEnum.VEGETABLE)
                );
                retriever.createIngredients(failList);
            } catch (RuntimeException e) {
                System.out.println("7-j) OK: Atomicité respectée, insertion annulée");
            }

            System.out.println("--- TOURS LES TESTS SONT PASSÉS ---");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}