package com.netcracker.dataservice.dto;

import com.netcracker.dataservice.utils.PasswordMatches;
import com.netcracker.dataservice.utils.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class RegistrationDto {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;


}
