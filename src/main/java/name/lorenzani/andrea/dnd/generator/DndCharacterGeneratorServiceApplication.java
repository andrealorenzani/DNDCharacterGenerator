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

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static com.google.common.base.Predicates.not;

@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableAsync
@ComponentScan(basePackages = {"name.lorenzani.andrea.dnd"})
public class DndCharacterGeneratorServiceApplication {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("name.lorenzani.andrea.dnd");
		return marshaller;
	}

	@Bean
	public WebServiceTemplate webServiceTemplate() {
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMarshaller(marshaller());
		template.setUnmarshaller(marshaller());
		return template;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}

	/*@Bean
	public TemplateEngine templateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(templateResolver());
		return templateEngine;
	}


	private ITemplateResolver templateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateResolver.setCacheable(false);

		return templateResolver;
	}*/

	@Value("${swagger.api.version}")
	String swaggerApiVersion;

	@Bean
	public Docket swaggerApiDocumentation(){
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
				.groupName("Api")
				.directModelSubstitute(LocalDate.class, String.class)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("name.lorenzani.andrea.dnd.generator"))
				.build()
				.pathMapping("/");
	}

	@Bean
	public Docket swaggerInternalDocumentation(){
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Internal")
				.select()
				.apis(not(RequestHandlerSelectors.basePackage("name.lorenzani.andrea.dnd.generator")))
				.build()
				.pathMapping("/dndgenerator")
				.tags(new Tag("Status endpoints", "All endpoints relating to the status of the Microservice"));
	}

	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(
			@Value("classpath*:mappings/*.xml")
					Resource[] resources) throws Exception {
		final DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
		// Other configurations
		dozerBeanMapperFactoryBean.setMappingFiles(resources);
		return dozerBeanMapperFactoryBean;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("DND Character Generator microservice")
				.description("API Documentation")
				.version(swaggerApiVersion)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(DndCharacterGeneratorServiceApplication.class, args);
	}
}
