package data.jdbc;

import data.config.ConfigManager;
import data.dto.StopId;
import data.dto.StopsDto;
import data.exception.RepositoryException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StopsDaoTest {
    private static final StopId KEY = new StopId(2, 8312);
    private final StopsDto PDN;
    private StopsDao instance;

    public StopsDaoTest() {
        PDN = new StopsDto(2, 8312, 11);
        try {
            ConfigManager.getInstance().load();
            instance = StopsDao.getInstance();
        } catch (RepositoryException e) {
            fail("Impossible de se connecter à la base de donner " + e);
        } catch (IOException e) {
            fail("Impossible d'ouvrir le fichier config");
        }
    }

    @Test
    public void testSelectExist() throws Exception {
        System.out.println("testSelectExist");
        //Arrange
        StopsDto expected = PDN;
        //Action
        StopsDto result = instance.select(KEY);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSelectNotExist() throws Exception {
        System.out.println("testSelectNotExist");
        //Arrange
        //Action
        StopsDto result = instance.select(new StopId(99, 999));
        //Assert
        assertNull(result);
    }

    @Test
    public void testSelectIncorrectParameter() throws Exception {
        System.out.println("testSelectIncorrectParameter");
        //Arrange
        StopId incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            instance.select(incorrect);
        });
    }
}