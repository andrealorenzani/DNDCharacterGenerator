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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character {

    @ApiModelProperty(required = false, example = "Thor")
    private String name;
    @ApiModelProperty(required = true, example = "10")
    private Integer strength;
    @ApiModelProperty(required = true, example = "16")
    private Integer dexterity;
    @ApiModelProperty(required = true, example = "18")
    private Integer constitution;
    @ApiModelProperty(required = true, example = "12")
    private Integer intelligence;
    @ApiModelProperty(required = true, example = "3")
    private Integer wisdom;
    @ApiModelProperty(required = true, example = "16")
    private Integer charisma;
    @ApiModelProperty(required = true, example = "156")
    private Integer maxHitPoint;
    @ApiModelProperty(required = false, example = "2018-07-04T08:14:25Z")
    private LocalDateTime generated;

}
