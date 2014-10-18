package org.activityinfo.legacy.shared.model;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.geo.GeoExtents;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * One-to-one DTO of the
 * {@link org.activityinfo.server.database.hibernate.entity.LocationType
 * LocationType} domain object.
 *
 * @author Alex Bertram
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)

public final class LocationTypeDTO extends BaseModelData implements EntityDTO, IsFormClass {

    public static final String NULL_LOCATION_TYPE_NAME = "Country";
    public static int NAME_MAX_LENGTH = 50;
    private Integer databaseId;
    private List<AdminLevelDTO> adminLevels;
    private GeoExtents countryBounds;

    public LocationTypeDTO() {
    }

    public LocationTypeDTO(int id, String name) {
        setId(id);
        setName(name);
    }

    @Override
    public ResourceId getResourceId() {
        return CuidAdapter.locationFormClass(getId());
    }

    public void setId(int id) {
        set("id", id);
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty @JsonView(DTOViews.Schema.class)
    public int getId() {
        return (Integer) get("id");
    }

    @Override
    public String getEntityName() {
        return "LocationType";
    }

    public void setName(String value) {
        set("name", value);
    }

    @JsonProperty @JsonView(DTOViews.Schema.class)
    public String getName() {
        return get("name");
    }

    public boolean isNationwide() {
        // hack!!
        return NULL_LOCATION_TYPE_NAME.equals(getName()) && getId() != 20301;
    }

    @JsonProperty("adminLevelId") @JsonView(DTOViews.Schema.class)
    public Integer getBoundAdminLevelId() {
        return get("boundAdminLevelId");
    }

    public void setBoundAdminLevelId(Integer id) {
        set("boundAdminLevelId", id);
    }

    public boolean isAdminLevel() {
        return getBoundAdminLevelId() != null;
    }

    public void setWorkflowId(String workflowId) {
        set("workflowId", workflowId);
    }

    public String getWorkflowId() {
        return get("workflowId");
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    public List<AdminLevelDTO> getAdminLevels() {
        return adminLevels;
    }

    public void setAdminLevels(List<AdminLevelDTO> adminLevels) {
        this.adminLevels = adminLevels;
    }

    public GeoExtents getCountryBounds() {
        return countryBounds;
    }

    public void setCountryBounds(GeoExtents countryBounds) {
        this.countryBounds = countryBounds;
    }


}
