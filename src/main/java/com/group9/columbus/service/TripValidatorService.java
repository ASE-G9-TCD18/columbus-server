package com.group9.columbus.service;

import org.springframework.stereotype.Service;

import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.Preference;
import com.group9.columbus.enums.PreferenceType;
import com.group9.columbus.exception.IncorrectValueFormat;

@Service
public class TripValidatorService {

	public void validateTripCreationDetails(TripDto tripDto) throws IncorrectValueFormat {

		for (Preference preference : tripDto.getPreferences()) {
			if (PreferenceType.START_DATE.equals(preference.getPreferenceType())
					|| PreferenceType.END_DATE.equals(preference.getPreferenceType())) {
			    String prefVal = (String)preference.getValue();
				if (!prefVal.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})"))
					throw new IncorrectValueFormat("Incorrect date format. Should match yyyy-MM-dd");

			} else if (PreferenceType.START_TIME.equals(preference.getPreferenceType())) {
                String prefVal = (String)preference.getValue();
				if (!prefVal.matches("([0-9]{2}):([0-9]{2}):([0-9]{2})"))
					throw new IncorrectValueFormat("Incorrect date format. Should match HH:mm:ss");

			} else if (PreferenceType.AGE_RANGE.equals(preference.getPreferenceType())) {
                String prefVal = (String)preference.getValue();
				if (!prefVal.matches("[0-9]*-[0-9]*"))
					throw new IncorrectValueFormat("Incorrect date format. Should match one of the formats "
							+ "xx-yy / - yy / xx -");
			}
		}
	}

}

