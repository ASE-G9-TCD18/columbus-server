package com.group9.columbus.test.trip;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.maps.model.LatLng;
import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Preference;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.entity.TripStop;
import com.group9.columbus.enums.Gender;
import com.group9.columbus.enums.PreferenceType;
import com.group9.columbus.enums.Repeat;
import com.group9.columbus.enums.TripType;
import com.group9.columbus.exception.IncorrectValueFormat;
import com.group9.columbus.repository.UserRepository;
import com.group9.columbus.service.TripService;
import com.group9.columbus.test.TestConfig;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = { TestConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TripServiceTest {

	Logger logger = Logger.getLogger(TripServiceTest.class);
	
	@Autowired
	private TripService tripService;

	@MockBean
	private UserRepository userRepo;
	
	String loginId = "testuser";
	String password = "testuser";
	
	private ApplicationUser user;
	private TripDto tripDto;
	private SimpleDateFormat sdf;
	
	@Before
	public void init() {
		sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		tripDto = new TripDto();
		
		user = new ApplicationUser();
		user.setLoginId(loginId);
		user.setPassword(password);
		user.setActive(true);
		user.setAge("40");
		user.setContactNumber("1234567890");
		user.setEmailId("temp@example.com");
		user.setFirstName("ABC");
		user.setLastName("Agrahari");
		user.setUserRating(5.0);
		
		given(userRepo.findByLoginId(loginId)).willReturn(user);
	}
	
	@Test
	public void createTripSuccessfull() throws IncorrectValueFormat {
		logger.info("Running create trip test");
		
		List<Preference> preferences = new ArrayList<>();
		Preference startDatePref = new Preference<>(PreferenceType.START_DATE, "2017-03-17_20:20:20");
		Preference endDatePref = new Preference<>(PreferenceType.END_DATE, "2017-03-20_20:20:20");
		Preference repeatPref = new Preference<>(PreferenceType.REPEAT, Repeat.WEEKDAY.toString());
		Preference genderPref = new Preference<>(PreferenceType.GENDER, Gender.M.toString());
		Preference agePref = new Preference<>(PreferenceType.AGE_RANGE, "20-30");
		
		preferences.add(startDatePref);
		preferences.add(endDatePref);
		preferences.add(repeatPref);
		preferences.add(genderPref);
		preferences.add(agePref);
		
		List<TripStop> tripStops = new ArrayList<>();
		TripStop startPos = new TripStop(1,new LatLng(53.2711963, -6.2045213)); // Central Park 
		TripStop endPos = new TripStop(1,new LatLng(53.3437967, -6.2567603)); // Trinity

		tripStops.add(startPos);
		tripStops.add(endPos);
		
		tripDto.setPreferences(preferences);
		tripDto.setTimestamp(sdf.format(new Date()));
		tripDto.setTripType(TripType.SCHEDULED);
		tripDto.setTripStops(tripStops);
		
		// Actual
		Trip actual = tripService.createTrip(loginId, tripDto);
		
		// Assertions
		assertThat(actual.getConversation()).isNotEqualTo(null);
		logger.info("Conversation created successfully inside trip.");
		
		assertThat(actual.getTripId()).isNotEqualTo(null);
		logger.info("Trip has been assigned a trip id.");
	}

	@Test(expected=IncorrectValueFormat.class)
	public void createTripFailure() throws IncorrectValueFormat {
		logger.info("Running create trip test for failure");
		List<Preference> preferences = new ArrayList<>();
		Preference startDatePref = new Preference<>(PreferenceType.START_DATE, "2017-03-17_202020");
		Preference endDatePref = new Preference<>(PreferenceType.END_DATE, "2017-03-20");
		Preference repeatPref = new Preference<>(PreferenceType.REPEAT, Repeat.WEEKDAY.toString());
		Preference genderPref = new Preference<>(PreferenceType.GENDER, Gender.M.toString());
		Preference agePref = new Preference<>(PreferenceType.AGE_RANGE, "20-30");
		// TODO: Group size pref

		preferences.add(startDatePref);
		preferences.add(endDatePref);
		preferences.add(repeatPref);
		preferences.add(genderPref);
		preferences.add(agePref);

		List<TripStop> tripStops = new ArrayList<>();
		TripStop startPos = new TripStop(1,new LatLng(53.2711963, -6.2045213)); // Central Park
		TripStop endPos = new TripStop(1,new LatLng(53.3437967, -6.2567603)); // Trinity

		tripStops.add(startPos);
		tripStops.add(endPos);

		tripDto.setPreferences(preferences);
		tripDto.setTimestamp(sdf.format(new Date()));
		tripDto.setTripType(TripType.SCHEDULED);
		tripDto.setTripStops(tripStops);

		// Actual
		Trip actual = tripService.createTrip(loginId, tripDto);

	}
}
