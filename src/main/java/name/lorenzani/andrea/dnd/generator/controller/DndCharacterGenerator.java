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

package name.lorenzani.andrea.dnd.generator.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.lorenzani.andrea.dnd.generator.exception.BadRequest;
import name.lorenzani.andrea.dnd.generator.exception.InternalServerError;
import name.lorenzani.andrea.dnd.generator.model.CharRequest;
import name.lorenzani.andrea.dnd.generator.model.Character;
import name.lorenzani.andrea.dnd.generator.model.Characters;
import name.lorenzani.andrea.dnd.generator.model.ErrorMessage;
import name.lorenzani.andrea.dnd.generator.service.CharacterGenerator;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@Slf4j
@Api(tags = "generate", description = "Generate your new character")
public class DndCharacterGenerator {

    private CharacterGenerator characterGenerator;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Character.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorMessage.class)})
    @ApiOperation(value = "generateCharacter", notes = "Generate a new NPG based on the info provided")
    @PostMapping(value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE ,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Character generateCharacter(@Validated @RequestBody CharRequest request) {
        log.debug("Called /generate with {}", request);
        try {
            return characterGenerator.genChar(request, 1).get().getCharacters().get(0);
        } catch(InterruptedException|ExecutionException ex){
            throw new InternalServerError("Unable to generate the character ("+ex.getMessage()+")", ex);
        }
    }

    @ApiOperation(value = "genChars", notes = "Generate a number of similar characters")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Characters.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorMessage.class)})
    @PostMapping(value = "/generate/{numOfChars}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Characters genChars(@PathVariable(name = "numOfChars") Integer numOfChars,
                               @Validated @RequestBody CharRequest request) {
        log.debug("Called /generate/{numOfChars} with {}", request);
        try {
            return characterGenerator.genChar(request, 1).get();
        } catch(InterruptedException|ExecutionException ex){
            throw new InternalServerError("Unable to generate the "+numOfChars+" characters ("+ex.getMessage()+")", ex);
        }
    }

}
