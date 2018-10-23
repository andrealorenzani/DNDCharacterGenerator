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

import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.ClassResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.GenericServiceListResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.NameUrlResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.RaceResponse;
import name.lorenzani.andrea.dnd.generator.properties.DndApiProperties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DndApiInvokerTest {

    private DndApiInvoker sut;

    @Mock
    private RestTemplate mockRestTemplate;

    @Mock
    private DndApiProperties dndApiProperties;

    private String defaultUrl = "http://www.fakeurl.tv";
    private ClassResponse clazz = new ClassResponse();
    private NameUrlResponse nures = new NameUrlResponse();
    private RaceResponse race = new RaceResponse();

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        race.setAbility_bonuses(new Integer[]{0,0,0,0,0,0});
        race.setName("mock");

        clazz.setName("mock");
        clazz.setHitDice(6);

        nures.setUrl("fakeurl");
        nures.setName("mock");

        GenericServiceListResponse generic = new GenericServiceListResponse();
        generic.setCount(1);
        generic.setResults(Collections.singletonList(nures));

        when(dndApiProperties.getApis()).thenReturn(Collections.emptyMap());
        when(dndApiProperties.getDefaultUrl()).thenReturn(defaultUrl);
        when(mockRestTemplate.getForEntity(anyString(), eq(GenericServiceListResponse.class))).thenReturn(new ResponseEntity<GenericServiceListResponse>(generic, HttpStatus.OK));
        when(mockRestTemplate.getForEntity(anyString(), eq(RaceResponse.class))).thenReturn(new ResponseEntity<RaceResponse>(race, HttpStatus.OK));
        when(mockRestTemplate.getForEntity(anyString(), eq(ClassResponse.class))).thenReturn(new ResponseEntity<ClassResponse>(clazz, HttpStatus.OK));

        sut = new DndApiInvoker(mockRestTemplate, dndApiProperties);
    }

    @Test
    public void shouldRetrieveClass() throws InterruptedException, ExecutionException {
        Optional<ClassResponse> test = sut.retrieveClassInfo("mock").get();
        assertTrue(test.isPresent());
        assertEquals(test.get(), clazz);
    }

    @Test
    public void shouldRetrieveRace() throws InterruptedException, ExecutionException {
        Optional<RaceResponse> test = sut.retrieveRaceInfo("mock").get();
        assertTrue(test.isPresent());
        assertEquals(test.get(), race);
    }

    @Test
    public void shouldHandleExceptions() throws InterruptedException, ExecutionException {
        when(mockRestTemplate.getForEntity(anyString(), eq(GenericServiceListResponse.class))).thenThrow(new RuntimeException("mock"));

        CompletableFuture<Optional<RaceResponse>> test = sut.retrieveRaceInfo("mock");
        do{}
        while(!test.isDone());
        assertTrue(!test.isCompletedExceptionally());
        assertTrue(!test.get().isPresent());
    }

}
