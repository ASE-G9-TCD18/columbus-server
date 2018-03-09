package com.group9.columbus.test.trip;

import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Preference;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.enums.PreferenceType;
import com.group9.columbus.repository.TripRepository;
import com.group9.columbus.repository.UserRepository;
import com.group9.columbus.service.TripService;
import com.group9.columbus.service.UserManagementService;
import com.group9.columbus.test.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author yang
 * @date 06/03/2018
 */
@SpringBootTest(classes = { TestConfig.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class JoinTripTest {

    @Autowired
    @Qualifier("mvc")
    private MockMvc mvc;

    @Autowired
    @Qualifier("httpMsgConverter")
    private HttpMessageConverter httpMsgConverter;

    @MockBean
    private TripRepository tripRepo;
    @MockBean
    private UserRepository userRepo;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserManagementService userService;

    private static final String TRIP_ID = "dummyTp_id";
    private static final String USER_ID_1 = "dummyUsr_1";
    private static final String USER_ID_2 = "dummyUsr_2";

    @Before
    public void setupTestData(){
        Trip dummyTp = new Trip();
        Preference<Integer> sizePref = new Preference<>(PreferenceType.GROUP_SIZE, 1);
        dummyTp.setPreferences(Arrays.asList(new Preference[]{sizePref}));
        dummyTp.setTripUsersLoginIds(new ArrayList<>());
        Mockito.when(tripRepo.findByTripId(TRIP_ID)).thenReturn(dummyTp);

        ApplicationUser user1 = new ApplicationUser();
        user1.setLoginId(USER_ID_1);
        ApplicationUser user2 = new ApplicationUser();
        user1.setLoginId(USER_ID_2);
        Mockito.when(userRepo.findByLoginId(USER_ID_1)).thenReturn(user1);
        Mockito.when(userRepo.findByLoginId(USER_ID_2)).thenReturn(user2);
    }

    @Test
    public void joinTripUnitTest() throws Exception {

        tripService.requestJoinTrip(USER_ID_1,TRIP_ID);
        tripService.requestJoinTrip(USER_ID_2,TRIP_ID);
        Trip trp = tripService.getTripById(TRIP_ID);

    }
//    @Test
//    public void integrationRepoOptimisticLocking() throws Exception {
//        mvc.perform(post("/trip/"+TRIP_ID).with(user("testUser").password("testUser")))
//                .andExpect(status().is2xxSuccessful());
//
//
//    }




}
