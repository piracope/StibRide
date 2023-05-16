package data.dto;

/**
 * Creates a new ID for stops.
 *
 * @param lineId    one of the stop's line's ID
 * @param stationId the station's ID
 */
public record StopId(int lineId, int stationId) {
}
