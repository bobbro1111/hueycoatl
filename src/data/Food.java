package data;



public enum Food {

    Shark(385, "Shark"),
    Manta(391, "Manta ray"),
    Anglerfish(13441, "Anglerfish");

    private final int id;
    private final String name;

    Food(int id, String name) {
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