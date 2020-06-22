package com.realtymgmt.signup.repository;

import org.springframework.data.repository.CrudRepository;

import com.realtymgmt.signup.entity.Contact;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ContactRepository extends CrudRepository<Contact, Integer> {

}
