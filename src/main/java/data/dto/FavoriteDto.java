package data.dto;

public class FavoriteDto extends Dto<String> {
    /**
     * The ID of the starting stop.
     */
    private final int startId;
    /**
     * The ID of the final stop
     */
    private final int destId;

    /**
     * Creates a new Favorite with a given starting and final stops, and with a name.
     *
     * @param startId the starting stop's ID
     * @param destId  the final stop's ID
     * @param name    this favorite trip's name
     */
    public FavoriteDto(int startId, int destId, String name) {
        super(name);
        this.startId = startId;
        this.destId = destId;
    }

    public int getStartId() {
        return startId;
    }

    public int getDestId() {
        return destId;
    }
}
