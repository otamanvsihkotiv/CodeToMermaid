package sequencediagram.firsttest;

import java.util.List;

class Repository {
    private final Datasource datasource;

    Repository(Datasource datasource) {
        this.datasource = datasource;
    }

    protected List<String> getTheCode() {
        return datasource.getTheCode();
    }
}