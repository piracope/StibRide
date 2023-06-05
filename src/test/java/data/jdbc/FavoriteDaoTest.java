package data.jdbc;

import data.config.ConfigManager;
import data.dto.FavoriteDto;
import data.exception.RepositoryException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteDaoTest {
    private static final String KEY = "TEST";

    private final FavoriteDto TEST;
    private final FavoriteDto TEST2;
    private final List<FavoriteDto> all;
    private FavoriteDao instance;

    public FavoriteDaoTest() {
        TEST = new FavoriteDto(8312, 8142, "TEST");
        TEST2 = new FavoriteDto(8302, 8472, "TEST2");

        all = new ArrayList<>();
        all.add(TEST);
        all.add(TEST2);

        try {
            ConfigManager.getInstance().load();
            instance = FavoriteDao.getInstance();
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
        FavoriteDto expected = TEST;
        //Action
        FavoriteDto result = instance.select(KEY);
        //Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSelectNotExist() throws Exception {
        System.out.println("testSelectNotExist");
        //Arrange
        //Action
        FavoriteDto result = instance.select("non");
        //Assert
        assertNull(result);
    }

    @Test
    public void testSelectIncorrectParameter() throws Exception {
        System.out.println("testSelectIncorrectParameter");
        //Arrange
        String incorrect = null;
        //Assert
        assertThrows(RepositoryException.class, () -> {
            //Action
            instance.select(incorrect);
        });
    }

    @Test
    public void testInsertCorrect() {
        System.out.println("testInsertCorrect");
        String key = "BONJOUR";
        FavoriteDto expectedDto = new FavoriteDto(8122, 8372, "BONJOUR");
        instance.insert(expectedDto);
        //assertEquals(expected, result);
        assertEquals(expectedDto, instance.select(key));
    }

}