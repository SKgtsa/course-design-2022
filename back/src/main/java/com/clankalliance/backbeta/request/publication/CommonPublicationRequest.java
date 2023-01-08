package com.clankalliance.backbeta.request.publication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPublicationRequest {

    private String token;

    private String publicationId;

}
