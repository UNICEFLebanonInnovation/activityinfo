package org.activityinfo.model.type.time;

import org.activityinfo.model.form.FormClassVisitor;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.record.Record;
import org.activityinfo.model.type.*;

/**
 * Value type that represents a continuous interval between two {@link org.activityinfo.model.type.time.LocalDate}s,
 * starting on {@code startDate}, inclusive, and ending on {@code endDate}, inclusive.
 */
public class LocalDateIntervalType implements FieldType, TemporalType {

    public interface TypeClass extends RecordFieldTypeClass<LocalDateInterval>, SingletonTypeClass {}

    public static final TypeClass TYPE_CLASS = new TypeClass() {
        @Override
        public String getId() {
            return "localDateInterval";
        }

        @Override
        public String getLabel() {
            return "Date Range";
        }

        @Override
        public FieldType createType() {
            return LocalDateIntervalType.INSTANCE;
        }

        @Override
        public LocalDateInterval deserialize(Record record) {
            return LocalDateInterval.fromRecord(record);
        }
    };

    public static final LocalDateIntervalType INSTANCE = new LocalDateIntervalType();

    private LocalDateIntervalType() {
    }

    @Override
    public FieldTypeClass getTypeClass() {
        return TYPE_CLASS;
    }

    @Override
    public <T> T accept(FormField field, FormClassVisitor<T> visitor) {
        return visitor.visitLocalDateIntervalField(field, this);
    }

    @Override
    public Record asRecord() {
        return TypeFieldType.asRecord(this);
    }

}
