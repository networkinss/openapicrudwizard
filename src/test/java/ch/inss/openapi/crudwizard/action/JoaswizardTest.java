package ch.inss.openapi.crudwizard.action;

import ch.inss.openapi.joaswizard.Constants;
import ch.inss.openapi.joaswizard.InputParameter;
import ch.inss.openapi.joaswizard.Joaswizard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JoaswizardTest implements Constants {


    @BeforeAll
    static void initialize() {
        File outputFolder = new File("output" + sep);
        if (outputFolder.isDirectory() == false) {
            outputFolder.mkdir();
        }
    }

    @Test
    void testVersion() throws Exception {
        ch.inss.openapi.joaswizard.Main.version();
    }

    @Test
    void testExcelFromInputStream() throws Exception {
        InputParameter inputParameter = new InputParameter();
        inputParameter.setResourceId("Id");
        inputParameter.addMethods("get");
        FileInputStream fileInputStream = new FileInputStream("src/test/testData/objectimport.xlsx");
        inputParameter.setInputStream(fileInputStream);
        Joaswizard jo = new Joaswizard();
        String fromExcelInputstreamToString = jo.createFromExcelInputstreamToString(inputParameter);

        String[] outArr = fromExcelInputstreamToString.split("\n");
        final List<String> allLines = Arrays.asList(outArr).stream().map(String::trim).collect(Collectors.toList());
        final List<String> checkList = new ArrayList<>();
        checkList.add("description: OpenAPI specification generated by Joaswizard (https://github.com/networkinss/joaswizard).");
        checkList.add("email: john.doe@example.com");
        checkList.add("operationId: findInvoiceById");
        checkList.add("operationId: getInvoiceList");
        boolean ok = false;
        try (Stream<String> lines = checkList.stream()) {
            ok = lines.allMatch(l -> allLines.contains(l));
        }
        if (ok == false) System.out.println(fromExcelInputstreamToString);
        assertTrue(ok);
    }

    @Test
    void testExcelFromFile() throws Exception {
        InputParameter inputParameter = new InputParameter();
        inputParameter.setResourceId("Id");
        inputParameter.addMethods("get");
        inputParameter.setInputFile("src/test/testData/objectimport.xlsx");
        Joaswizard jo = new Joaswizard();
        String fromExcelInputstreamToString = jo.createFromExcelfileToString(inputParameter);

        String[] outArr = fromExcelInputstreamToString.split("\n");
        final List<String> allLines = Arrays.asList(outArr).stream().map(String::trim).collect(Collectors.toList());
        final List<String> checkList = new ArrayList<>();
        checkList.add("description: OpenAPI specification generated by Joaswizard (https://github.com/networkinss/joaswizard).");
        checkList.add("email: john.doe@example.com");
        checkList.add("operationId: findInvoiceById");
        checkList.add("operationId: getInvoiceList");
        boolean ok = false;
        try (Stream<String> lines = checkList.stream()) {
            ok = lines.allMatch(l -> allLines.contains(l));
        }
        if (ok == false) System.out.println(fromExcelInputstreamToString);
        assertTrue(ok);
    }
}