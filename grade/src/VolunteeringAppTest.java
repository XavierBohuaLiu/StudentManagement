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
        // 添加一些测试数据
        tableModel.addRow(new Object[]{"1", "Work 1", "10 hours", "5 credits"});
        tableModel.addRow(new Object[]{"2", "Work 2", "8 hours", "4 credits"});
        tableModel.addRow(new Object[]{"3", "Work 3", "6 hours", "3 credits"});

        // 调用 saveTableDataToFile 方法
        volunteeringApp.saveTableDataToFile(TEST_FILE_PATH);

        // 验证文件是否正确保存
        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH)));

        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH))) {
            // 验证每一行数据是否正确保存
            String line = reader.readLine();
            assertEquals("1,Work 1,10 hours,5 credits", line);

            line = reader.readLine();
            assertEquals("2,Work 2,8 hours,4 credits", line);

            line = reader.readLine();
            assertEquals("3,Work 3,6 hours,3 credits", line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 清理测试数据
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
        // 清空表格数据
        tableModel.setRowCount(0);
    }
}
