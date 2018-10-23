/***
*   Copyright 2018 Andrea Lorenzani
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*
***/

package name.lorenzani.andrea.dnd.generator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.ClassResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.GenericServiceListResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.NameUrlResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.RaceResponse;
import name.lorenzani.andrea.dnd.generator.properties.DndApiProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DndApiInvoker {
    private final RestTemplate restTemplate;
    private final DndApiProperties dndApiProperties;

    public CompletableFuture<Optional<ClassResponse>> retrieveClassInfo(String className){
        return CompletableFuture.supplyAsync(() ->retrieveInfo("classes", className, ClassResponse.class))
                .exceptionally((err) -> {
                    log.error("Error retrieving class {}: {}", className, err);
                    return Optional.empty();
                });
    }

    public CompletableFuture<Optional<RaceResponse>> retrieveRaceInfo(String raceName){
        return CompletableFuture.supplyAsync(() ->retrieveInfo("races", raceName, RaceResponse.class))
                .exceptionally((err)->{
                    log.error("Error retrieving race {}: {}", raceName, err);
                    return Optional.empty();
                });
    }

    private <T> Optional<T> retrieveInfo(String name, String value, Class<T> classT) {
        String url = dndApiProperties.getApis().getOrDefault(name,dndApiProperties.getDefaultUrl()+name+"/");
        log.info("Retrieving from url '{}'", url);
        GenericServiceListResponse resp = restTemplate.getForEntity( url, GenericServiceListResponse.class).getBody();
        Optional<T> response = Optional.ofNullable(resp)
                .map(GenericServiceListResponse::getResults)
                .orElse(Collections.emptyList())
                .stream()
                .filter(nameUrl -> value.toLowerCase().equals(nameUrl.getName().toLowerCase()))
                .map(NameUrlResponse::getUrl)
                .map(classUrl -> restTemplate.getForEntity(classUrl, classT).getBody())
                .findFirst();
        log.info("{}etrieved {} with value {}", (response.isPresent())? "R":"Not r", name, value);
        return response;
    }

}
