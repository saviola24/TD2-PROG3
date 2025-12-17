package hei.school;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        DataRetriever retriever = new DataRetriever(db);

        List<Ingredient> ingredients = retriever.findIngredients(1, 10);

        if (ingredients.isEmpty()) {
            System.out.println("La base est connectée mais la table est vide !");
        } else {
            System.out.println("Liste des ingrédients trouvés :");
            for (Ingredient ing : ingredients) {
                System.out.println("- " + ing.getName() + " (" + ing.getPrice() + "€)");
            }
        }
    }
}