package com.university.api.service.events;

import com.university.api.service.models.Course;
import com.university.api.service.services.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class CourseModelListener extends AbstractMongoEventListener<Course> {
    private final SequenceGeneratorService sequenceGenerator;

    @Autowired
    public CourseModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Course> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Course.SEQUENCE_NAME));
        }
    }
}
