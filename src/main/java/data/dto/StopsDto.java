package data.dto;

public class StopsDto extends Dto<StopId> {

    /**
     * The order in the line.
     */
    private final int order;

    /**
     * Creates a new instance of <code>StopsDto</code>.
     * <p>
     * The key of the data is both lineId and stationId.
     *
     * @param lineId    the line in which this stop is
     * @param stationId the station on which this stop is
     * @param order     the order in the line this stop is
     */
    public StopsDto(int lineId, int stationId, int order) {
        super(new StopId(lineId, stationId));
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
