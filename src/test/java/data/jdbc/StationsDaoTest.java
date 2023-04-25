package data.jdbc;

import data.config.ConfigManager;
import data.dto.StationsDto;
import data.exception.RepositoryException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StationsDaoTest {

    private StationsDao instance;
    private static final int KEY = 8312;
    private final StationsDto PDN;

    public StationsDaoTest() {
        PDN = new StationsDto(8312, "PORTE DE NAMUR");

        try {
            ConfigManager.getInstance().load();
            instance = StationsDao.getInstance();
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
        StationsDto expected = PDN;
        //Action
        StationsDto result = instance.select(KEY);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSelectNotExist() throws Exception {
        System.out.println("testSelectNotExist");
        //Arrange
        //Action
        StationsDto result = instance.select(9999);
        //Assert
        assertNull(result);
    }

    @Test
    public void testSelectIncorrectParameter() throws Exception {
        System.out.println("testSelectIncorrectParameter");
        //Arrange
        Integer incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            instance.select(incorrect);
        });
    }

}