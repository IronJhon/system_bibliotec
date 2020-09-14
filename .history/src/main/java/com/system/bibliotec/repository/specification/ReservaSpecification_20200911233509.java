package com.system.bibliotec.repository.specification;

import com.system.bibliotec.model.AbstractAuditingEntity_;
import com.system.bibliotec.model.Reservas;
import com.system.bibliotec.model.Reservas_;

import org.springframework.data.jpa.domain.Specification;

import ch.qos.logback.core.status.Status;

public class ReservaSpecification {

    

    public static Specification<Reservas> porID(Long id) {
		if (id == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.equal(root.get(AbstractAuditingEntity_.ID), id);
		}
	}


    public static Specification<Reservas> porStatus(Status status) {
		if (status == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.equal(root.get(Reservas_.status), status);
		}
	}


    
}