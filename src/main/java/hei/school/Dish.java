package hei.school;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double price; // prix de vente 
    private List<Ingredient> ingredients = new ArrayList<>();

    public Dish(Integer id, String name, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    public Double getDishCost() {
        return ingredients.stream().mapToDouble(Ingredient::getPrice).sum();
    }

    public Double getGrossMargin() {
        if (this.price == null) {
            throw new RuntimeException("Prix de vente non fixé pour le plat : " + this.name);
        }
        return this.price - getDishCost();
    }

    public DishTypeEnum getDishType() { return dishType; }
    public String getName() { return name; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public void setPrice(Double price) { this.price = price; }

    public int getId() {
        return id;
    }
}
