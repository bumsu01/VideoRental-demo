package video.rental.demo.application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Rating;
import video.rental.demo.domain.Rental;
import video.rental.demo.domain.Repository;
import video.rental.demo.domain.Video;

public class Interactor {
	private Repository repository;
	
	public Interactor(Repository repository) {
		super();
		this.repository = repository;
	}

	public String clearRentals(int customerCode) {
		StringBuilder builder = new StringBuilder();
		
		Customer foundCustomer = getRepository().findCustomerById(customerCode);
	
		if (foundCustomer == null) {
			return "No customer found";
		} else {
			builder.append("Id: " + foundCustomer.getCode() + "\nName: " + foundCustomer.getName() + "\tRentals: "
					+ foundCustomer.getRentals().size() + "\n");
			for (Rental rental : foundCustomer.getRentals()) {
				builder.append("\tTitle: " + rental.getVideo().getTitle() + " ");
				builder.append("\tPrice Code: " + rental.getVideo().getPriceCode());
			}
	
			List<Rental> rentals = new ArrayList<Rental>();
			foundCustomer.setRentals(rentals);
	
			getRepository().saveCustomer(foundCustomer);
		}
		
		return builder.toString();
	}

	public void returnVideo(int customerCode, String videoTitle) {
		Customer foundCustomer = getRepository().findCustomerById(customerCode);
		if (foundCustomer == null)
			return;
	
		List<Rental> customerRentals = foundCustomer.getRentals();
	
		for (Rental rental : customerRentals) {
			if (rental.getVideo().getTitle().equals(videoTitle) && rental.getVideo().isRented()) {
				Video video = rental.returnVideo();
				video.setRented(false);
				getRepository().saveVideo(video);
				break;
			}
		}
	
		getRepository().saveCustomer(foundCustomer);
	}

	public String listVideos() {
		StringBuilder builder = new StringBuilder();
		
		List<Video> videos = getRepository().findAllVideos();
	
		for (Video video : videos) {
			builder.append(
					"Video type: " + video.getVideoType() + 
					"\tPrice code: " + video.getPriceCode() + 
					"\tRating: " + video.getVideoRating() +
					"\tTitle: " + video.getTitle() + "\n"
					); 
		}
		return builder.toString();
	}

	public String listCustomers() {

		List<Customer> customers = getRepository().findAllCustomers();

		StringBuilder sb = new StringBuilder();
		for (Customer customer : customers) {
			sb.append("ID: " + customer.getCode() + "\nName: " + customer.getName() + "\tRentals: "
					+ customer.getRentals().size());
			for (Rental rental : customer.getRentals()) {
				sb.append("\tTitle: " + rental.getVideo().getTitle() + " ");
				sb.append("\tPrice Code: " + rental.getVideo().getPriceCode());
				sb.append("\tReturn Status: " + rental.getStatus());
			}
		}

		return sb.toString();
	}

	public String getCustomerReposrt(int code) {
		Customer foundCustomer = getRepository().findCustomerById(code);
	
		if (foundCustomer == null) {
			return "No customer found";
		} else {
			return foundCustomer.getReport();
		}
	}

	public void rentVideo(int code, String videoTitle) {
		Customer foundCustomer = getRepository().findCustomerById(code);
		if (foundCustomer == null)
			return;
	
		Video foundVideo = getRepository().findVideoByTitle(videoTitle);
	
		if (foundVideo == null)
			return;
	
		if (foundVideo.isRented() == true)
			return;
	
		Boolean status = foundVideo.rentFor(foundCustomer);
		if (status == true) {
			getRepository().saveVideo(foundVideo);
			getRepository().saveCustomer(foundCustomer);
		} else {
			return;
		}
	}

	public void registerCustomer(String name, int code, String dateOfBirth) {
		Customer customer = new Customer(code, name, LocalDate.parse(dateOfBirth));
		getRepository().saveCustomer(customer);
	}

	public void registerVideo(String title, int videoType, int priceCode, int videoRating, LocalDate registeredDate) {
		Rating rating;
		if (videoRating == 1) rating = Rating.TWELVE;
		else if (videoRating == 2) rating = Rating.FIFTEEN;
		else if (videoRating == 3) rating = Rating.EIGHTEEN;
		else throw new IllegalArgumentException("No such rating " + videoRating);
		
		Video video = new Video(title, videoType, priceCode, rating, registeredDate);
	
		getRepository().saveVideo(video);
	}

	Repository getRepository() {
		return repository;
	}

	
}
