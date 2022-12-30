package com.ideas2it.FHIRSample.dto;

import com.ideas2it.FHIRSample.common.Constants;
import com.ideas2it.FHIRSample.common.ErrorConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <p>
 * The Patient dto is mainly used for reducing the number of expensive remote calls.
 * In order to convert data between the DTO and any entity objects,
 * the assembler object was defined, but now we are using mappers for converting data.
 * </p>
 *
 * @author Gunaseelan
 * @version 1
 * @since 2022-10-10
 */
@Data
@NoArgsConstructor
public class PatientDto {

    private long id;

    @NotNull(message = ErrorConstants.NAME_SHOULD_NOT_BE_NULL)
    @Pattern(regexp = Constants.NAME_REGEX, message = ErrorConstants.NAME_FORMAT)
    private String name;

    @NotNull(message = ErrorConstants.DATE_OF_BIRTH_SHOULD_NOT_BE_NULL)
    @Past(message = ErrorConstants.ENTER_VALID_DATE_OF_BIRTH)
    private LocalDate dateOfBirth;

    @NotNull(message = ErrorConstants.GENDER_SHOULD_NOT_BE_NULL)
    @Pattern(regexp = Constants.GENDER_REGEX, message = ErrorConstants.ENTER_MALE_OR_FEMALE)
    private String gender;

    @Pattern(regexp = Constants.MOBILE_NUMBER_REGEX, message = ErrorConstants.ENTER_VALID_MOBILE_NUMBER)
    private String mobileNumber;

    @NotNull(message = ErrorConstants.EMAIL_SHOULD_NOT_BE_NULL)
    @Email(message = ErrorConstants.ENTER_VALID_EMAIL)
    private String email;

    private String state;

    private String city;

    private String pincode;
}
