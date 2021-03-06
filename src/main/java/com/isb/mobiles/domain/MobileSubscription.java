package com.isb.mobiles.domain;

import com.isb.mobiles.domain.enumeration.Type;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "subscription", type = "mobile")
@DynamicUpdate
public class MobileSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String msisdn;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id_owner")
    private Customer owner;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id_user")
    private Customer user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name= "service_type", nullable = false)
    private Type serviceType;

    @NotNull
    @Column(name = "service_start_date", nullable = false)
    private Long serviceStartDate;

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.serviceStartDate = now.getTime();
    }

}
