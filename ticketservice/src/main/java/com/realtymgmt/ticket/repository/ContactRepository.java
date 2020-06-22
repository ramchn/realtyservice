package com.realtymgmt.ticket.repository;

import org.springframework.data.repository.CrudRepository;

import com.realtymgmt.ticket.entity.Contact;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ContactRepository extends CrudRepository<Contact, Integer> {

}
