package portfolio;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VolunteeringAppTest {
    private static final String TEST_FILE_PATH = "test_data.txt";
    private VolunteeringApp volunteeringApp;
    private DefaultTableModel tableModel;

    @Before
    public void setUp() {
        volunteeringApp = new VolunteeringApp();
        tableModel = volunteeringApp.getTableModel();
    }

    @Test
    public void testSaveTableDataToFile() {

        tableModel.addRow(new Object[]{"1", "Work 1", "10 hours", "5 credits"});
        tableModel.addRow(new Object[]{"2", "Work 2", "8 hours", "4 credits"});
        tableModel.addRow(new Object[]{"3", "Work 3", "6 hours", "3 credits"});


        volunteeringApp.saveTableDataToFile(TEST_FILE_PATH);


        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH)));

        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH))) {

            String line = reader.readLine();
            assertEquals("1,Work 1,10 hours,5 credits", line);

            line = reader.readLine();
            assertEquals("2,Work 2,8 hours,4 credits", line);

            line = reader.readLine();
            assertEquals("3,Work 3,6 hours,3 credits", line);
        } catch (IOException e) {
            e.printStackTrace();
        }


        cleanupTestData();
    }

    private void cleanupTestData() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {

        tableModel.setRowCount(0);
    }
}
