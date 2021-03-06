package org.activityinfo.model.type;

import com.google.common.collect.Lists;
import org.activityinfo.model.resource.ResourceId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A FieldValue which references another FormInstance
 */
public final class Reference extends FieldValue {

    private final ResourceId id;

    private Reference(@Nonnull ResourceId id) {
        this.id = id;
    }

    public static Reference to(@Nonnull ResourceId id) {
        return new Reference(id);
    }

    public static Reference to(@Nonnull String resourceId) {
        return new Reference(ResourceId.create(resourceId));
    }

    public static List<Reference> to(Iterable<ResourceId> resourceIds) {
        List<Reference> refs = Lists.newArrayList();
        for(ResourceId id : resourceIds) {
            refs.add(Reference.to(id));
        }
        return refs;
    }

    /**
     * @return the id of the referenced {@code Resource}
     */
    public ResourceId getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reference reference = (Reference) o;

        return id.equals(reference.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "@" + id.asString();
    }


}
