package ch.inss.openapi.crudwizard.action

import ch.inss.openapi.crudwizard.OutputWraper
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test

import java.io.FileInputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest : BasePlatformTestCase() {

    @Test
    fun testJo() {
        val content = readFile("src/test/testData/openapi.txt", StandardCharsets.UTF_8)
        val compare = readFile("src/test/testData/openapi.yaml", StandardCharsets.UTF_8)
        val output = Jo.joStringToOas3(content, 3)
        val result = output.outputText
        assertEquals(result, compare)
    }

    @Test
    @Throws(Exception::class)
    fun testExcelFromInputStream() {
        val fileInputStream = FileInputStream("src/test/testData/objectimport.xlsx")
        val fromExcelInputstreamToString: OutputWraper = Jo.joExcelToOas3String(fileInputStream)
        val outputText = fromExcelInputstreamToString.outputText
        val outArr = outputText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val allLines = Arrays.asList(*outArr).stream().map { obj: String -> obj.trim { it <= ' ' } }
                .collect(Collectors.toList())
        val checkList: MutableList<String> = ArrayList()
        checkList.add("description: Generated by the IntelliJ Plugin 'OpenAPI CRUD Wizard'")
        checkList.add("email: john.doe@example.com")
        checkList.add("operationId: findInvoiceById")
        checkList.add("operationId: getInvoiceList")
        var ok = false
        var lines = checkList.stream()
        ok = lines.allMatch { l -> allLines.contains(l) }
        assertTrue(ok)
    }

    @Test
    @Throws(Exception::class)
    fun testExcelFromFile() {
        val fromExcelInputstreamToString = Jo.joExcelToOas3File()
        assertTrue(fromExcelInputstreamToString)
    }

    fun readFile(path: String, encoding: Charset): String {
        val encoded = Files.readAllBytes(Paths.get(path))
        return String(encoded, encoding)
    }
}