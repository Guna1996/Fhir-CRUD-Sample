package com.ideas2it.FHIRSample.mapper.service.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.ideas2it.FHIRSample.dto.PatientDto;
import com.ideas2it.FHIRSample.exception.NotAccessibleException;
import com.ideas2it.FHIRSample.mapper.PatientMapper;
import com.ideas2it.FHIRSample.mapper.service.PatientService;
import lombok.val;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    FhirContext context;

    /**
     * {@inheritDoc}
     */
    public boolean createPatient(PatientDto patientDto) {
        val patient = patientMapper.convertToFhir((patientDto));
        val serverBase = "https://hapi.fhir.org/baseR4";
        val client = context.newRestfulGenericClient(serverBase);
        System.out.println("888888888888888888888" + client);
        val outcome = client.create().resource(patient).execute();
//        patient.setId(outcome.getResource().getIdElement().getIdPartAsLong().toString());
        return outcome.getCreated();
    }

    public PatientDto getPatient(long id) {
        Patient patient = null;
        val client = context.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        try {
            patient = client.read().resource(Patient.class).withId(id).execute();
        } catch (ResourceNotFoundException exception) {
            throw new NotAccessibleException("Resource not found");
        }
        return patientMapper.convertToDto(patient);
    }


    public PatientDto updatePatient(long id, PatientDto patientDto) {
        patientDto.setId(id);
        val patient = patientMapper.convertToFhirWithId(patientDto);
        val serverBase = "https://hapi.fhir.org/baseR4";
        val client = context.newRestfulGenericClient(serverBase);
        try {
            client.read().resource(Patient.class).withId(id).execute();
            val outcome = client.update().resource(patient).execute();
        } catch (ResourceNotFoundException exception) {
            throw new NotAccessibleException("Resource not found");
        }
        return patientMapper.convertToDto(patient);
    }

    public String deletePatient(long id) {
        MethodOutcome outcome = null;
        val client = context.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        try {
            val patient = client.read().resource(Patient.class).withId(id).execute();
            outcome = client.delete().resource(patient).execute();
        } catch (ResourceNotFoundException exception) {
            throw new NotAccessibleException("Resource not found");
        }
        return context.newJsonParser().encodeResourceToString(outcome.getOperationOutcome());
    }

    public String createPatientBundle(PatientDto patientDto) {
        val patient = patientMapper.convertToFhir((patientDto));
        Observation observation = new Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation
                .getCode()
                .addCoding()
                .setSystem("http://loinc.org")
                .setCode("789-8")
                .setDisplay("Erythrocytes [#/volume] in Blood by Automated count");
        observation.setValue(
                new Quantity()
                        .setValue(4.12)
                        .setUnit("10 trillion/L")
                        .setSystem("http://unitsofmeasure.org")
                        .setCode("10*12/L"));
        observation.setSubject(new Reference(patient.getIdElement().getValue()));
        val bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        bundle.addEntry()
                .setFullUrl(patient.getIdElement().getValue())
                .setResource(patient)
                .getRequest()
                .setUrl("Patient")
                .setMethod(Bundle.HTTPVerb.POST);
        bundle.addEntry()
                .setResource(observation)
                .getRequest()
                .setUrl("Observation")
                .setMethod(Bundle.HTTPVerb.POST);
        val ctx = FhirContext.forR4();
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
        val client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        val resp = client.transaction().withBundle(bundle).execute();
        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp);
    }
}
