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

import name.lorenzani.andrea.dnd.generator.model.CharRequest;
import name.lorenzani.andrea.dnd.generator.model.Character;
import name.lorenzani.andrea.dnd.generator.model.Characters;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.ClassResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.RaceResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CharacterGeneratorTest {

    private CharacterGenerator sut;

    @Mock
    private DndApiInvoker dndApiInvoker;
    @Mock
    private DiceRoller diceRoller;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        ClassResponse clazz = new ClassResponse();
        clazz.setName("mockclazz");
        clazz.setHitDice(5);
        CompletableFuture<Optional<ClassResponse>> classInfo = CompletableFuture.completedFuture(Optional.of(clazz));
        when(dndApiInvoker.retrieveClassInfo(anyString())).thenReturn(classInfo);
        RaceResponse race = new RaceResponse();
        race.setName("mockrace");
        race.setAbility_bonuses(new Integer[]{1,1,1,1,1,1});
        CompletableFuture<Optional<RaceResponse>> raceInfo = CompletableFuture.completedFuture(Optional.of(race));
        when(dndApiInvoker.retrieveRaceInfo(anyString())).thenReturn(raceInfo);
        when(diceRoller.generateCharWith4d6()).thenReturn(Arrays.asList(1,2,3,4,5,6));
        sut = new CharacterGenerator(dndApiInvoker, diceRoller);
    }

    @Test
    public void shouldGenerateWith4d6() throws Exception {
        Character character = sut.generateWith4D6(1).get(0);
        assertEquals(1, character.getStrength().longValue());
        assertEquals(2, character.getDexterity().longValue());
        assertEquals(3, character.getConstitution().longValue());
        assertEquals(4, character.getIntelligence().longValue());
        assertEquals(5, character.getWisdom().longValue());
        assertEquals(6, character.getCharisma().longValue());
    }

    @Test
    public void shouldGenerateAChar() throws Exception {
        CharRequest req = new CharRequest();
        req.setClassName("mockclazz");
        req.setLevel(2);
        req.setRace("mockrace");

        Characters characters = sut.genChar(req, 1).get();

        assertThat(characters.getCharacters(), hasSize(1));
        assertThat(characters.getCharacters().get(0), not(nullValue()));

        Character character = characters.getCharacters().get(0);
        assertEquals(2, character.getStrength().longValue());
        assertEquals(3, character.getDexterity().longValue());
        assertEquals(4, character.getConstitution().longValue());
        assertEquals(5, character.getIntelligence().longValue());
        assertEquals(6, character.getWisdom().longValue());
        assertEquals(7, character.getCharisma().longValue());
    }

    @Test
    public void shouldGenerateNoChar() throws Exception {
        CharRequest req = new CharRequest();
        Characters characters = sut.genChar(req, -1).get();
        assertThat(characters.getCharacters(), hasSize(0));
    }

    @Test
    public void shouldBeResilientToNoRace() throws Exception {
        CharRequest req = new CharRequest();
        req.setClassName("mockclazz");
        req.setLevel(2);
        req.setRace("mockrace");

        CompletableFuture<Optional<RaceResponse>> raceInfo = CompletableFuture.completedFuture(Optional.empty());
        when(dndApiInvoker.retrieveRaceInfo(anyString())).thenReturn(raceInfo);

        Characters characters = sut.genChar(req, 1).get();
        assertThat(characters.getCharacters(), hasSize(1));

        Character character = characters.getCharacters().get(0);
        assertEquals(1, character.getStrength().longValue());
        assertEquals(2, character.getDexterity().longValue());
        assertEquals(3, character.getConstitution().longValue());
        assertEquals(4, character.getIntelligence().longValue());
        assertEquals(5, character.getWisdom().longValue());
        assertEquals(6, character.getCharisma().longValue());
    }

    @Test
    public void shouldBeResilientToNoClass() throws Exception {
        CharRequest req = new CharRequest();
        req.setClassName("mockclazz");
        req.setLevel(2);
        req.setRace("mockrace");

        CompletableFuture<Optional<ClassResponse>> classInfo = CompletableFuture.completedFuture(Optional.empty());
        when(dndApiInvoker.retrieveClassInfo(anyString())).thenReturn(classInfo);

        Characters characters = sut.genChar(req, 1).get();
        assertThat(characters.getCharacters(), hasSize(1));

        Character character = characters.getCharacters().get(0);
        assertEquals(2, character.getStrength().longValue());
        assertEquals(3, character.getDexterity().longValue());
        assertEquals(4, character.getConstitution().longValue());
        assertEquals(5, character.getIntelligence().longValue());
        assertEquals(6, character.getWisdom().longValue());
        assertEquals(7, character.getCharisma().longValue());
    }

}