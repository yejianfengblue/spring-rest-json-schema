/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.springrestjsonschema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An order.
 *
 * @author Oliver Gierke
 */@Slf4j
@Entity
@Getter @AllArgsConstructor
@ToString(exclude = "lineItems")
@Table(name = "RBOrder")
public class Order {

	@Id @GeneratedValue(strategy = GenerationType.AUTO) @JsonIgnore
	private Long id;
	private @Version Long version;
	private final Location location;
	private final LocalDateTime orderedDate;
	private Status status;

	@OrderColumn //
	@Column(unique = true) //
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //
	private final List<LineItem> lineItems = new ArrayList<>();

	/**
	 * Creates a new {@link Order} for the given {@link LineItem}s and {@link Location}.
	 * 
	 * @param lineItems must not be {@literal null}.
	 * @param location
	 *///@JsonCreator
	public Order(Collection<LineItem> lineItems, Location location) {
		this.location = location == null ? Location.TAKE_AWAY : location;
		this.status = Status.PAYMENT_EXPECTED;
		this.lineItems.addAll(lineItems);
		this.orderedDate = LocalDateTime.now();
	}

	/**
	 * Creates a new {@link Order} containing the given {@link LineItem}s.
	 * 
	 * @param items must not be {@literal null}.
	 */
	public Order(LineItem... items) {
		this(List.of(items), null);
	}

	public Order() {
		this(new LineItem[0]);
	}

	/**
	 * Marks the {@link Order} as payed.
	 */
	public Order markPaid() {

		if (isPaid()) {
			throw new IllegalStateException("Already paid order cannot be paid again!");
		}

		this.status = Status.PAID;

		return this;
	}

	/**
	 * Marks the {@link Order} as in preparation.
	 */
	public Order markInPreparation() {

		if (this.status != Status.PAID) {
			throw new IllegalStateException(
					String.format("Order must be in state payed to start preparation! Current status: %s", this.status));
		}

		this.status = Status.PREPARING;

		return this;
	}

	/**
	 * Marks the {@link Order} as prepared.
	 */
	public Order markPrepared() {

		if (this.status != Status.PREPARING) {
			throw new IllegalStateException(String
					.format("Cannot mark Order prepared that is currently not preparing! Current status: %s.", this.status));
		}

		this.status = Status.READY;

		return this;
	}

	public Order markTaken() {

		if (this.status != Status.READY) {
			throw new IllegalStateException(
					String.format("Cannot mark Order taken that is currently not paid! Current status: %s.", this.status));
		}

		this.status = Status.TAKEN;

		return this;
	}

	/**
	 * Returns whether the {@link Order} has been paid already.
	 * 
	 * @return
	 */
	public boolean isPaid() {
		return !this.status.equals(Status.PAYMENT_EXPECTED);
	}

	/**
	 * Returns if the {@link Order} is ready to be taken.
	 * 
	 * @return
	 */
	public boolean isReady() {
		return this.status.equals(Status.READY);
	}

	public boolean isTaken() {
		return this.status.equals(Status.TAKEN);
	}

	/**
	 * Enumeration for all the statuses an {@link Order} can be in.
	 * 
	 * @author Oliver Gierke
	 */
	public static enum Status {

		/**
		 * Placed, but not payed yet. Still changeable.
		 */
		PAYMENT_EXPECTED,

		/**
		 * {@link Order} was payed. No changes allowed to it anymore.
		 */
		PAID,

		/**
		 * The {@link Order} is currently processed.
		 */
		PREPARING,

		/**
		 * The {@link Order} is ready to be picked up by the customer.
		 */
		READY,

		/**
		 * The {@link Order} was completed.
		 */
		TAKEN;
	}
}
