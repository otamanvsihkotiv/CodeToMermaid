package sequencediagram.firsttest

import org.codetomermaid.diagram.sequence.toSequenceDiagram
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class SimpleTest {
    private var result = ""
    val folder: Path = Path.of("src", "test", "java", "sequencediagram", "firsttest", "UI.java")
    val sourcePath: Path = Path.of("src", "test", "java")

    @BeforeEach
    fun setUp() {
        val datasource = Datasource()
        val repository = Repository(datasource)
        val viewModel = ViewModel(repository)
        val ui = UI(viewModel)

        val list = ui.getTheCode()
        if (list != null) {
            result = list.joinToString("")
        }
    }

    @Test
    fun verifyThatTheTestProgramIsWorking() {
        assert(result == "Hello World!")
    }

    @Test
    @Throws(IOException::class)
    fun verifyOutputFile() {
        toSequenceDiagram(
            folder,
            "getTheCode",
            sourcePath,
            "simpletest"
        )

        val expected = Files.readString(Path.of("src/test/resources/simpletest.md"))
        val actual = Files.readString(Path.of("target/generated-diagrams/sequencediagram/simpletest.md"))
        assertEquals(expected, actual)
    }

    @Test
    fun abortsIfTargetMethodIsntFound() {
        try {
            Files.deleteIfExists(Path.of("target", "generated-diagrams", "sequencediagram", "simpletest.md"))
            toSequenceDiagram(
                folder,
                "retriveTheCode",
                sourcePath,
                "simpletest"
            )
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "No method named retriveTheCode in class UI.\n" +
                        "Methods found in class UI: [getTheCode]}",
                e.message
            )
        }
    }
}
