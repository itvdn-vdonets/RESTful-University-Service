package com.university.api.service.events;

import com.university.api.service.models.Student;
import com.university.api.service.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class StudentModelListener extends AbstractMongoEventListener<Student> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public StudentModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

   @Override
    public void onBeforeConvert(BeforeConvertEvent<Student> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Student.SEQUENCE_NAME));
        }
    }
}
