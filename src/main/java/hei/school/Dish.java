package hei.school;

import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishtype;
    private List<Ingredient> ingredients;

    public double getDishCost(){
        return  ingredients == null ? null : ingredients
                .stream().mapToDouble(Ingredient::getPrice).sum();
    }


    public Dish(Integer id, String name, DishTypeEnum dishtype) {
        this.id = id;
        this.name = name;
        this.dishtype = dishtype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishtype() {
        return dishtype;
    }

    public void setDishtype(DishTypeEnum dishtype) {
        this.dishtype = dishtype;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
