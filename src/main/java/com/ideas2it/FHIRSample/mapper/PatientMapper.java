package com.ideas2it.FHIRSample.mapper;

import com.ideas2it.FHIRSample.dto.PatientDto;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * Patient mapper class is used convert Patient model into
 * Patient dto and also used to convert Patient dto into
 * Patient model.
 * </p>
 *
 * @author Gunaseelan
 * @version 1
 * @since 2022-10-10
 */

@Component
public class PatientMapper {

    /**
     * <p>
     * This method is used to convert Patient dto into
     * Patient model
     * </p>
     *
     * @param patientDto {@link PatientDto} contains patient details
     * @return {@link Patient}
     */
    public Patient convertToFhir(PatientDto patientDto) {
        var patient = new Patient();


        patient.addIdentifier()
                .setSystem("http://acme.org/mrns")
                .setValue(UUID.randomUUID().toString());
        patient.addName()
                .setText(patientDto.getName())
                .addGiven("J")
                .addGiven("Jonah");
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(patientDto.getMobileNumber());
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(patientDto.getEmail());
        switch (patientDto.getGender()) {
            case "male" -> patient.setGender(Enumerations.AdministrativeGender.MALE);
            case "female" -> patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            case "others" -> patient.setGender(Enumerations.AdministrativeGender.OTHER);
        }
        patient.setBirthDate(Date.from(patientDto.getDateOfBirth().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        patient.addAddress().setState(patientDto.getState()).setCity(patientDto.getCity()).setPostalCode(patientDto.getPincode());
        return patient;
    }

    public Patient convertToFhirWithId(PatientDto patientDto) {
        var patient = new Patient();

        patient.setId(String.valueOf(patientDto.getId()));
        patient.addIdentifier()
                .setSystem("http://acme.org/mrns")
                .setValue(UUID.randomUUID().toString());
        patient.addName()
                .setText(patientDto.getName())
                .addGiven("J")
                .addGiven("Jonah");
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(patientDto.getMobileNumber());
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(patientDto.getEmail());
        switch (patientDto.getGender()) {
            case "male" -> patient.setGender(Enumerations.AdministrativeGender.MALE);
            case "female" -> patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            case "others" -> patient.setGender(Enumerations.AdministrativeGender.OTHER);
        }
        patient.setBirthDate(Date.from(patientDto.getDateOfBirth().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        patient.addAddress().setState(patientDto.getState()).setCity(patientDto.getCity()).setPostalCode(patientDto.getPincode());
        return patient;
    }

    public PatientDto convertToDto(Patient patient) {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(Integer.valueOf(patient.getIdElement().getIdPart()));
        patientDto.setName(patient.getNameFirstRep().getText());
        patientDto.setDateOfBirth(patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        patientDto.setMobileNumber(patient.getTelecomFirstRep().getValue());
        patientDto.setEmail(patient.getTelecom().get(1).getValue());
        patientDto.setGender(patient.getGender().toString());
        patientDto.setState(patient.getAddressFirstRep().getState());
        patientDto.setCity(patient.getAddressFirstRep().getCity());
        patientDto.setPincode(patient.getAddressFirstRep().getPostalCode());
        return patientDto;
    }
}

