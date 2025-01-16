import java.util.UUID;

public class Item {
    private final String id;
    private final String name;

    public Item(final String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
