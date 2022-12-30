package com.ideas2it.FHIRSample.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.ideas2it.FHIRSample.dto.PatientDto;
import com.ideas2it.FHIRSample.mapper.PatientMapper;
import com.ideas2it.FHIRSample.mapper.service.impl.PatientServiceImpl;
import lombok.val;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @InjectMocks
    PatientServiceImpl patientService;

    @Mock
    PatientMapper patientMapper;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    FhirContext context;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IGenericClient client;

    private Patient patient = new Patient();

    private PatientDto patientDto = new PatientDto();

    String  serverBase = "https://hapi.fhir.org/baseR4";
    @BeforeEach
    public void setup() throws ParseException {

        patient.addName()
                .setText("guna")
                .addGiven("J")
                .addGiven("John");
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("8899776677");
        patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("gun@gmail.com");
        patient.setGender(Enumerations.AdministrativeGender.MALE);
        patientDto.setId(7777);
        patientDto.setName("guna");
        patientDto.setDateOfBirth(LocalDate.of(1999, 01, 01));
        patientDto.setMobileNumber("8899889988");
        patientDto.setEmail("gun@gmail.com");
        patientDto.setGender("male");
        patientDto.setState("tamilnadu");
        patientDto.setCity("chennai");
        patientDto.setPincode("600078");
    }

    @Test
    void createPatientTest() {
        IGenericClient clients = context.newRestfulGenericClient(serverBase);
        when(clients.create().resource(patient).execute()).thenReturn(new MethodOutcome().setCreated(true));
        when(patientMapper.convertToFhir(patientDto)).thenReturn(patient);
        when(context.newRestfulGenericClient(serverBase)).thenReturn(clients);
        var outcome = patientService.createPatient(patientDto);
        assertEquals(true, outcome);
    }
    @Test
    void getPatient() {

        when(client.read().resource(Patient.class).withId(7777L).execute()).thenReturn(patient);
        when(context.newRestfulGenericClient("http://hapi.fhir.org/baseR4")).thenReturn(client);
        when(patientMapper.convertToDto(patient)).thenReturn(patientDto);
        var patientDto = patientService.getPatient(7777L);
        assertEquals("guna", patientDto.getName());
    }

    @Test
    void getPatientTest_1() {

        when(client.read().resource(Patient.class).withId(7777L).execute())
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class,
                () -> client.read().resource(Patient.class).withId(7777L).execute());
    }

    @Test
    void updatePatientTest() {

        long patientId = 7777;
        patient.addName().setFamily("tamil");
        patient.setId(new IdType(patientId));
        when(patientMapper.convertToFhirWithId(patientDto)).thenReturn(patient);
        when(context.newRestfulGenericClient(serverBase)).thenReturn(client);
        when(client.read().resource(Patient.class).withId(patientId).execute()).thenReturn(patient);
        when(client.update().resource(patient).execute()).thenReturn(new MethodOutcome().setResource(patient));
        when(patientMapper.convertToDto(patient)).thenReturn(patientDto);
        var patient = client.read().resource(Patient.class).withId(patientId).execute();
        var outcome = patientService.updatePatient(7777L, patientDto);
        assertEquals(7777, outcome.getId());
    }

    @Test
    void updatePatientTest_1() {

        long patientId = 7777;
        patient.addName().setFamily("tamil");
        patient.setId(new IdType(patientId));
        when(client.read().resource(Patient.class).withId(patientId).execute())
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class,
                () -> client.read().resource(Patient.class).withId(patientId).execute());
    }

    @Test
    void deletePatientTest() {
        MethodOutcome outcome = new MethodOutcome();
        outcome.setOperationOutcome(new OperationOutcome()
                .setIssue(List.of(new OperationOutcome.OperationOutcomeIssueComponent()
                        .setDiagnostics("deleted"))));
        outcome.setStatusCode(200);
        when(client.delete().resource(patient).execute()).thenReturn(outcome);
        when(context.newRestfulGenericClient("http://hapi.fhir.org/baseR4")).thenReturn(client);
        when(client.read().resource(Patient.class).withId(7777L).execute()).thenReturn(patient);
        when(context.newJsonParser().encodeResourceToString(outcome.getOperationOutcome())).thenReturn("true");
        var result = patientService.deletePatient(7777L);
        assertEquals("true", result);
    }
}