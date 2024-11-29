package com.nisumpruebatecnica.musers.repositories;

import com.nisumpruebatecnica.musers.entity.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicUserRepository extends JpaRepository<BasicUser,Integer> {
    public BasicUser findByEmailLike(String email);
    public BasicUser findByUsernameLike(String username);
    public BasicUser findByJwtLike(String jwt);
}
