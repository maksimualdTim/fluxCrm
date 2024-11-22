package uz.fluxCrm.fluxCrm.crm.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isMain;

    @Column(name = "account_id")
    private Long accountId;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id")
    @JsonBackReference
    private Pipeline pipeline;

    @OneToMany(mappedBy = "status")
    @JsonManagedReference
    private List<Lead> leads;

}
