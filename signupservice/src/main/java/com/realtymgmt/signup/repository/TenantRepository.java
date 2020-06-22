package com.realtymgmt.signup.repository;

import org.springframework.data.repository.CrudRepository;

import com.realtymgmt.signup.entity.Tenant;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface TenantRepository extends CrudRepository<Tenant, Integer> {

}
