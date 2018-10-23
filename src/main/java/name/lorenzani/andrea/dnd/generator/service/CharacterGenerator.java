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
import name.lorenzani.andrea.dnd.generator.model.CharRequest;
import name.lorenzani.andrea.dnd.generator.model.Character;
import name.lorenzani.andrea.dnd.generator.model.Characters;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.ClassResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.GenericServiceListResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.NameUrlResponse;
import name.lorenzani.andrea.dnd.generator.model.dndExternalApi.RaceResponse;
import name.lorenzani.andrea.dnd.generator.properties.DndApiProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class CharacterGenerator {

    private final DndApiInvoker dndApiInvoker;
    private final DiceRoller diceRoller;

    public CompletableFuture<Characters> genChar(CharRequest request, Integer nChar) {
        if(nChar<1) {
            CompletableFuture<Characters> res = new CompletableFuture<>();
            res.complete(new Characters(Collections.emptyList()));
            return res;
        }
        CompletableFuture<Optional<ClassResponse>> classInfo = dndApiInvoker.retrieveClassInfo(request.getClassName());
        CompletableFuture<Optional<RaceResponse>> raceInfo = dndApiInvoker.retrieveRaceInfo(request.getRace());
        CompletableFuture<List<Character>> generatedValues = CompletableFuture.supplyAsync(() -> generateWith4D6(nChar));
        CompletableFuture<List<Character>> withRace = raceInfo.thenCombine(generatedValues, (race, chars) -> chars.parallelStream().map(c -> {
            race.ifPresent(rc -> {
                c.setStrength(c.getStrength()+rc.getAbility_bonuses()[0]);
                c.setDexterity(c.getDexterity()+rc.getAbility_bonuses()[1]);
                c.setConstitution(c.getConstitution()+rc.getAbility_bonuses()[2]);
                c.setIntelligence(c.getIntelligence()+rc.getAbility_bonuses()[3]);
                c.setWisdom(c.getWisdom()+rc.getAbility_bonuses()[4]);
                c.setCharisma(c.getCharisma()+rc.getAbility_bonuses()[5]);
            });
            return c;
        }).collect(Collectors.toList()));
        CompletableFuture<List<Character>> complete = withRace.thenCombine(classInfo, (chars, claz) -> chars.parallelStream().map(c -> {
            c.setMaxHitPoint(diceRoller.maxHitPoint(claz.isPresent()? claz.get().getHitDice() : 6, request.getLevel(), getModificatorForCharacteristic(c.getConstitution())));
            c.setGenerated(LocalDateTime.now());
            return c;
        }).collect(Collectors.toList()));
        return complete.thenApply(Characters::new);
    }

    private Integer getModificatorForCharacteristic(Integer ch) {
        return (ch-10)/2;
    }

    List<Character> generateWith4D6(Integer nchar) {
        List<Character> res = new ArrayList<>(nchar);
        for(int i=0; i<nchar; i++) {
            List<Integer> values = diceRoller.generateCharWith4d6();
            res.add(Character.builder()
                             .strength(values.get(0))
                             .dexterity(values.get(1))
                             .constitution(values.get(2))
                             .intelligence(values.get(3))
                             .wisdom(values.get(4))
                             .charisma(values.get(5))
                             .name("Char "+i)
                             .build());
            log.debug("Generated character n. {} with {}", i, values);
        }
        return res;
    }


}
