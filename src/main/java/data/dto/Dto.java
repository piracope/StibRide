package data.dto;

import java.util.Objects;

/**
 * Generic class to build a Data transfer object. The data carried is known by
 * its key.
 *
 * @param <K> key of the data.
 * @author jlc
 * @see <a href="https://en.wikipedia.org/wiki/Data_transfer_object"> Wikipedia</a>
 */
public abstract class Dto<K> {

    /**
     * Key of the data.
     */
    protected K key;

    /**
     * Creates a new instance of <code>Dto</code> with the key of the data.
     *
     * @param key key of the data.
     */
    protected Dto(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Clé absente");
        }
        this.key = key;
    }

    /**
     * Returns the key of the data.
     *
     * @return the key of the data.
     */
    public K getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dto<?> dto = (Dto<?>) o;
        return key.equals(dto.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
