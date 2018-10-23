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

package name.lorenzani.andrea.dnd.generator.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String>  {

    private String format;

    private boolean nullable;

    @Override
    public void initialize(DateFormat dateFormatAnnotation) {
        format = dateFormatAnnotation.value();
        nullable = dateFormatAnnotation.nullable();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null) {
            return validateNullableDate(context);
        }
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean validateNullableDate(ConstraintValidatorContext context) {
        if (nullable) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate("may not be empty")
                .addConstraintViolation();
        return false;
    }
}
