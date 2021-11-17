package video.rental.demo;

import video.rental.demo.application.Interactor;
import video.rental.demo.domain.Repository;
import video.rental.demo.infrastructure.RepositoryMemImpl;
import video.rental.demo.presentation.CmdUI;
import video.rental.demo.presentation.GraphicUI;
import video.rental.demo.presentation.UI;
import video.rental.demo.utils.SampleGenerator;

public class Main {
	private static UI ui;

	public static void main(String[] args) {
		Repository repository = new RepositoryMemImpl();
		new SampleGenerator(repository).generateSamples();
		
		Interactor interactor = new Interactor(repository);
		ui = new GraphicUI(interactor);
		ui.start();
	}
}
