package hei.school;

public class Ingredient {
    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private Integer idDish;

    public Ingredient(Integer id, String name, double price, CategoryEnum category, Integer idDish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.idDish = idDish;
    }

    public String getDishName() {
        throw new RuntimeException("Not implemented");
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Integer getIdDish() {
        return idDish;
    }

    public void setIdDish(Integer idDish) {
        this.idDish = idDish;
    }
}
