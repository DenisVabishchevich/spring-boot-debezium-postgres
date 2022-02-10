package com.dv.outbox.service;

import com.dv.outbox.model.InboxMessage;
import com.dv.outbox.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeduplicationService {

    private final InboxRepository inboxRepository;

    @Transactional(readOnly = true)
    public boolean exists(String id) {
        return inboxRepository.existsById(id);
    }

    @Transactional
    public InboxMessage saveInboxMessage(InboxMessage message) {
        return inboxRepository.save(message);
    }
}
