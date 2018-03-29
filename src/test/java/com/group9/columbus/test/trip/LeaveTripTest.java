package com.group9.columbus.test.trip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.exception.TripManagementException;
import com.group9.columbus.repository.TripRepository;
import com.group9.columbus.repository.UserRepository;
import com.group9.columbus.service.TripService;
import com.group9.columbus.test.TestConfig;

@SpringBootTest(classes = { TestConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class LeaveTripTest {

	Logger logger = Logger.getLogger(LeaveTripTest.class);

	@MockBean
	TripRepository mockedTripRepo;

	@MockBean
	private UserRepository userRepo;
	
	@Autowired
	TripService tripService;

	String tripId = "1803286775";
	String memberLoginId = "tripMember1";

	
	@Before
	public void init() {
		Trip trip = new Trip();

		trip.setTripId("1803286775");
		trip.setAdmin("admin");

		List<String> tripUsers = new ArrayList<>();
		tripUsers.add("admin");
		tripUsers.add(memberLoginId);
		tripUsers.add("tripMember2");
		trip.setTripUsersLoginIds(tripUsers);

		ApplicationUser tripMember = new ApplicationUser();
		tripMember.setLoginId(memberLoginId);
		tripMember.setTrips(new ArrayList<>(Arrays.asList(trip)));

		// Mocking the trip
		given(mockedTripRepo.findByTripId(tripId)).willReturn(trip);

		// Mocking the trip
		given(userRepo.findByLoginId(memberLoginId)).willReturn(tripMember);
	}

	@Test
	public void leaveTripTest() throws TripManagementException {

		// Assertions
		assertThat(tripService.leaveTrip(memberLoginId, tripId)).isEqualTo(true);
	}

}
