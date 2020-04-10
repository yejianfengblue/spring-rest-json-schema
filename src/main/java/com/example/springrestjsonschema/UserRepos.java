package com.example.springrestjsonschema;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepos extends CrudRepository<User, Long> {
}
