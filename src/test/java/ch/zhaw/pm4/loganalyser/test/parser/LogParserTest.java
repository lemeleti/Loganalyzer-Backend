package ch.zhaw.pm4.loganalyser.test.parser;

import ch.zhaw.pm4.loganalyser.model.log.LogConfig;
import ch.zhaw.pm4.loganalyser.model.log.LogService;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent;
import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import ch.zhaw.pm4.loganalyser.parser.LogParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogParserTest {

    private static final String LFOLDER = "logs";

    @Test
    void readTest() throws Exception {
        // prepare
        LogService serviceMock = mock(LogService.class);
        File logFolder = new File(LogParserTest.class.getClassLoader().getResource(LFOLDER).getPath());
        when(serviceMock.getLogDirectory()).thenReturn(logFolder.toString());
        when(serviceMock.getLogConfig()).thenReturn(getLogConfig());

        LogParser parser = new LogParser();

        // execute
        List<String[]> result = parser.read(new ArrayList<>(), serviceMock);

        // validate
        assertEquals("192.168.1.1", result.get(0)[0]);
        assertEquals(30, result.size());
        for (String[] line : result) {
            assertEquals(9, line.length);
        }
    }

    private LogConfig getLogConfig() {
        // register columns
        Map<Integer, ColumnComponent> columnComponentMap = new TreeMap<>();
        int i = 0;
        columnComponentMap.put(++i, createColumnComponent(9L, "Custom Seperator", ColumnType.CUSTOM, "-"));
        columnComponentMap.put(++i, createColumnComponent(2L, "User", ColumnType.CUSTOM, "-|[a-zA-Z]+"));
        columnComponentMap.put(++i, createColumnComponent(3L, "Timestamp", ColumnType.DATE, "\\[.+\\]"));
        columnComponentMap.put(++i, createColumnComponent(4L, "Request", ColumnType.MESSAGE, "\\\".+\\\""));
        columnComponentMap.put(++i, createColumnComponent(5L, "Response Code", ColumnType.CUSTOM, "\\d{1,3}"));
        columnComponentMap.put(++i, createColumnComponent(6L, "Byte Size", ColumnType.CUSTOM, "\\d+"));
        columnComponentMap.put(++i, createColumnComponent(7L, "Something", ColumnType.CUSTOM, "\\\".+\\\""));
        columnComponentMap.put(++i, createColumnComponent(8L, "Request Client", ColumnType.CUSTOM, "\\\".+\\\""));

        //register config
        LogConfig config = new LogConfig();
        config.setName("Nginx");
        config.setSeparator(" ");
        config.setHeaderLength(0);
        config.setColumnComponents(columnComponentMap);

        return config;
    }

    private ColumnComponent createColumnComponent(long id, String name, ColumnType type, String format) {
        return ColumnComponent.builder()
                .id(id)
                .name(name)
                .columnType(type)
                .format(format)
                .build();
    }

}
