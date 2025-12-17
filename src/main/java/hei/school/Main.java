package hei.school;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        DataRetriever retriever = new DataRetriever(db);

        List<Ingredient> ingredients = retriever.findIngredients(1,10);
    }
}