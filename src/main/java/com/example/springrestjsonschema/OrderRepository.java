package com.example.springrestjsonschema;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(/*excerptProjection = OrderProjection.class*/)
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

	List<Order> findByStatus(@Param("status") Order.Status status);
}