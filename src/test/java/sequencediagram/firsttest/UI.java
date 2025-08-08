package sequencediagram.firsttest;

import java.util.List;

class UI {
    private final ViewModel viewModel;

    UI(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    protected List<String> getTheCode() {
        return viewModel.getTheCode();
    }
}



