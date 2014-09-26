package org.activityinfo.model.form;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.activityinfo.model.record.Record;
import org.activityinfo.model.record.RecordBuilder;
import org.activityinfo.model.record.Records;
import org.activityinfo.model.resource.ResourceId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A logical group of {@code FormElements}
 *
 */
public class FormSection extends FormElement implements FormElementContainer {

    private final ResourceId id;
    private String label;
    private final List<FormElement> elements = Lists.newArrayList();

    public FormSection(ResourceId id) {
        Preconditions.checkNotNull(id);
        this.id = id;
    }

    public ResourceId getId() {
        return id;
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<FormElement> getElements() {
        return elements;
    }

    @Override
    public FormSection addElement(FormElement element) {
        elements.add(element);
        return this;
    }

    @Override
    public Record asRecord() {
        RecordBuilder record = Records.builder();
        record.set("id", id.asString());
        record.set("label", label);
        record.set("type", "section");
        record.set("elements", asRecordList(getElements()));
        return record.build();
    }

    @Override
    public String toString() {
        return "FormSection{" +
                "id=" + id +
                ", label=" + label +
                '}';
    }
}
