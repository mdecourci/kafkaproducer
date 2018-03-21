package com.netpod.kafkdocker.endpoints;

import com.netpod.kafkdocker.SpringKafkaApplication;
import com.netpod.kafkdocker.documents.Employee;
import com.netpod.kafkdocker.repository.EmployeeRepository;
import com.netpod.kafkdocker.repository.MessageRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringKafkaApplication.class)
@WebAppConfiguration
public class KafkaConsumerControllerTest {


    private static String BOOT_TOPIC = "acxTopic";

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 2, BOOT_TOPIC);

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Employee> employeeList = new ArrayList<>();

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter, not(nullValue()));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
    }

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        List<String> messages =
                Arrays.asList("fullName=James Bond,email=james.bond@gmail.com,managerEmail=m.boss@gmail.com",
                        "fullName=Harvey Nicols,email=harvey.nicols@gmail.com,managerEmail=harveys.boss@gmail.com");

        for (String message : messages) {
            messageRepository.save(message);

            Employee employee = Employee.builder().message(message).build();
            employeeRepository.save(employee);
        }

    }

    @After
    public void tearDown() throws Exception {
        messageRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void getEmployee() throws Exception {

        String email = "harvey.nicols@gmail.com";

        mockMvc.perform(get("/consumer/employee/{email}/", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.fullName", is("Harvey Nicols")))
                .andExpect(jsonPath("$.managerEmail", is("harveys.boss@gmail.com")));
    }

    @Test
    public void getAllEmployees() throws Exception {

        mockMvc.perform(get("/consumer/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$[0].email", is("james.bond@gmail.com")))
                .andExpect(jsonPath("$[0].fullName", is("James Bond")))
                .andExpect(jsonPath("$[0].managerEmail", is("m.boss@gmail.com")))
                .andExpect(jsonPath("$[1].id", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$[1].email", is("harvey.nicols@gmail.com")))
                .andExpect(jsonPath("$[1].fullName", is("Harvey Nicols")))
                .andExpect(jsonPath("$[1].managerEmail", is("harveys.boss@gmail.com")));
    }

    @Test
    public void getMessage() throws Exception {

        String email = "harvey.nicols@gmail.com";

        mockMvc.perform(get("/consumer/message/{email}/", email)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", isEmptyOrNullString()))
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.fullName", is("Harvey Nicols")))
                .andExpect(jsonPath("$.managerEmail", is("harveys.boss@gmail.com")));
    }

    @Test
    public void getAllMessages() throws Exception {

        mockMvc.perform(get("/consumer/message"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", isEmptyOrNullString()))
                .andExpect(jsonPath("$[0].email", is("james.bond@gmail.com")))
                .andExpect(jsonPath("$[0].fullName", is("James Bond")))
                .andExpect(jsonPath("$[0].managerEmail", is("m.boss@gmail.com")))
                .andExpect(jsonPath("$[1].id", isEmptyOrNullString()))
                .andExpect(jsonPath("$[1].email", is("harvey.nicols@gmail.com")))
                .andExpect(jsonPath("$[1].fullName", is("Harvey Nicols")))
                .andExpect(jsonPath("$[1].managerEmail", is("harveys.boss@gmail.com")));
    }
}