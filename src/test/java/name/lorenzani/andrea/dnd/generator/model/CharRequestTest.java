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

import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

public class CharRequestTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldValidateCharRequest() {
        //Given
        CharRequest guest = new CharRequest();

        //When
        List<String> validationMessages = validator.validate(guest).stream().
                map(constraint -> constraint.getPropertyPath() + " " + constraint.getMessage()).collect(toList());

        //Then
        assertThat(validationMessages, hasSize(3));
        assertThat(validationMessages, hasItem("race must not be empty"));
        assertThat(validationMessages, hasItem("className must not be empty"));
        assertThat(validationMessages, hasItem("level must not be null"));
    }
}