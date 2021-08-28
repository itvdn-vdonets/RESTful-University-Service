package com.university.api.service.events;

import com.university.api.service.models.Professor;
import com.university.api.service.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class ProfessorsModelListener extends AbstractMongoEventListener<Professor> {
    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public ProfessorsModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Professor> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Professor.SEQUENCE_NAME));
        }
    }
}
