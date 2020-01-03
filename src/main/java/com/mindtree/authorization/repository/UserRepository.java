package com.mindtree.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindtree.authorization.entity.PrincipalData;
import com.mindtree.authorization.entity.User;

@Repository
public interface UserRepository extends JpaRepository<PrincipalData, String> {

}
