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

package name.lorenzani.andrea.dnd.generator.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CharRequest {

    @NotNull
    @Min(1)
    @Max(25)
    @ApiModelProperty(required = true)
    private Integer level;

    @NotEmpty
    @ApiModelProperty(required = true, allowableValues = "dwarf,elf,halfling,human,dragonborn,gnome,half-elf,half-orc,tiefling")
    private String race;

    @JsonProperty("class")
    @NotEmpty
    @ApiModelProperty(value="class", required = true, allowableValues = "barbarian,bard,cleric,druid,fighter,monk,paladin,ranger,rogue,sorcerer,warlock,wizard")
    private String className;
}
