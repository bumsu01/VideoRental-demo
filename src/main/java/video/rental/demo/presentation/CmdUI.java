package video.rental.demo.presentation;

import java.time.LocalDate;
import java.util.Scanner;

import video.rental.demo.application.Interactor;

public class CmdUI {
	private static Scanner scanner = new Scanner(System.in);
	private Interactor interactor;
	
	public CmdUI(Interactor interactor) {
		this.interactor = interactor;
	}
	
	CmdFunction cmdFunction;

	public void start() {
		boolean quit = false;
		while (!quit) {
			int command = getCommand();
			switch (command) {
			case 0:
				quit = true;
				break;
			case 1:
				cmdFunction.listCustomers(interactor);
				break;
			case 2:
				cmdFunction.listVideos(interactor);
				break;
			case 3:
				cmdFunction.register(interactor, "customer");
				break;
			case 4:
				cmdFunction.register(interactor, "video");
				break;
			case 5:
				cmdFunction.rentVideo(interactor);
				break;
			case 6:
				cmdFunction.returnVideo(interactor);
				break;
			case 7:
				cmdFunction.getCustomerReport(interactor);
				break;
			case 8:
				cmdFunction.clearRentals(interactor);
				break;
			default:
				break;
			}
		}
		System.out.println("Bye");
	}

	public int getCommand() {
		System.out.println("\nSelect a command !");
		System.out.println("\t 0. Quit");
		System.out.println("\t 1. List customers");
		System.out.println("\t 2. List videos");
		System.out.println("\t 3. Register customer");
		System.out.println("\t 4. Register video");
		System.out.println("\t 5. Rent video");
		System.out.println("\t 6. Return video");
		System.out.println("\t 7. Show customer report");
		System.out.println("\t 8. Show customer and clear rentals");

		int command = scanner.nextInt();

		return command;
	}

}
