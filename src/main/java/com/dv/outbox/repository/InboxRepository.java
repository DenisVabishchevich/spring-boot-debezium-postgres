package com.dv.outbox.repository;

import com.dv.outbox.model.InboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<InboxMessage, String> {
}
