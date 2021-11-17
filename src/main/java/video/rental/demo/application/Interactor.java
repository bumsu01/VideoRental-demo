package video.rental.demo.application;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import video.rental.demo.domain.Customer;
import video.rental.demo.domain.Rating;
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
			builder.append("No customer found\n");
		} else {
			builder.append(foundCustomer.clearRental());
			getRepository().saveCustomer(foundCustomer);
		}
		
		return builder.toString();
	}

	public void returnVideo(int customerCode, String videoTitle) {
		Customer foundCustomer = getRepository().findCustomerById(customerCode);
		if (foundCustomer == null)
			return;

		Video video = foundCustomer.returnVideo(videoTitle);
		if (video != null)
			getRepository().saveVideo(video);

		getRepository().saveCustomer(foundCustomer);
	}

	public String listVideos() {
		StringBuilder builder = new StringBuilder();
		
		List<Video> videos = getRepository().findAllVideos();
	
		for (Video video : videos) {
			builder.append(video.toString());
		}
		return builder.toString();
	}

	public String listCustomers() {
		List<Customer> customers = getRepository().findAllCustomers();
		return customers
				.stream()
				.map(Customer::toString)
				.collect(Collectors.joining());
	}

	public String getCustomerReports(int code) {
		Customer foundCustomer = getRepository().findCustomerById(code);

		return (foundCustomer == null?"No customer found" : foundCustomer.getReport())  + "\n";
	}

	public void rentVideo(int code, String videoTitle) {
		Customer foundCustomer = getRepository().findCustomerById(code);
		if (foundCustomer == null)
			return;
	
		Video foundVideo = getRepository().findVideoByTitle(videoTitle);
	
		if (foundVideo == null || foundVideo.isRented())
			return;
	
		if (foundVideo.rentFor(foundCustomer)) {
			getRepository().saveVideo(foundVideo);
			getRepository().saveCustomer(foundCustomer);
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
