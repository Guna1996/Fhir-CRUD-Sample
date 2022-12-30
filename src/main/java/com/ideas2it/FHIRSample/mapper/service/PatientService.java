package com.ideas2it.FHIRSample.mapper.service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import com.ideas2it.FHIRSample.dto.PatientDto;

public interface PatientService {

    /**
     * <p>
     * This method is used to add patient details such as
     * name, date of birth, mobile number, gender, etc
     * into data base by getting details from the patient
     * </p>
     *
     * @param patientDto {@link PatientDto} contains patient details
     * @return {@link String}
     */
    boolean createPatient(PatientDto patientDto);

    PatientDto getPatient(long id);

    PatientDto updatePatient(long id, PatientDto patientDto);

    String deletePatient(long id);

    String createPatientBundle(PatientDto patientDto);
}
