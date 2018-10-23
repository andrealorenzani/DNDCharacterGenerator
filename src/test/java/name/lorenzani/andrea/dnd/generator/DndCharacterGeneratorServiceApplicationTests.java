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

package name.lorenzani.andrea.dnd.generator;

import io.github.robwin.swagger.test.SwaggerAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DndCharacterGeneratorServiceApplicationTests {

	@LocalServerPort
	int serverPort;

	@Test
	public void contextLoads() {
	}

	@Test
	public void validateThatImplementationMatchesDocumentationSpecification() throws URISyntaxException {
		String designFirstSwagger = Paths.get(DndCharacterGeneratorServiceApplicationTests.class.
				getResource("/swagger/swagger.yml").toURI()).toString();
		SwaggerAssertions.assertThat("http://localhost:"+serverPort+"/v2/api-docs?group=Api")
				.isEqualTo(designFirstSwagger);
	}

}
