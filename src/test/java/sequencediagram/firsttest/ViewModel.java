package sequencediagram.firsttest;

import java.util.List;

class ViewModel {
    private final Repository repository;

    ViewModel(Repository repository) {
        this.repository = repository;
    }

    protected List<String> getTheCode() {
        return repository.getTheCode();
    }
}