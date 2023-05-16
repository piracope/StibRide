package data.dto;

public class StationsDto extends Dto<Integer> {
    /**
     * The Station's name.
     */
    private final String name;

    /**
     * Creates a new instance of <code>StationsDto</code>.
     *
     * @param key  key of the data.
     * @param name the station's name
     */
    public StationsDto(Integer key, String name) {
        super(key);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
