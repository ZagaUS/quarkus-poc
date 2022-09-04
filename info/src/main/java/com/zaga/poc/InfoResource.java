package com.zaga.poc;

import java.util.Set;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.zaga.poc.client.personality.PersonalityService;
import com.zaga.poc.client.personality.Personality;
import com.zaga.poc.client.locality.LocalityService;
import com.zaga.poc.client.locality.Locality;


@Path("/info")
public class InfoResource {

    @Inject
    @RestClient
    PersonalityService personalityService;

    @Inject
    @RestClient
    LocalityService localityService;

    @GET
    @Path("/")
    public Set<Info> getInfo() {
        Set<Personality> personalities = personalityService.getAll();
        Set<Locality> localities = localityService.getAll();
        
        return new InfoBuilder()
            .withLocalities(List.copyOf(localities))
            .withPersonalities(List.copyOf(personalities))
            .build();
    }
}
