package com.SmsCallback.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.SmsCallback.Model.callback_arch;


@Repository
@Transactional
public interface callbackRepository extends JpaRepository<callback_arch, Long> {
	  
}

