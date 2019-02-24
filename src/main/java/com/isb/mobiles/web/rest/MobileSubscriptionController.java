package com.isb.mobiles.web.rest;

import com.isb.mobiles.domain.enumeration.Type;
import com.isb.mobiles.service.MobileSubscriptionService;
import com.isb.mobiles.service.dto.MobileSubscriptionDTO;
import com.isb.mobiles.web.rest.errors.BadRequestAlertException;
import com.isb.mobiles.web.rest.errors.MobileSubscriptionNotFoundException;
import com.isb.mobiles.web.rest.util.PaginationUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Mobile Subscriptions.
 */
@RestController
@Slf4j
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value="mobilesubscriptions", description="Operations pertaining to mobile subscriptions")
public class MobileSubscriptionController {

    private final MobileSubscriptionService mobileSubscriptionService;

    private static final String ENTITY_NAME = "MobileSubscription";

    public MobileSubscriptionController(MobileSubscriptionService mobileSubscriptionService) {
        this.mobileSubscriptionService = mobileSubscriptionService;
    }

    /**
     * POST  /subscriptions : Create a new mobile subscription.
     *
     * @param mobileSubscriptionDTO the mobileSubscriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mobileSubscriptionDTO,
     * or with status 400 (Bad Request) if the subscription's id already exists
     */
    @PostMapping(value = "/subscriptions", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "${swagger.sub-controller.create.value}", response = MobileSubscriptionDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created mobile subscription")
    })
    public ResponseEntity<MobileSubscriptionDTO> create(
            @ApiParam(value = "${swagger.sub-controller.create.param}")
            @Valid @RequestBody MobileSubscriptionDTO mobileSubscriptionDTO) {

        log.debug("REST request to save Mobile Subscription : {}", mobileSubscriptionDTO);
        if (mobileSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("Mobile subscription cannot be added, already has an ID", ENTITY_NAME, "id exists");
        }
        MobileSubscriptionDTO result = mobileSubscriptionService.save(mobileSubscriptionDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * GET  /subscriptions : get all mobile subscriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mobile subscriptions in body
     */
    @GetMapping("/subscriptions")
    @ApiOperation(value = "${swagger.sub-controller.get-all.value}", response = List.class)
    public ResponseEntity<List<MobileSubscriptionDTO>> getAll(Pageable pageable) {

        log.debug("REST request to get a page of Mobile Subscriptions");
        Page<MobileSubscriptionDTO> page = mobileSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /subscriptions/:id : get mobile subscription by id.
     *
     * @param id the id of mobile subscription
     * @return the ResponseEntity with status 200 (OK) and the mobile subscription in body
     */
    @GetMapping("/subscriptions/{id}")
    @ApiOperation(value = "${swagger.sub-controller.get.value}", response = MobileSubscriptionDTO.class)
    public ResponseEntity<MobileSubscriptionDTO> get(
            @ApiParam(value = "${swagger.sub-controller.get.param}")
            @PathVariable Integer id) {
        log.debug("REST request to get Mobile Subscription by id {}", id);
        Optional<MobileSubscriptionDTO> mobSub = mobileSubscriptionService.findOne(id);

        if (mobSub.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mobSub.get(), HttpStatus.OK);
    }

    /**
     * GET  /subscriptions?query=:query : search for mobile subscriptions corresponding to the query.
     *
     * @param query     the query of the mobile subscriptions search
     * @param pageable  the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mobile subscriptions in body
     */
    @GetMapping("/subscriptions/search")
    @ApiOperation(value = "${swagger.sub-controller.search.value}", response = List.class)
    public ResponseEntity<List<MobileSubscriptionDTO>> search(
            @ApiParam(value="${swagger.sub-controller.search.param}")
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            Pageable pageable) {
        log.debug("REST request to search for a page of Mobile Subscriptions for query {}", query);
        Page<MobileSubscriptionDTO> page = mobileSubscriptionService.search("*" + query + "*", pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subscriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * PUT /subscriptions/:id : Updates owner and/or user of Mobile Subscription.
     *
     * @param mobileSubscriptionDTO owner/user of mobile subscription to update
     * @param id id of the mobile subscription
     * @return the ResponseEntity with status 200 (OK) and with body the updated mobile subscription
     * @throws MobileSubscriptionNotFoundException 404 (Bad Request) if the mobile subscription is not found
     */
    @PutMapping("/subscriptions/{id}")
    public ResponseEntity<MobileSubscriptionDTO> update(
            @Valid @RequestBody MobileSubscriptionDTO mobileSubscriptionDTO,
            @PathVariable Integer id) {
        log.debug("REST request to update Mobile Subscription : {}", mobileSubscriptionDTO);
        MobileSubscriptionDTO mobSubUpdated = mobileSubscriptionService.update(id, mobileSubscriptionDTO);
        return new ResponseEntity<MobileSubscriptionDTO>(mobSubUpdated, HttpStatus.OK);
    }

    /**
     * PUT /subscriptions/:id/type : Updates service type of Mobile Subscription.
     *
     * @param mobileSubscriptionDTO mobileSubscriptionDTO to update
     * @param id id of the mobile subscription
     * @return the ResponseEntity with status 200 (OK) and with body the updated mobile subscription
     * @throws MobileSubscriptionNotFoundException 404 (Bad Request) if the mobile subscription is not found
     */
    @PutMapping("/subscriptions/{id}/type")
    @ApiOperation(value = "${swagger.sub-controller.update-type.value}", response = MobileSubscriptionDTO.class)
    public ResponseEntity<MobileSubscriptionDTO> updateType (
            @ApiParam(value="${swagger.sub-controller.update-type.param}")
            @Valid @RequestBody MobileSubscriptionDTO mobileSubscriptionDTO,
            @ApiParam(value="${swagger.sub-controller.update-type.id}")
            @PathVariable Integer id) throws MobileSubscriptionNotFoundException, IllegalArgumentException {
        log.debug("REST request to update service type of Mobile Subscription service : {}", mobileSubscriptionDTO.getServiceType());
        MobileSubscriptionDTO mobSubUpdated = mobileSubscriptionService.updateType(id, mobileSubscriptionDTO.getServiceType());
        return new ResponseEntity<MobileSubscriptionDTO>(mobSubUpdated, HttpStatus.OK);
    }


    /**
     * DELETE  /subscriptions/:id : delete the "id" Mobile Subscription.
     *
     * @param id the id of the MobileSubscriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subscriptions/{id}")
    @ApiOperation(value = "${swagger.sub-controller.delete.value}")
    public ResponseEntity<Void> delete(
            @ApiParam(value="${swagger.sub-controller.delete.id}")
            @PathVariable Integer id) {
        log.debug("REST request to delete Mobile Subscription : {}", id);
        mobileSubscriptionService.delete(id);
        return ResponseEntity.ok().build();
    }

}

