package sequencediagram.firsttest;

import org.codetomermaid.ToSequenceDiagram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    private String result;

    @BeforeEach
    public void setUp(){
        Datasource datasource = new Datasource();
        Repository repository = new Repository(datasource);
        ViewModel viewModel = new ViewModel(repository);
        UI ui = new UI(viewModel);

        List<String> list = ui.getTheCode();

        StringBuilder output = new StringBuilder();
        for (String s : list) {
            output.append(s);
        }
        result = output.toString();
    }

    @Test
    public void verifyThatTheTestProgramIsWorking() {
        assert result.equals("Hello World!");
    }

    @Test
    public void verifyOutputFile() throws IOException {
        Path folder = Path.of("src", "test", "java", "sequencediagram.firsttest", "UI.java");
        Path saveTo = Path.of("target", "testoutput", "sequencediagram", "simpletest.md");
        ToSequenceDiagram.notImplementedYet(folder, "getTheCode", saveTo);

        String expected = Files.readString(Path.of("src/test/resources/simpletest.md"));
        String actual   = Files.readString(Path.of("target/testoutput/sequencediagram/simpletest.md"));
        assertEquals(expected, actual);
    }
}
