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

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import name.lorenzani.andrea.dnd.generator.DndCharacterGeneratorServiceApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DndCharacterGeneratorServiceApplication.class)
@AutoConfigureWireMock(port = 9999, stubs = "classpath:/stubs")
public abstract class ContractVerifierBaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

}