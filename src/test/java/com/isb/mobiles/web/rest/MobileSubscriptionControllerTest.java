package com.isb.mobiles.web.rest;

import com.isb.mobiles.MobilesApplication;
import com.isb.mobiles.domain.Customer;
import com.isb.mobiles.domain.MobileSubscription;
import com.isb.mobiles.domain.enumeration.Type;
import com.isb.mobiles.repository.jpa.CustomerRepository;
import com.isb.mobiles.repository.jpa.MobileSubscriptionRepository;
import com.isb.mobiles.service.MobileSubscriptionService;
import com.isb.mobiles.service.dto.MobileSubscriptionDTO;
import com.isb.mobiles.service.mapper.MobileSubscriptionMapper;
import com.isb.mobiles.web.rest.exceptions.GlobalRestExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MobilesApplication.class)
public class MobileSubscriptionControllerTest {

    private MockMvc mockMvc;

    private Pageable pageRequest;

    @Autowired
    private MobileSubscriptionService mobSubService;

    MobileSubscriptionController mobileSubscriptionController;

    @Autowired
    private MobileSubscriptionRepository mobSubRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MobileSubscriptionMapper mobSubMapper;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private GlobalRestExceptionHandler exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MobileSubscription mobSub;

    private static final String API_URL = "/api/subscriptions";

    private static final String DEFAULT_MSISDN = "+35677000008";
    private static final String UPDATED_MSISDN = "+35677080001";

    private static final Type DEFAULT_SERVICE_TYPE = Type.MOBILE_POSTPAID;
    private static final Type UPDATED_SERVICE_TYPE = Type.MOBILE_PREPAID;


    private static final Customer DEFAULT_CUSTOMER = new Customer(1001);
    private static final Customer UPDATED_CUSTOMER = new Customer(1000);


    private TestRestTemplate template = new TestRestTemplate();

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        final MobileSubscriptionController mobSubController = new MobileSubscriptionController(mobSubService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(mobSubController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();

    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileSubscription createEntity(EntityManager em) {
        MobileSubscription mobSub = MobileSubscription.builder()
                .msisdn(DEFAULT_MSISDN)
                .serviceType(DEFAULT_SERVICE_TYPE)
                .user(DEFAULT_CUSTOMER)
                .owner(DEFAULT_CUSTOMER)
                .serviceStartDate(new Date().getTime())
                .build();
        return mobSub;
    }

    @Before
    public void initTest() {
        mobSub = createEntity(em);
    }

    @Test
    public void createMobileSubscriptionTest() throws Exception {
        int databaseSizeBeforeCreate = mobSubRepository.findAll().size();

        // Create the MobileSubscriptionDTO
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);
        mockMvc.perform(post("/api/subscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isCreated());

        // Validate the Mobile Subscription in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeCreate + 1);
        MobileSubscription testMobSub = subscriptions.get(subscriptions.size() - 1);
        assertThat(testMobSub.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testMobSub.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);
        assertThat(testMobSub.getOwner().getId()).isEqualTo(DEFAULT_CUSTOMER.getId());
        assertThat(testMobSub.getOwner().getId()).isEqualTo(DEFAULT_CUSTOMER.getId());
    }

    @Test
    @Transactional
    public void createMobileSubscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mobSubRepository.findAll().size();

        // Create the Mobile Subscription with an existing ID
        mobSub.setId(1);
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        // An entity with an existing ID cannot be created, so this API call must fail
        mockMvc.perform(post("/api/subscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Mobile Subscription in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getALlMobileSubscriptionsTest() throws Exception {
        int databaseSizeBeforeCreate = mobSubRepository.findAll().size();

        // Initialize the database
        mobSubRepository.saveAndFlush(mobSub);

        // Get all the mobile subscriptions
        mockMvc.perform(MockMvcRequestBuilders.get(API_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(hasItem(mobSub.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getMobileSubscriptionTest() throws Exception {
        // Initialize the database
        mobSubRepository.saveAndFlush(mobSub);

        // Get all the mobile subscriptions
        mockMvc.perform(get("/api/subscriptions/{id}", mobSub.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void searchTest() throws Exception {

        // Initialize the database
        mobSubRepository.saveAndFlush(mobSub);

        // Get all the mobile subscriptions
        mockMvc.perform(MockMvcRequestBuilders.get("/api/subscriptions/search?query=77"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    public void updateTypeNonExistingMobileSubscription() throws Exception {
        int databaseSizeBeforeUpdate = mobSubRepository.findAll().size();

        // Create the Mobile Subscription
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        // If the entity does not exist in DB, it will throw MobileSubscriptionNotFound exception
        mockMvc.perform(put("/api/subscriptions/{id}/type", 100)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isNotFound());

        // Validate the Mobile Subscription in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void updateOwnerNonExistingMobileSubscription() throws Exception {
        int databaseSizeBeforeUpdate = mobSubRepository.findAll().size();

        // Create the Mobile Subscription
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        // If the entity does not exist in DB, it will throw MobileSubscriptionNotFound exception
        mockMvc.perform(put("/api/subscriptions/{id}/owner", 100)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isNotFound());

        // Validate the Mobile Subscription in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void updateUserNonExistingMobileSubscription() throws Exception {
        int databaseSizeBeforeUpdate = mobSubRepository.findAll().size();

        // Create the Mobile Subscription
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        // If the entity does not exist in DB, it will throw MobileSubscriptionNotFound exception
        mockMvc.perform(put("/api/subscriptions/{id}/user", 100)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isNotFound());

        // Validate the Mobile Subscription in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void updateUser() throws Exception {
        MobileSubscription saved = mobSubRepository.saveAndFlush(mobSub);
        int databaseSizeBeforeUpdate = mobSubRepository.findAll().size();
        mobSub.setUser(UPDATED_CUSTOMER);

        // Create the Mobile Subscription
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        mockMvc.perform(put("/api/subscriptions/" + saved.getId() + "/user", mobSubDTO)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isOk());

        // Validate the Game in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void updateNonExistentUserTest() throws Exception {
        int databaseSizeBeforeUpdate = mobSubRepository.findAll().size();

        mobSub.setUser(new Customer(2000));

        // Create the Mobile Subscription
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        // If the customer does not exist in DB, it will throw CustomerNotFoundException
        mockMvc.perform(put("/api/subscriptions/1/user", mobSubDTO)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isNotFound());

        // Validate the Game in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void updateNonExistentOwnerTest() throws Exception {
        int databaseSizeBeforeUpdate = mobSubRepository.findAll().size();

        mobSub.setOwner(new Customer(2000));

        // Create the Mobile Subscription
        MobileSubscriptionDTO mobSubDTO = mobSubMapper.toDto(mobSub);

        // If the customer does not exist in DB, it will throw CustomerNotFoundException
        mockMvc.perform(put("/api/subscriptions/1/owner", mobSubDTO)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mobSubDTO)))
                .andExpect(status().isNotFound());

        // Validate the Mobile Subscription in the database
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteNonExistingMobileSubscriptionTest() throws Exception {

        int databaseSizeBeforeDelete = mobSubRepository.findAll().size();

        mockMvc.perform(delete("/api/subscriptions/{id}", 100)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        // Validate the database has minus one record
        List<MobileSubscription> subscriptions = mobSubRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeDelete);
    }



}

