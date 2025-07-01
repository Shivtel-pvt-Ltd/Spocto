package com.SmsCallback.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SmsCallback.Model.callback;

public interface CallBackRepositoryUniq extends JpaRepository<callback, Long> {

}
