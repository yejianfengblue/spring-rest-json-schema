/*
 * Copyright 2015-2019 the original author or authors.
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


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
class JacksonCustomizations {

	public @Bean
	Module restbucksModule() {
		return new RestbucksModule();
	}

	@SuppressWarnings("serial")
	static class RestbucksModule extends SimpleModule {

		public RestbucksModule() {

			setMixInAnnotation(Order.class, RestbucksModule.OrderMixin.class);
		}

		@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
		static abstract class OrderMixin {

			@JsonCreator
			public OrderMixin(Collection<LineItem> lineItems, Location location) {}
		}

	}
}
