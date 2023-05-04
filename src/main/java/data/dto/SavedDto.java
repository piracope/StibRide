package data.dto;

public class SavedDto extends Dto<String> {
    private final int startId;
    private final int destId;

    public SavedDto(int startId, int destId, String name) {
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
