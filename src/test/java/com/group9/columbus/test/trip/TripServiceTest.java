package com.group9.columbus.test.trip;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.group9.columbus.controller.TripController;
import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.Preference;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.entity.TripStop;
import com.group9.columbus.enums.Gender;
import com.group9.columbus.enums.PreferenceType;
import com.group9.columbus.enums.Repeat;
import com.group9.columbus.enums.TripType;
import com.group9.columbus.repository.TripRepository;
import com.group9.columbus.service.TripService;
import com.group9.columbus.test.TestConfig;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(classes = { TestConfig.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TripServiceTest {
	
	@Autowired
	@Qualifier("mvc")
	private MockMvc mvc;
	
	@Autowired
	@Qualifier("httpMsgConverter")
	private HttpMessageConverter httpMsgConverter;

	@Autowired
	private TripController tripController;
	
	@Autowired
	private TripRepository tripRepo;
	
	@Autowired
	private TripService tripService;
	
	String loginId = "testuser";
	String password = "testuser";
	
	private TripDto tripDto;
	private SimpleDateFormat sdf;
	
	@Before
	public void init() {
		sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		tripDto = new TripDto();
	}
	
	@Test
	public void createTrip() {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		
		List<Preference> preferences = new ArrayList<>();
		Preference startTimePref = new Preference(PreferenceType.START_TIME, "20:20:20");
		Preference startDatePref = new Preference(PreferenceType.START_DATE, "2017-03-17");
		Preference endDatePref = new Preference(PreferenceType.END_DATE, "2017-03-20");
		Preference repeatPref = new Preference(PreferenceType.REPEAT, Repeat.WEEKDAY.toString());
		Preference genderPref = new Preference(PreferenceType.GENDER, Gender.M.toString());
		Preference agePref = new Preference(PreferenceType.AGE_RANGE, "20-30");
		
		preferences.add(startTimePref);
		preferences.add(startDatePref);
		preferences.add(endDatePref);
		preferences.add(repeatPref);
		preferences.add(genderPref);
		preferences.add(agePref);
		
		List<TripStop> tripStops = new ArrayList<>();
		TripStop startPos = new TripStop(1,new Point(53.2711963, -6.2045213)); // Central Park 
		TripStop endPos = new TripStop(1,new Point(53.3437967, -6.2567603)); // Trinity

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
		assert
				
	}
}
