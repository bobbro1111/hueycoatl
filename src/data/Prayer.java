package data;



public enum Prayer {

    Prayer(2434, "Prayer potion(4)"),
    Super(3024, "Super restore(4)");

    private final int id;
    private final String name;

    Prayer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}